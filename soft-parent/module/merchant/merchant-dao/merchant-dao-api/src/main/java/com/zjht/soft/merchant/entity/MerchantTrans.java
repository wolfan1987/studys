package com.zjht.soft.merchant.entity;

import com.zjht.solar.ecard.commons.database.dao.Model;

import java.util.Date;

/**
 * Created by wuqiyang on 2017/9/18。
 */
public class MerchantTrans implements Model<Long> {
    /**
     * 主键。
     */
    private Long    id;
    /**
     * 订单编号。
     */
    private String  orderId;
    /**
     * 请求参数。
     */
    private String  reqData;
    /**
     * pos终端号。
     */
    private String  tid;
    /**
     * 商户编号。
     */
    private String  mid;
    /**
     * 支付方式。
     */
    private Integer payment;
    /**
     * 消费金额,单位：分。
     */
    private int     txnAmt;

    /**
     * 优惠金额,单位：分。
     */
    private int favAmt;

    /**
     * 实扣金额,单位：分。
     */
    private int     payAmt;
    /**
     * 创建时间。
     */
    private Date    createTime;
    /**
     * 订单状态。
     * 0：已提交posp处理
     * 1：posp处理完成
     */
    private Integer orderStatus;
    /**
     * 更新时间。
     */
    private Date    updateTime;
    /**
     * posp返回数据。
     */
    private String  resData;
    /**
     * posp返回的订单状态。
     * 0：成功
     * 1：失败
     */
    private Integer status;
    /**
     * posp返回支付流水号。
     */
    private String  systrace;

    /**
     * 付款码。
     */
    private String authCode;
    /**
     * 货币类型。
     */
    private String currencyCode = "CNY";

    /**
     * 批次号。
     */
    private String batchNo;
    /**
     * 备注。
     */
    private String note;
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

    @Override
    public Long getKey() {
        return this.id;
    }

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

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public Integer getPayment() {
        return payment;
    }

    public void setPayment(Integer payment) {
        this.payment = payment;
    }

    public int getTxnAmt() {
        return txnAmt;
    }

    public void setTxnAmt(int txnAmt) {
        this.txnAmt = txnAmt;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Integer orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getResData() {
        return resData;
    }

    public void setResData(String resData) {
        this.resData = resData;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getSystrace() {
        return systrace;
    }

    public void setSystrace(String systrace) {
        this.systrace = systrace;
    }

    public String getAuthCode() {
        return authCode;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getTxnId() {
        return txnId;
    }

    public void setTxnId(String txnId) {
        this.txnId = txnId;
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

    public int getFavAmt() {
        return favAmt;
    }

    public void setFavAmt(int favAmt) {
        this.favAmt = favAmt;
    }

    public int getPayAmt() {
        return payAmt;
    }

    public void setPayAmt(int payAmt) {
        this.payAmt = payAmt;
    }

    @Override
    public String toString() {
        return "{" + " orderId='" + orderId + '\'' + ", reqData='" +
               reqData + '\'' + ", tid='" + tid + '\'' + ", mid='" + mid + '\'' + ", payment=" +
               payment + ", txnAmt=" + txnAmt + ", favAmt=" + favAmt + ", payAmt=" + payAmt +
               ", createTime=" + createTime + ", orderStatus=" + orderStatus + ", updateTime=" +
               updateTime + ", resData='" + resData + '\'' + ", status=" + status + ", systrace='" +
               systrace + '\'' + ", authCode='" + authCode + '\'' + ", currencyCode='" +
               currencyCode + '\'' + ", batchNo='" + batchNo + '\'' + ", note='" + note + '\'' +
               ", txnId='" + txnId + '\'' + ", txnDate='" + txnDate + '\'' + ", txnTime='" +
               txnTime + '\'' + '}';
    }
}
