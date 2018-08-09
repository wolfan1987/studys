package com.zjht.soft.merchant.socket;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by zjhtadmin on 2017/11/8.
 */
public class OrderEntity {

    private ChannelHandlerContext context;
    private Channel               channel;
    private String                requestJson;


    public OrderEntity(ChannelHandlerContext context, String requestJson) {
        this.context = context;
        this.requestJson = requestJson;
    }

    public ChannelHandlerContext getContext() {
        return context;
    }

    public void setContext(ChannelHandlerContext context) {
        this.context = context;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public String getRequestJson() {
        return requestJson;
    }

    public void setRequestJson(String requestJson) {
        this.requestJson = requestJson;
    }
}
