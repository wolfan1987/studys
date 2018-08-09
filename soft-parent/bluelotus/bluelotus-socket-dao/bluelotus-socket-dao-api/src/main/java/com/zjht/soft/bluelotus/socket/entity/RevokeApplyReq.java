package com.zjht.soft.bluelotus.socket.entity;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;

/**
 * 撤销申请实体。
 * Created by yuanyaping on 2017/9/20.
 */
public class RevokeApplyReq implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 版本号。
     * 默认值：V1.0
     */
    @JSONField(name = "version")
    private String version;

    /**
     * 商户号。
     */
    @JSONField(name = "mid")
    private String mid;

    /**
     * 终端号。
     */
    @JSONField(name = "tid")
    private String tid;

    /**
     * 交易流水号。
     */
    @JSONField(name = "systrace")
    private String systrace;

    /**
     * 交易类型。
     * 默认值：841510
     */
    @JSONField(name = "txn_id")
    private String txnId;

    /**
     * 交易金额，单位为分。
     */
    @JSONField(name = "txn_amt")
    private int txnAmt;

    /**
     * 货币类型。
     * 默认值：CNY
     */
    @JSONField(name = "currency_code")
    private String currencyCode;

    /**
     * 商户订单号。
     */
    @JSONField(name = "order_id")
    private String orderId;

    /**
     * 交易备注。
     */
    @JSONField(name = "note")
    private String note;

    /**
     * 批次号。
     */
    @JSONField(name = "batch_no")
    private String batchNo;

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = (mid == null) ? null : mid.trim();
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public String getSystrace() {
        return systrace;
    }

    public void setSystrace(String systrace) {
        this.systrace = systrace;
    }

    public String getTxnId() {
        return txnId;
    }

    public void setTxnId(String txnId) {
        this.txnId = txnId;
    }

    public int getTxnAmt() {
        return txnAmt;
    }

    public void setTxnAmt(int txnAmt) {
        this.txnAmt = txnAmt;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public String toString() {
        return "{" + "version='" + version + '\'' + ", mid='" + mid + '\'' + ", tid='" + tid +
               '\'' + ", systrace='" + systrace + '\'' + ", txnId='" + txnId + '\'' + ", txnAmt=" +
               txnAmt + ", currencyCode='" + currencyCode + '\'' + ", orderId='" + orderId + '\'' +
               ", note='" + note + '\'' + ", batchNo='" + batchNo + '\'' + '}';
    }
}
