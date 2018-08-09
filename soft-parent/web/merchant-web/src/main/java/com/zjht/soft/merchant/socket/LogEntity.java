package com.zjht.soft.merchant.socket;

import com.alibaba.fastjson.JSONObject;
import com.zjht.soft.bluelotus.socket.entity.PayOrderReq;

/**
 * Created by zjhtadmin on 2017/11/16.
 */
public class LogEntity {

    private PayOrderReq req;

    private  JSONObject requestData;

    public LogEntity(PayOrderReq req, JSONObject requestData) {
        this.req = req;
        this.requestData = requestData;
    }

    public PayOrderReq getReq() {
        return req;
    }

    public void setReq(PayOrderReq req) {
        this.req = req;
    }

    public JSONObject getRequestData() {
        return requestData;
    }

    public void setRequestData(JSONObject requestData) {
        this.requestData = requestData;
    }
}
