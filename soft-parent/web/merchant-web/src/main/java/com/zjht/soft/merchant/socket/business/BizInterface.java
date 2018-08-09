package com.zjht.soft.merchant.socket.business;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

/**
 * 所有的Socket业务类，都需要实现这个接口
 * <br>
 * Created by 黄灿贤 on 2017年9月27日
 *
 * @version 1.0-SNAPSHOT
 */
public interface BizInterface {

    /** 商户订单号 */
    String BUSINESS_ID_FIELD = "order_id";
    /** 交易类型字段 */
    String BUSINESS_TYPE_FIELD = "txn_id";
    /** 交易日期 */
    String TXN_DATE            = "txn_date";
    /** 交易时间 */
    String TXN_TIME            = "txn_time";
    /** 商户号 */
    String MID                 = "mid";
    /** 终端号 */
    String TID                 = "tid";

    /**
     * 业务处理入口方法
     *
     * @param channel     当前客户端与服务端的Socket链接
     * @param requestJson 客户端发过来的Json字符串
     */
    void doTask(Channel channel, String requestJson);


}