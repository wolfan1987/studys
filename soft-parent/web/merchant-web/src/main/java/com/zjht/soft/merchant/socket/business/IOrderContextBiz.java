package com.zjht.soft.merchant.socket.business;

import io.netty.channel.ChannelHandlerContext;

/**
 * Created by zjhtadmin on 2017/11/9.
 */
public interface IOrderContextBiz {

    void produceOrder(ChannelHandlerContext context, String requestJson);

}
