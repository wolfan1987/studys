package com.zjht.soft.merchant.entity;

import com.zjht.solar.ecard.commons.database.dao.Model;

import java.util.Date;

/**
 * Created by wuqiyang on 2017/9/19。
 */
public class MerchantTransLog implements Model<Long> {

    private Long   id;
    private String orderId;
    private String reqData;
    private Date   updateTime;
    private Date   createTime;
    private String resData;
    /**
     * 业务代码 撤销：841500，支付：841100。
     */
    private String txnId;
    /**
     * 交易日期。
     */
    private String txnDate;
    /**
     * 交易时间。
     */
    private String txnTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getReqData() {
        return reqData;
    }

    public void setReqData(String reqData) {
        this.reqData = reqData;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getResData() {
        return resData;
    }

    public void setResData(String resData) {
        this.resData = resData;
    }

    public String getTxnDate() {
        return txnDate;
    }

    public void setTxnDate(String txnDate) {
        this.txnDate = txnDate;
    }

    public String getTxnTime() {
        return txnTime;
    }

    public void setTxnTime(String txnTime) {
        this.txnTime = txnTime;
    }

    @Override
    public Long getKey() {
        return null;
    }

    public String getTxnId() {
        return txnId;
    }

    public void setTxnId(String txnId) {
        this.txnId = txnId;
    }

    @Override
    public String toString() {
        return "{" + "id=" + id + ", orderId='" + orderId + '\'' + ", reqData='" + reqData + '\'' +
               ", updateTime=" + updateTime + ", createTime=" + createTime + ", resData='" +
               resData + '\'' + ", txnId='" + txnId + '\'' + ", txnDate='" + txnDate + '\'' +
               ", txnTime='" + txnTime + '\'' + '}';
    }
}
