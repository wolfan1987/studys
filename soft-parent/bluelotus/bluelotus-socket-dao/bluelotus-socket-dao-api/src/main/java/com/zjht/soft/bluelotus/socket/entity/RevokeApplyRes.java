package com.zjht.soft.bluelotus.socket.entity;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;

/**
 * 撤销申请返回实体。
 * Created by yuanyaping on 2017/9/20.
 */
public class RevokeApplyRes implements Serializable {
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
     * 默认值：841500
     */
    @JSONField(name = "txn_id")
    private String txnId = "841500";

    /**
     * 原交易流水号。
     */
    @JSONField(name = "osystrace")
    private String osystrace;

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
     * 交易返回码。
     * 00 ：表示成功,其他都是失败
     */
    @JSONField(name = "rc")
    private String rc;


    /**
     * 返回码信息。
     */
    @JSONField(name = "rc_detail")
    private String rcDetail;

    /**
     * 交易时间，hhmmss。
     */
    @JSONField(name = "txn_time")
    private String txnTime;

    /**
     * 交易状态。
     * 0-成功，1-处理中，2-失败
     */
    @JSONField(name = "order_status")
    private int orderStatus;

    /**
     * 商户订单号。
     */
    @JSONField(name = "order_id")
    private String orderId;

    /**
     * 参考号。
     */
    @JSONField(name = "rrn")
    private String rrn;

    /**
     * 交易日期，yyyymmdd。
     */
    @JSONField(name = "txn_date")
    private String txnDate;

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

    /**
     * 原批次号。
     */
    @JSONField(name = "obatch_no")
    private String obatchNo;

    /**
     * 卡类型。
     * <p>
     * 0=银联刷卡1=银联扫码，2=汇通卡，3=汇通卡电子现金卷，
     * 4=翼支付扫码(暂不对接)，5=微信支付,6=支付宝(暂不对接)，
     * 7=QQ钱包(暂不对接),8=外币卡(暂不对接),9=中经扫码。
     * </p>
     */
    @JSONField(name = "pay_type")
    private Integer payType;

    public Integer getPayType() {
        return payType;
    }

    public void setPayType(Integer payType) {
        this.payType = payType;
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
        this.mid = mid;
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

    public String getOsystrace() {
        return osystrace;
    }

    public void setOsystrace(String osystrace) {
        this.osystrace = osystrace;
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

    public String getRc() {
        return rc;
    }

    public void setRc(String rc) {
        this.rc = rc;
    }

    public String getRcDetail() {
        return rcDetail;
    }

    public void setRcDetail(String rcDetail) {
        this.rcDetail = rcDetail;
    }

    public String getTxnTime() {
        return txnTime;
    }

    public void setTxnTime(String txnTime) {
        this.txnTime = txnTime;
    }

    public int getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(int orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getRrn() {
        return rrn;
    }

    public void setRrn(String rrn) {
        this.rrn = rrn;
    }

    public String getTxnDate() {
        return txnDate;
    }

    public void setTxnDate(String txnDate) {
        this.txnDate = txnDate;
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

    public String getObatchNo() {
        return obatchNo;
    }

    public void setObatchNo(String obatchNo) {
        this.obatchNo = obatchNo;
    }

    @Override
    public String toString() {
        return "{" + "version='" + version + '\'' + ", mid='" + mid + '\'' +
               ", tid='" + tid + '\'' + ", systrace='" + systrace + '\'' + ", txnId='" + txnId +
               '\'' + ", osystrace='" + osystrace + '\'' + ", txnAmt=" + txnAmt +
               ", currencyCode='" + currencyCode + '\'' + ", rc='" + rc + '\'' + ", rcDetail='" +
               rcDetail + '\'' + ", txnTime='" + txnTime + '\'' + ", orderStatus=" + orderStatus +
               ", orderId='" + orderId + '\'' + ", rrn='" + rrn + '\'' + ", txnDate='" + txnDate +
               '\'' + ", note='" + note + '\'' + ", batchNo='" + batchNo + '\'' + ", obatchNo='" +
               obatchNo + '\'' + ", payType=" + payType + '}';
    }
}
