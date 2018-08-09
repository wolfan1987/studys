package com.zjht.soft.bluelotus.socket.entity;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;

/**
 * Created by wuqiyang on 2017/9/20。
 */
public class PayOrderReq implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 交易码。
     * 默认值：841100
     */
    @JSONField(name = "txn_id")
    private String txnId;

    /**
     * 付款码。
     */
    @JSONField(name = "auth_code")
    private String authCode;

    /**
     * 商户订单号。
     */
    @JSONField(name = "order_id")
    private String orderId;

    /**
     * 版本号。
     */
    @JSONField(name = "version")
    private String version = "V1.0";

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
     * 交易金额。
     */
    @JSONField(name = "txn_amt")
    private int txnAmt;

    /**
     * 货币类型。
     * 默认值：CNY
     */
    @JSONField(name = "currency_code")
    private String currencyCode = "CNY";

    /**
     * 流水。
     * 格式：HHmmss
     */
    @JSONField(name = "systrace")
    private String sysTrace;

    /**
     * 备注。
     */
    @JSONField(name = "note")
    private String note;

    /**
     * 批次号。
     */
    @JSONField(name = "batch_no")
    private String batchNo;

    public String getTxnId() {
        return txnId;
    }

    public void setTxnId(String txnId) {
        this.txnId = txnId;
    }

    public String getAuthCode() {
        return authCode;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

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

    public int getTxnAmt() {
        return txnAmt;
    }

    public void setTxnAmt(int txnAmt) {
        this.txnAmt = txnAmt;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getSysTrace() {
        return sysTrace;
    }

    public void setSysTrace(String sysTrace) {
        this.sysTrace = sysTrace;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    @Override
    public String toString() {
        return "PayOrderReq{" + "txnId='" + txnId + '\'' + ", authCode='" + authCode + '\'' +
               ", orderId='" + orderId + '\'' + ", version='" + version + '\'' + ", mid='" + mid +
               '\'' + ", tid='" + tid + '\'' + ", txnAmt=" + txnAmt + ", currencyCode='" +
               currencyCode + '\'' + ", sysTrace='" + sysTrace + '\'' + ", note='" + note + '\'' +
               ", batchNo='" + batchNo + '\'' + '}';
    }

    public String toJsonString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("{");
        stringBuffer.append("\"txn_id\":").append("\"").append(txnId).append("\"");
        stringBuffer.append(",\"auth_code\":").append("\"").append(authCode).append("\"");
        stringBuffer.append(",\"order_id\":").append("\"").append(orderId).append("\"");
        stringBuffer.append(",\"version\":").append("\"").append(version).append("\"");
        stringBuffer.append(",\"mid\":").append("\"").append(mid).append("\"");
        stringBuffer.append(",\"tid\":").append("\"").append(tid).append("\"");
        stringBuffer.append(",\"txn_amt\":").append("\"").append(txnAmt).append("\"");
        stringBuffer.append(",\"currency_code\":").append("\"").append(currencyCode).append("\"");
        stringBuffer.append(",\"systrace\":").append("\"").append(sysTrace).append("\"");
        stringBuffer.append(",\"batch_no\":").append("\"").append(batchNo).append("\"");
        if (note != null) {
            stringBuffer.append(",\"note\":").append("\"").append(note).append("\"");
        }
        stringBuffer.append("}");
        return stringBuffer.toString();
    }
}
