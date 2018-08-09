package com.zjht.soft.merchant.socket.business;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zjht.soft.bluelotus.socket.dao.RevokeApplyWriteDao;
import com.zjht.soft.bluelotus.socket.entity.RevokeApplyReq;
import com.zjht.soft.bluelotus.socket.entity.RevokeApplyRes;
import com.zjht.soft.merchant.entity.MerchantTrans;
import com.zjht.soft.merchant.entity.MerchantTransLog;
import com.zjht.soft.merchant.service.MerchantTransLogService;
import com.zjht.soft.merchant.service.MerchantTransService;
import com.zjht.soft.merchant.socket.ResponseConstant;
import io.netty.channel.Channel;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;

/**
 * 撤销的业务处理类。
 * <br>
 * Created by wuqiyang on 2017年9月28日
 *
 * @version 1.0-SNAPSHOT
 */

@Component
public class RevokeBiz implements BizInterface {

    private static final Logger log = LoggerFactory.getLogger(RevokeBiz.class);

    @Reference(version = "1.0.0")
    private RevokeApplyWriteDao revokeWriteDao;

    @Reference(version = "1.0.0")
    private MerchantTransService transService;

    @Reference(version = "1.0.0")
    private MerchantTransLogService transLogService;

    //业务处理入口
    public void doTask(Channel channel, String requestJson) {
        log.debug("开始处理--->撤销业务，requestJson={}", requestJson);
        try {
            RevokeApplyReq req = JSON.parseObject(requestJson, RevokeApplyReq.class);
            MerchantTrans trans = this.saveTransData(req, requestJson);
            // 记录日志
            MerchantTransLog orderLog = this.saveTransLog(req, requestJson);
            // 调用POSP接口
            RevokeApplyRes res = revokeWriteDao.revokeApply(req);
            String resposeJson = JSON.toJSONString(res);
            log.debug("撤销 POSP响应={}", resposeJson);
            // 更新日志
            this.updateTransLog(orderLog, resposeJson);
            this.updateTransData(trans, res);
            // 响应客户端
            channel.writeAndFlush(resposeJson);
            log.debug("处理完成--->撤销业务，orderLogId={}", orderLog.getId());
            if (checkRevoke(res)) {
                for (int i = 0; i < 10; i++) {
                    log.info("撤销失败，自动撤销第 {} 次", i);
                    MerchantTransLog revokelog = this.saveTransLog(req, requestJson);
                    // 调用POSP接口
                    RevokeApplyRes revokeres = revokeWriteDao.revokeApply(req);
                    String resJson = JSON.toJSONString(revokeres);
                    log.debug("第 {} 次 POSP响应={}", i, resJson);
                    // 更新日志
                    this.updateTransLog(revokelog, resJson);
                    // 记录日志
                    if (revokeres.getOrderStatus() == 2) {
                        break;
                    } else {
                        continue;
                    }
                }
            }
        } catch (Exception e) {
            HashMap<String, Object> errorMap = new HashMap<>();
            errorMap.put(
                ResponseConstant.BUSINESS_ERROR_CODE, ResponseConstant.BUSINESS_ERROR_MESG);
            errorMap.put(
                ResponseConstant.BUSINESS_EXCEPTION_INFO, ExceptionUtils.getRootCauseMessage(e));
            String resposeJson = JSON.toJSONString(errorMap);
            channel.writeAndFlush(resposeJson);
            log.error("处理--撤销[被扫]业务时，出现异常", e);
        } finally {
            channel.close();
        }
    }


    private boolean checkRevoke(RevokeApplyRes res) {
        if (res != null && !"00".equals(res.getRc())) {
            if (res.getOrderStatus() != 2) {
                String orderId = res.getOrderId();
                String txnId = BizTypeEnum.PAY.getValue();
                MerchantTrans merchantTrans = transService.findByOrderId(orderId, txnId);
                if (merchantTrans != null) {
                    return merchantTrans.getOrderStatus() == 1;
                }
            }
        }
        return false;
    }

    private MerchantTrans saveTransData(RevokeApplyReq req, String requestJson) {
        MerchantTrans trans = new MerchantTrans();
        JSONObject reqData = JSON.parseObject(requestJson);
        trans.setBatchNo(req.getBatchNo());
        trans.setCreateTime(new Date());
        trans.setMid(req.getMid());
        trans.setNote(req.getNote());
        trans.setOrderId(req.getOrderId());
        trans.setCurrencyCode(req.getCurrencyCode());
        trans.setReqData(requestJson);
        trans.setTid(req.getTid());
        trans.setTxnAmt(req.getTxnAmt());
        trans.setTxnId(req.getTxnId());
        trans.setTxnDate(reqData.getString(BizInterface.TXN_DATE));
        trans.setTxnTime(reqData.getString(BizInterface.TXN_TIME));
        trans.setBatchNo(req.getBatchNo());
        trans.setOrderStatus(-1);
        transService.create(trans);
        return trans;
    }

    private void updateTransData(MerchantTrans trans, RevokeApplyRes res) {
        String resposeJson = JSON.toJSONString(res);
        trans.setResData(resposeJson);
        trans.setUpdateTime(new Date());
        //trans.setPayment(res.getPayType());
        trans.setOrderStatus(res.getOrderStatus());
        trans.setStatus(res.getOrderStatus());
        transService.update(trans);
    }

    private MerchantTransLog saveTransLog(RevokeApplyReq req, String requestJson) {
        JSONObject reqData = JSON.parseObject(requestJson);
        MerchantTransLog orderLog = new MerchantTransLog();
        orderLog.setOrderId(req.getOrderId());
        orderLog.setTxnId(req.getTxnId());
        orderLog.setReqData(requestJson);
        orderLog.setCreateTime(new Date());
        orderLog.setTxnDate(reqData.getString(BizInterface.TXN_DATE));
        orderLog.setTxnTime(reqData.getString(BizInterface.TXN_TIME));
        transLogService.create(orderLog);
        return orderLog;
    }

    private void updateTransLog(MerchantTransLog orderLog, String resposeJson) {
        orderLog.setResData(resposeJson);
        orderLog.setUpdateTime(new Date());
        transLogService.update(orderLog);
    }
}
