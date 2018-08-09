package com.zjht.soft.merchant.socket.business;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zjht.soft.bluelotus.socket.dao.TransactionQueryWriteDao;
import com.zjht.soft.bluelotus.socket.entity.TransactionQueryReq;
import com.zjht.soft.bluelotus.socket.entity.TransactionQueryRes;
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

@Component
public class QueryBiz implements BizInterface {

    private static final Logger log = LoggerFactory.getLogger(QueryBiz.class);

    @Reference(version = "1.0.0")
    private TransactionQueryWriteDao transactionQueryWriteDao;

    @Reference(version = "1.0.0")
    private MerchantTransService transService;

    @Reference(version = "1.0.0")
    private MerchantTransLogService transLogService;

    //业务处理入口
    public void doTask(Channel channel, String requestJson) {

        log.debug("开始处理--->查询业务，requestJson={}", requestJson);
        try {
            TransactionQueryReq req = JSON.parseObject(requestJson, TransactionQueryReq.class);
            MerchantTrans trans = this.saveTransData(req, requestJson);
            // 记录日志
            MerchantTransLog orderLog = this.saveTransLog(req, requestJson);
            // 调用POSP接口
            TransactionQueryRes res = transactionQueryWriteDao.transactionQuery(req);
            String resposeJson = JSON.toJSONString(res);
            log.debug("POSP响应={}", resposeJson);
            // 更新日志
            this.updateTransLog(orderLog, resposeJson);
            this.updateTransData(trans, res);
            // 响应客户端
            channel.writeAndFlush(resposeJson);
            log.debug("处理完成--->查询业务，orderLogId={}", orderLog.getId());
        } catch (Exception e) {
            HashMap<String, Object> errorMap = new HashMap<String, Object>();
            errorMap.put(
                ResponseConstant.BUSINESS_ERROR_CODE, ResponseConstant.BUSINESS_ERROR_MESG);
            errorMap.put(
                ResponseConstant.BUSINESS_EXCEPTION_INFO, ExceptionUtils.getRootCauseMessage(e));
            String resposeJson = JSON.toJSONString(errorMap);
            channel.writeAndFlush(resposeJson);
            log.error("处理--查询业务，出现异常", e);
        } finally {
            channel.close();
        }
    }


    private MerchantTrans saveTransData(TransactionQueryReq req, String requestJson) {

        MerchantTrans trans = new MerchantTrans();
        JSONObject reqData = JSON.parseObject(requestJson);
        trans.setBatchNo(req.getBatchNo());
        trans.setCreateTime(new Date());
        trans.setMid(req.getMid());
        trans.setOrderId(req.getOrderId());
        trans.setReqData(requestJson);
        trans.setTid(req.getTid());
        trans.setTxnId(req.getTxnId());
        trans.setTxnDate(reqData.getString(BizInterface.TXN_DATE));
        trans.setTxnTime(reqData.getString(BizInterface.TXN_TIME));
        trans.setBatchNo(req.getBatchNo());
        trans.setOrderStatus(0);
        trans = transService.create(trans);
        return trans;

    }

    private void updateTransData(MerchantTrans trans, TransactionQueryRes res) {
        String resposeJson = JSON.toJSONString(res);
        trans.setResData(resposeJson);
        trans.setUpdateTime(new Date());
        trans.setStatus(res.getOrderStatus());
        trans.setOrderStatus(res.getOrderStatus());//posp处理完成
        transService.update(trans);
    }

    private MerchantTransLog saveTransLog(TransactionQueryReq req, String requestJson) {
        JSONObject reqData = JSON.parseObject(requestJson);
        MerchantTransLog orderLog = new MerchantTransLog();
        orderLog.setOrderId(req.getOrderId());
        orderLog.setTxnId(req.getTxnId());
        orderLog.setCreateTime(new Date());
        orderLog.setReqData(requestJson);
        orderLog.setTxnDate(reqData.getString(BizInterface.TXN_DATE));
        orderLog.setTxnTime(reqData.getString(BizInterface.TXN_TIME));
        orderLog = transLogService.create(orderLog);
        return orderLog;
    }

    private void updateTransLog(MerchantTransLog orderLog, String resposeJson) {
        orderLog.setResData(resposeJson);
        orderLog.setUpdateTime(new Date());
        transLogService.update(orderLog);
    }

}
