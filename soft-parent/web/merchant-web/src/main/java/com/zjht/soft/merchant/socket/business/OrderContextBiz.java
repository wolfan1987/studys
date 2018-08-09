package com.zjht.soft.merchant.socket.business;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zjht.soft.bluelotus.socket.dao.PayOrderSocketWriteDao;
import com.zjht.soft.bluelotus.socket.entity.PayOrderReq;
import com.zjht.soft.bluelotus.socket.entity.PayOrderRes;
import com.zjht.soft.merchant.dao.PayTypeDao;
import com.zjht.soft.merchant.entity.CodeAcq;
import com.zjht.soft.merchant.entity.MerchantTrans;
import com.zjht.soft.merchant.entity.MerchantTransLog;
import com.zjht.soft.merchant.entity.PayType;
import com.zjht.soft.merchant.service.CodeAcqService;
import com.zjht.soft.merchant.service.MerchantTransLogService;
import com.zjht.soft.merchant.service.MerchantTransService;
import com.zjht.soft.merchant.service.PosInfoService;
import com.zjht.soft.merchant.socket.LogEntity;
import com.zjht.soft.merchant.socket.OrderEntity;
import com.zjht.soft.merchant.socket.ResponseConstant;
import com.zjht.soft.merchant.socket.component.CustomNettyServer;
import com.zjht.soft.merchant.socket.component.CustomServerHandler;
import com.zjht.soft.merchant.socket.polling.LoadCodeAcqJob;
import com.zjht.soft.merchant.socket.polling.PollingDataJob;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.Attribute;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.ConnectionClosedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 处理基于Netty订单请求的相关业务实现
 *
 * @Author AndrewLiu
 * @Date 2017.11.09
 */
@Component
public class OrderContextBiz extends AbstractBiz implements IOrderContextBiz {

    private static final Logger log = LoggerFactory.getLogger(OrderContextBiz.class);

    private              ArrayBlockingQueue<OrderEntity> orderLists = new ArrayBlockingQueue<>(500);
    private              ArrayBlockingQueue<LogEntity>   logLists   = new ArrayBlockingQueue<>(500);
    private static final String                          DEF_TID    = "00000000";
    @Reference(version = "1.0.0")
    private PayOrderSocketWriteDao pospSocketDao;

    @Reference(version = "1.0.0")
    private PayTypeDao payTypeDao;

    @Reference(version = "1.0.0")
    private MerchantTransService transService;

    @Reference(version = "1.0.0")
    private MerchantTransLogService transLogService;

    @Reference(version = "1.0.0")
    private CodeAcqService codeAcqService;

    @Reference(version = "1.0.0")
    private PosInfoService posInfoService;

    public static boolean TEST_PAY_FLAG = true;//测试用，模拟超时自动撤单,默认为false不启用

    @Autowired
    private QueryBiz  queryBiz;
    @Autowired
    private RevokeBiz revokeBiz;

    private static AtomicBoolean isInitThread = new AtomicBoolean(false);


    @Override
    public void produceOrder(ChannelHandlerContext context, String requestJson) {
        try {
            orderLists.add(new OrderEntity(context, requestJson));
            //   orderQueue.addLock();
            //没有初始化，则初始化7个线程任务(7个任务，最大7个线程)
            if (!isInitThread.get()) {
                for (int i = 0; i < 7; i++) {
                    orderExec.submit(new PayOrderProcessor());
                }
                //初始化3个日志线程
                for (int i = 0; i < 3; i++) {
                    logExec.submit(new LogProcesser());
                }
                isInitThread.set(true);
            }
        } catch (Exception anyException) {
            log.error("---添加订单到队列orderLists出错,异常:" + anyException.getMessage());
            anyException.printStackTrace();
        }

    }

    class PayOrderProcessor implements Runnable {
        @Override
        public void run() {
            while (true) {
                OrderEntity order = null;
                try {
                    order = orderLists.take();
                } catch (InterruptedException e) {
                    log.error("---从订单队列获取订单出错，线程中断：Exceptionmsg=" + e.getMessage());
                    e.printStackTrace();
                    continue;
                }
                String requestJson = order.getRequestJson();
                Channel channel = order.getContext().channel();
                log.debug("NettyServer receive requestJson={}", requestJson);
                JSONObject requestData = JSON.parseObject(requestJson);// 解释Json
                // 必要的基础参数检验
                Map<String, Object> result = checkData(requestData);
                boolean success = MapUtils.getBooleanValue(result, "success");
                if (!success) {
                    log.warn("基础参数检验失败===>{}", MapUtils.getString(result, "message"));
                    String resposeJson = JSON.toJSONString(result);
                    channel.writeAndFlush(resposeJson);
                    channel.close();
                    continue;
                } else {
                    // 异常撤单时要用
                    String businessId = requestData.getString(BizInterface.BUSINESS_ID_FIELD);
                    Attribute<String> attr = channel.attr(CustomServerHandler.BUSINESS_ID_KEY);
                    attr.set(businessId);
                    log.debug("channel.attr值为:{}", attr.get());
                    // 保存channel
                    CustomNettyServer.ChannelGroup.add(businessId, channel);
                    order.getContext().fireChannelReadComplete();
                }
                String businessTypeStr = requestData.getString(BizInterface.BUSINESS_TYPE_FIELD);
                BizTypeEnum businessType = BizTypeEnum.getEnumByValue(businessTypeStr);
                if (BizTypeEnum.PAY.compareTo(businessType) == 0) {
                    log.info("开始处理--->扫码付[被扫]业务，requestJson={}", requestJson);
                    PayOrderReq req = null;
                    try {
                        req = JSON.parseObject(requestJson, PayOrderReq.class);
                        int payType = checkPayFlag(req);//检查支付方式
                        if (payType == 0) {
                            throw new IllegalArgumentException("暂不支持的支付类型");
                        } else if (payType == -1) {
                            throw new IllegalArgumentException("付款码长度不合法");
                        }
                        //记录so请求日志
                        MerchantTrans trans = this.saveTransData(req, requestData, requestJson,
                                                                 payType);
                        req.setBatchNo(trans.getBatchNo());
                        req.setSysTrace(trans.getSystrace());
                        // 记录请求posp日志
                        //String requestPosJson = JSON.toJSONString(req);
                        //  String requestPosJson = req.toJsonString();
                        //   MerchantTransLog orderLog = this.saveTransLog(req, requestData);
                        //将日志加入队列，由日志线程处理日志
                        logLists.add(new LogEntity(req, requestData));
                        // 调用POSP接口
                        PayOrderRes respose = pospSocketDao.payOrder(req);
                        respose.setPayType(trans.getPayment());
                        int favAmt = respose.getFavAmt();
                        int txnAmt = respose.getTxnAmt();
                        trans.setFavAmt(favAmt);
                        if (favAmt > 0) {
                            respose.setPaymt(txnAmt - favAmt);
                            trans.setPayAmt(txnAmt - favAmt);
                        } else {
                            respose.setPaymt(txnAmt);
                            trans.setPayAmt(txnAmt);
                        }
                        //String resposeJson = JSON.toJSONString(respose);
                        String resposeJson = respose.toJsonString();
                        log.info("POSP响应={}", resposeJson);

//                                                if (TEST_PAY_FLAG) {
//                                                    respose.setOrderStatus(6);
//                                                }

                        // 更新日志
                        // this.updateTransLog(orderLog, resposeJson);
                        //this.updateTransData(trans, respose, resposeJson);
                        trans.setUpdateTime(new Date());
                        trans.setResData(resposeJson);
                        trans.setOrderStatus(respose.getOrderStatus());// posp处理完成
                        trans.setStatus(respose.getOrderStatus());


                        boolean updateResult = transService.update(trans);//更新订单状态
                        if (updateResult) {
                            // 4支付状态未明; 6用户输入密码; 后台线程轮询结果
                            if (4 == respose.getOrderStatus() || 6 == respose.getOrderStatus()) {
                                PollingDataJob.QUERY_TASK_LIST.add(trans);
                                log.info("orderId={}的扫码付[被扫]业务处理中，由后台线程轮询结果后响应客户端",
                                         respose.getOrderId());
                            }
                            // 其它的返回给SO库
                            else {
                                   writeChannel(channel,resposeJson);
                                log.info("处理完成--->扫码付[被扫]业务，orderId={} orderStatus={}",
                                         respose.getOrderId(), respose.getOrderStatus());
                            }
                        } else {
                                respose.setOrderStatus(3);
                                respose.setRcDetail("交易异常，若订单支付成功将在一分钟内退款");
                                resposeJson = respose.toJsonString();
                                writeChannel(channel,resposeJson);
                            log.info("处理完成--->扫码付[被扫]业务，orderId={} orderStatus={}",
                                     respose.getOrderId(), respose.getOrderStatus());
                        }

                    } catch (Exception e) {
                        PayOrderRes respose = new PayOrderRes();
                        respose.setOrderId(req.getOrderId());
                        respose.setRc("00");
                        respose.setOrderStatus(3);
                        respose.setRcDetail(ResponseConstant.BUSINESS_ERROR_MESG);
                        if (e instanceof IllegalArgumentException) {
                            respose.setRc("12");
                            respose.setRcDetail(e.getMessage());
                            log.warn("处理--扫码付[被扫]业务时，OrderId={} 出现异常。case = {}", req.getOrderId(),
                                     e.getMessage());
                        } else if (e.getMessage().contains(
                            "SQLIntegrityConstraintViolationException")) {
                            respose.setRc("96");
                            respose.setRcDetail("支付失败，订单号重复");
                            log.error(
                                "订单号重复 OrderId={}  异常信息 {}", req.getOrderId(), e.getMessage());
                        } else {
                            log.error(
                                "处理--扫码付[被扫]业务时，出现异常。OrderId={} 异常信息 {}", req.getOrderId(), e);
                        }
                        String resposeJson = JSON.toJSONString(respose);
                        log.debug("返回SO数据-->{}", resposeJson);
                        channel.writeAndFlush(resposeJson);
                        channel.close();
                    }
                } else if (BizTypeEnum.QUERY.compareTo(businessType) == 0) {
                    queryBiz.doTask(channel, requestJson);
                } else if (BizTypeEnum.CANCEL.compareTo(businessType) == 0) {
                    revokeBiz.doTask(channel, requestJson);
                } else {
                    log.error("不支持的接口类型：  businessTypeStr={}", businessTypeStr);
                }
            }
        }


        private  void writeChannel(Channel channel,String msg) throws ConnectionClosedException{
            if (channel.isOpen() && channel.isActive() && channel.isWritable()) {
                log.info("payBiz响应返回SO数据-->{}", msg);
                channel.writeAndFlush(msg);
                channel.close();
            } else {
                throw new ConnectionClosedException("netty与So库的连接异常！");
            }
        }

        private MerchantTrans saveTransData(
            PayOrderReq req, JSONObject requestData, String requestJson, int payType)
            throws Exception {
            MerchantTrans trans = new MerchantTrans();
            trans.setPayment(payType);
            trans.setAuthCode(req.getAuthCode());
            trans.setCreateTime(new Date());
            trans.setMid(req.getMid());
            trans.setNote(req.getNote());
            trans.setOrderId(req.getOrderId());
            trans.setCurrencyCode(req.getCurrencyCode());
            trans.setReqData(requestJson);
            trans.setTid(req.getTid());
            trans.setTxnAmt(req.getTxnAmt());
            trans.setTxnId(req.getTxnId());
            trans.setTxnDate(requestData.getString(BizInterface.TXN_DATE));
            trans.setTxnTime(requestData.getString(BizInterface.TXN_TIME));
            trans.setBatchNo(req.getBatchNo());
            trans.setOrderStatus(0);
            return transService.create(trans);
        }

        private void updateTransData(MerchantTrans trans, PayOrderRes res, String resposeJson) {
            trans.setUpdateTime(new Date());
            trans.setResData(resposeJson);
            trans.setOrderStatus(res.getOrderStatus());// posp处理完成
            trans.setStatus(res.getOrderStatus());
            transService.update(trans);
        }

        /**
         * 检查此次支付方式商户是否支持。
         *
         * @param payReq 请求参数
         * @return true or false
         */

        private int checkPayFlag(PayOrderReq payReq) {
            try {
                String key;
                if (payReq != null) {
                    key = payReq.getMid() + payReq.getTid();
                    CodeAcq codeAcq = LoadCodeAcqJob.getPayType(payReq.getAuthCode());
                    Integer payCode = LoadCodeAcqJob.PAY_FLAG_MAP.get(key);
                    if (payCode == null) { //如果为空则用缺省终端号匹配
                        key = payReq.getMid() + DEF_TID;
                        payCode = LoadCodeAcqJob.PAY_FLAG_MAP.get(key);
                    }
                    if (codeAcq == null) {
                        codeAcq = getPayType(payReq.getAuthCode());
                        if (codeAcq == null) {
                            return 0;
                        }
                        if (payCode == null) {
                            PayType payType = payTypeDao.findByMidAndTid(payReq.getMid(),
                                                                         payReq.getTid());
                            if (payType == null) {
                                payType = payTypeDao.findByMidAndTid(payReq.getMid(), DEF_TID);
                            }
                            payCode = payType.getPayCode();
                        }
                    }
                    if (codeAcq.getCodeLen() != payReq.getAuthCode().length()) {
                        return -1;//付款码与定义的长度不一致
                    }
                    int payFlag = codeAcq.getPayFlag();
                    //若payCode还是为null则些商户的支付方式未配置
                    if (payCode != null && payFlag != 0 && (payFlag & payCode) == payFlag) {
                        return codeAcq.getPayType();
                    }
                }
            } catch (Exception ex) {
                log.error("检查支付方式异常 {}", ex);
            }
            return 0;
        }

       /* */

        /**
         * 根据付款码[auth_code]的开头，来判断是哪种支付类型。
         *
         * @param authCode 支付码
         * @return
         */
        private CodeAcq getPayType(String authCode) {
            CodeAcq result = null;
            authCode = StringUtils.trimToEmpty(authCode);
            List<CodeAcq> cardBinList = codeAcqService.findAll();
            for (CodeAcq codeAcq : cardBinList) {
                String headCode = codeAcq.getHeadCode();
                if (StringUtils.startsWithIgnoreCase(authCode, headCode)) {
                    result = codeAcq;
                    break;
                }
            }
            if (result == null) {
                log.warn("无法判断支付类型，authCode={}", authCode);
            }
            return result;
        }
    }

    class LogProcesser implements Runnable {

        @Override
        public void run() {
            while (true) {
                try {
                    LogEntity logEntity = logLists.take();
                    if (logEntity != null) {
                        saveTransLog(logEntity.getReq(), logEntity.getRequestData());
                    }
                } catch (InterruptedException e) {
                    log.error("---日志处理线程中断：interrupteException=" + e.getMessage());
                    e.printStackTrace();
                    continue;
                } catch (Exception anyException) {
                    log.error("---日志处理线程出错：anyException=" + anyException.getMessage());
                    anyException.printStackTrace();
                    continue;
                }
            }
        }

        private void saveTransLog(PayOrderReq req, JSONObject requestData) {
            try {
                MerchantTransLog orderLog = new MerchantTransLog();
                orderLog.setOrderId(req.getOrderId());
                orderLog.setTxnId(req.getTxnId());
                orderLog.setReqData(req.toJsonString());
                orderLog.setCreateTime(new Date());
                orderLog.setTxnDate(requestData.getString(BizInterface.TXN_DATE));
                orderLog.setTxnTime(requestData.getString(BizInterface.TXN_TIME));
                orderLog = transLogService.create(orderLog);
                log.info("日志记录表中的ID为 {}", orderLog.getId());
            } catch (Exception anyException) {
                throw anyException;
            }

        }

        private void updateTransLog(MerchantTransLog orderLog, String resposeJson) {
            orderLog.setResData(resposeJson);
            orderLog.setUpdateTime(new Date());
            transLogService.update(orderLog);
        }
    }


}


