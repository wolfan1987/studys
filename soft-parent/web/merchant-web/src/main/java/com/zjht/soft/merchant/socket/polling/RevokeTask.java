package com.zjht.soft.merchant.socket.polling;

import com.zjht.soft.bluelotus.socket.dao.RevokeApplyWriteDao;
import com.zjht.soft.bluelotus.socket.dao.TransactionQueryWriteDao;
import com.zjht.soft.bluelotus.socket.entity.RevokeApplyReq;
import com.zjht.soft.bluelotus.socket.entity.RevokeApplyRes;
import com.zjht.soft.bluelotus.socket.entity.TransactionQueryReq;
import com.zjht.soft.bluelotus.socket.entity.TransactionQueryRes;
import com.zjht.soft.merchant.entity.MerchantTrans;
import com.zjht.soft.merchant.entity.PosInfo;
import com.zjht.soft.merchant.service.MerchantTransService;
import com.zjht.soft.merchant.socket.business.BizTypeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

public class RevokeTask implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(RevokeTask.class);

    private MerchantTrans data;

    private RevokeApplyWriteDao pospRevokeDao;

    private TransactionQueryWriteDao pospQueryDao;

    private MerchantTransService transService;

    public RevokeTask(
        MerchantTrans order, RevokeApplyWriteDao pospRevokeDao,
        TransactionQueryWriteDao pospQueryDao, MerchantTransService transService) {
        this.data = order;
        this.pospRevokeDao = pospRevokeDao;
        this.pospQueryDao = pospQueryDao;
        this.transService = transService;
    }

    // 调用POSP撤销接口
    public void run() {
        try {
            String orderId = data.getOrderId();
            log.info("调用自动超时撤单，OrderId={}", orderId);
            int oldOrderStatus = data.getOrderStatus();
            // 需要继续进行撤单操作的
            if (oldOrderStatus == -2) {
                this.revoke();
            }
            // 需要查询撤消结果
            else if (oldOrderStatus == 8) {
                this.query();
            } else {
                log.warn("oldOrderStatus异常,检查查询SQL！！！ orderId={} OrderStatus={}", orderId,
                         oldOrderStatus);
            }
        } catch (Exception e) {
            log.error("调用POSP撤销接口时，出现异常", e);
        }
    }

    /**
     * 撤单
     *
     * @throws Exception
     */
    private void revoke() throws Exception {
        try {
            RevokeApplyReq revokeApplyReq = new RevokeApplyReq();
            revokeApplyReq.setBatchNo(data.getBatchNo());
            revokeApplyReq.setMid(data.getMid());
            revokeApplyReq.setTid(data.getTid());
            revokeApplyReq.setOrderId(data.getOrderId());
            revokeApplyReq.setTxnAmt(data.getTxnAmt());
            revokeApplyReq.setTxnId(BizTypeEnum.REVOKE.getValue());
            //revokeApplyReq.setSystrace(data.getSystrace());
            PosInfo   newPosInfo =  transService.getNextPosInfo(data.getMid(),data.getTid());
            if(newPosInfo!=null){
                revokeApplyReq.setSystrace(newPosInfo.getSystrace());
            }else{
                throw new IllegalArgumentException("2次获取终端撤销流水号异常");
            }
        log.info("开始--->调POSP[撤单]接口 orderId={}", data.getOrderId());
            RevokeApplyRes pospResponse = pospRevokeDao.revokeApply(revokeApplyReq);
            int orderStatus = pospResponse.getOrderStatus();
            log.info("结束--->调POSP[撤单]接口 orderId={}，返回OrderStatus={}", data.getOrderId(),
                     orderStatus);
            if (orderStatus == 2 || orderStatus == 8 || orderStatus == 7) {
                data.setOrderStatus(orderStatus);
            }
            data.setUpdateTime(new Date());
            transService.update(data);
        } catch (Exception ex) {
            log.error("撤销异常 orderId={} ex={}", data.getOrderId(), ex.getMessage());
        }

    }

    /**
     * 查询撤单结果
     *
     * @throws Exception
     */
    private void query() throws Exception {
        TransactionQueryReq queryReq = new TransactionQueryReq();
        queryReq.setBatchNo(data.getBatchNo());
        queryReq.setSystrace(data.getSystrace());
        queryReq.setOrderId(data.getOrderId());
        queryReq.setMid(data.getMid());
        queryReq.setTid(data.getTid());
        queryReq.setTxnId(BizTypeEnum.QUERY.getValue());
        log.info("开始--->调POSP[查询撤单结果]接口 orderId={}", data.getOrderId());
        try {
            TransactionQueryRes pospResponse = pospQueryDao.transactionQuery(queryReq);
            int orderStatus = pospResponse.getOrderStatus();
            log.info("结束--->调POSP[查询撤单结果]接口 orderId={}，返回OrderStatus={}", data.getOrderId(),
                     orderStatus);
            if (orderStatus == 2 || orderStatus == 8 || orderStatus == 7) {
                data.setOrderStatus(orderStatus);
            } else {
                log.info("OrderId={}的需要重新发起撤单，OrderStatus={}", pospResponse.getOrderId(),
                         orderStatus);
                if(orderStatus == 3){ //如果=3，表示撤消的订单不存在
                    data.setOrderStatus(3);
                }else{
                    data.setOrderStatus(-2);// 重新撤单
                }
            }
            data.setUpdateTime(new Date());
            transService.update(data);
        } catch (Exception ex) {
            log.error("调POSP[查询撤单结果]接口 异常 orderId={} ex={}", data.getOrderId(), ex);
        }


    }

}
