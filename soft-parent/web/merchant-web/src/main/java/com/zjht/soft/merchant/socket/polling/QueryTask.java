package com.zjht.soft.merchant.socket.polling;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zjht.soft.bluelotus.socket.dao.TransactionQueryWriteDao;
import com.zjht.soft.bluelotus.socket.entity.TransactionQueryReq;
import com.zjht.soft.bluelotus.socket.entity.TransactionQueryRes;
import com.zjht.soft.merchant.entity.MerchantTrans;
import com.zjht.soft.merchant.service.MerchantTransService;
import com.zjht.soft.merchant.socket.business.BizTypeEnum;
import com.zjht.soft.merchant.socket.business.OrderContextBiz;
import com.zjht.soft.merchant.socket.component.CustomNettyServer;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

public class QueryTask implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(QueryTask.class);

    private MerchantTrans data;

    private TransactionQueryWriteDao pospSocketDao;

    private MerchantTransService transService;

    private int timeout;

    public QueryTask(
        MerchantTrans order, TransactionQueryWriteDao pospSocketDao,
        MerchantTransService transService, int timeout) {
        this.data = order;
        this.pospSocketDao = pospSocketDao;
        this.transService = transService;
        this.timeout = timeout;
    }

    // 调用POSP查询接口
    public void run() {
        Date runTime = new Date();
        String orderId = data.getOrderId();
        try {
            log.info("调用POSP查询接口，OrderId={}", orderId);
            TransactionQueryReq queryReq = new TransactionQueryReq();
            queryReq.setBatchNo(data.getBatchNo());
            queryReq.setSystrace(data.getSystrace());
            queryReq.setOrderId(data.getOrderId());
            queryReq.setMid(data.getMid());
            queryReq.setTid(data.getTid());
            queryReq.setTxnId(BizTypeEnum.QUERY.getValue());
            TransactionQueryRes pospResponse = pospSocketDao.transactionQuery(queryReq);

            log.info("posp轮询结果 orderId= {},orderStatus= {}", orderId,
                     pospResponse.getOrderStatus());
            // 处理查询支付结果时，无卡类型
            pospResponse.setPayType(data.getPayment());
            int favAmt = pospResponse.getFavAmt();
            int txnAmt = pospResponse.getTxnAmt();
            if (favAmt > 0) {
                pospResponse.setPaymt(txnAmt - favAmt);
            } else {
                pospResponse.setPaymt(txnAmt);
            }
            int newOrderStatus = pospResponse.getOrderStatus();

            //测试用，模拟超时自动撤单
//                        if (OrderContextBiz.TEST_PAY_FLAG) {
//                            newOrderStatus = 6;
//                        }

            Date createTime = data.getCreateTime();
            Date timeoutTime = DateUtils.addSeconds(createTime, timeout);
            Date nextRunTime = DateUtils.addSeconds(runTime, 5);
            Date flagDate = DateUtils.addSeconds(new Date(), -timeout);
            // 超时的，直接返回失败给客户端
            if (createTime.compareTo(flagDate) <= 0 || timeoutTime.compareTo(nextRunTime) <= 0) {
                log.debug("订单轮询超时 orderId={} , orderStatus={}", orderId, newOrderStatus);
                PollingDataJob.QUERY_TASK_LIST.remove(data);//移除查询订单集合
                JSONObject resJson = JSON.parseObject(data.getResData());
                resJson.put("order_status", "3");
                resJson.put("rc_detail", "支付超时");
                log.debug("queryTask 轮询返回SO-->{}", resJson);
                data.setOrderStatus(-2);
                data.setUpdateTime(new Date());
                transService.update(data);
                CustomNettyServer.ChannelGroup.push(
                    data.getOrderId(), resJson.toJSONString(), true);

            }
            // 得到最终结果为【成功、失败、订单关闭】直接返回失败给客户端
            else if (newOrderStatus == 1 || newOrderStatus == 3 || newOrderStatus == 7) {
                log.debug("订单轮询结果为 orderId={} , orderStatus={}", orderId, newOrderStatus);
                PollingDataJob.QUERY_TASK_LIST.remove(data);//移除查询订单集合
                String responseJson = JSON.toJSONString(pospResponse);
                data.setOrderStatus(newOrderStatus);
                data.setUpdateTime(new Date());
                boolean result = transService.update(data);
                if (result) {
                    CustomNettyServer.ChannelGroup.push(data.getOrderId(), responseJson, true);
                } else {
                    pospResponse.setOrderStatus(3);
                    pospResponse.setRcDetail("交易异常，若订单支付成功将在一分钟内退款");
                    responseJson = JSON.toJSONString(pospResponse);
                    CustomNettyServer.ChannelGroup.push(data.getOrderId(), responseJson, true);
                }
            } else {
                log.info("继续等下一次轮询--->OrderId ={},orderStatus = {}", orderId, newOrderStatus);
            }
        } catch (Exception e) {
            log.error("调用POSP查询接口时，出现异常, OrderId={}", orderId, e);
        }
    }

}
