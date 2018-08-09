package com.zjht.soft.bluelotus.socket.entity;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;

/**
 * Created by wuqiyang on 2017/9/20。
 * 扫码付返回实体。
 */

public class PayOrderRes implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 交易返回码。
     * 00 ：表示成功
     */
    @JSONField(name = "rc")
    private String rc;

    /**
     * 返回码信息。
     */
    @JSONField(name = "rc_detail")
    private String rcDetail;

    /**
     * 交易流水。
     * 格式：HHmmss
     */
    @JSONField(name = "systrace")
    private String sysTrace;

    /**
     * 交易金额。
     */
    @JSONField(name = "txn_amt")
    private int txnAmt;

    /**
     * 货币类型。
     */
    @JSONField(name = "currency_code")
    private String currencyCode;

    /**
     * 交易时间,hhmmss。
     */
    @JSONField(name = "txn_time")
    private String txnTime;

    /**
     * 交易日期,yyyyMMdd。
     */
    @JSONField(name = "txn_date")
    private String txnDate;

    /**
     * 交易状态。
     * 0  上送
     * 1  支付成功
     * 2  订单被撤销(或被冲正)
     * 3  支付失败
     * 4  支付状态未明
     * 5 订单被关闭
     * 6  用户输入密码
     * 7  订单未支付
     * 8  退款中
     * 9  订单被冲正
     * 10 订单本地超时作废
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
     * 交易码。
     */
    @JSONField(name = "txn_id")
    private String txnId;


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
     * 机构订单号，部分交易返回。
     */
    @JSONField(name = "acq_order")
    private String acqOrder;
    /**
     * 机构附加信息。
     */
    @JSONField(name = "acq_info")
    private String acqInfo;

    /**
     * 机构交易时间。
     */
    @JSONField(name = "acq_time")
    private String acqTime;
    /**
     * 优惠金额。
     */
    @JSONField(name = "fav_amt")
    private int    favAmt;
    /**
     * 实扣金额(分)。
     */
    @JSONField(name = "pay_amt")
    private int    paymt;
    /**
     * 批次号。
     */
    @JSONField(name = "batch_no")
    private String batchNo;

    /**
     * 备注。
     */
    @JSONField(name = "note")
    private String note;

    /**
     * 支付方式类型。
     * <p>
     * 0=银联刷卡1=银联扫码，2=汇通卡，3=汇通卡电子现金卷，
     * 4=翼支付扫码(暂不对接)，5=微信支付,6=支付宝(暂不对接)，
     * 7=QQ钱包(暂不对接),8=外币卡(暂不对接),9=中经扫码。
     * </p>
     */
    @JSONField(name = "pay_type")
    private Integer payType;

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

    public String getSysTrace() {
        return sysTrace;
    }

    public void setSysTrace(String sysTrace) {
        this.sysTrace = sysTrace;
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

    public String getTxnTime() {
        return txnTime;
    }

    public void setTxnTime(String txnTime) {
        this.txnTime = txnTime;
    }

    public String getTxnDate() {
        return txnDate;
    }

    public void setTxnDate(String txnDate) {
        this.txnDate = txnDate;
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

    public String getTxnId() {
        return txnId;
    }

    public void setTxnId(String txnId) {
        this.txnId = txnId;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Integer getPayType() {
        return payType;
    }

    public void setPayType(Integer payType) {
        this.payType = payType;
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

    public String getAcqTime() {
        return acqTime;
    }

    public void setAcqTime(String acqTime) {
        this.acqTime = acqTime;
    }

    public String getAcqOrder() {
        return acqOrder;
    }

    public void setAcqOrder(String acqOrder) {
        this.acqOrder = acqOrder;
    }

    public String getAcqInfo() {
        return acqInfo;
    }

    public void setAcqInfo(String acqInfo) {
        this.acqInfo = acqInfo;
    }

    public int getFavAmt() {
        return favAmt;
    }

    public void setFavAmt(int favAmt) {
        this.favAmt = favAmt;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public int getPaymt() {
        return paymt;
    }

    public void setPaymt(int paymt) {
        this.paymt = paymt;
    }

    @Override
    public String toString() {
        return "PayOrderRes{" + "rc='" + rc + '\'' + ", rcDetail='" + rcDetail + '\'' +
               ", sysTrace='" + sysTrace + '\'' + ", txnAmt=" + txnAmt + ", currencyCode='" +
               currencyCode + '\'' + ", txnTime='" + txnTime + '\'' + ", txnDate='" + txnDate +
               '\'' + ", orderStatus=" + orderStatus + ", orderId='" + orderId + '\'' + ", rrn='" +
               rrn + '\'' + ", txnId='" + txnId + '\'' + ", mid='" + mid + '\'' + ", tid='" + tid +
               '\'' + ", acqOrder='" + acqOrder + '\'' + ", acqInfo='" + acqInfo + '\'' +
               ", acqTime='" + acqTime + '\'' + ", favAmt=" + favAmt + ", paymt=" + paymt +
               ", batchNo='" + batchNo + '\'' + ", note='" + note + '\'' + ", payType=" + payType +
               '}';
    }

    public String toJsonString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("{");
        stringBuffer.append("\"rc\":").append("\"").append(rc).append("\"");
        stringBuffer.append(",\"rc_detail\":").append("\"").append(rcDetail).append("\"");
        stringBuffer.append(",\"order_id\":").append("\"").append(orderId).append("\"");
        stringBuffer.append(",\"mid\":").append("\"").append(mid).append("\"");
        stringBuffer.append(",\"tid\":").append("\"").append(tid).append("\"");
        stringBuffer.append(",\"txn_amt\":").append("\"").append(txnAmt).append("\"");
        stringBuffer.append(",\"currency_code\":").append("\"").append(currencyCode).append("\"");
        stringBuffer.append(",\"systrace\":").append("\"").append(sysTrace).append("\"");
        stringBuffer.append(",\"batch_no\":").append("\"").append(batchNo).append("\"");
        stringBuffer.append(",\"order_status\":").append("\"").append(orderStatus).append("\"");
        stringBuffer.append(",\"txn_id\":").append("\"").append(txnId).append("\"");
        stringBuffer.append(",\"fav_amt\":").append("\"").append(favAmt).append("\"");
        stringBuffer.append(",\"pay_amt\":").append("\"").append(paymt).append("\"");

        if (txnTime != null) {
            stringBuffer.append(",\"txn_time\":").append("\"").append(txnTime).append("\"");
        }
        if (txnDate != null) {
            stringBuffer.append(",\"txn_date\":").append("\"").append(txnDate).append("\"");
        }
        if (payType != null) {
            stringBuffer.append(",\"pay_type\":").append("\"").append(payType).append("\"");
        }

        if (note != null) {
            stringBuffer.append(",\"note\":").append("\"").append(note).append("\"");
        }
        if (rrn != null) {
            stringBuffer.append(",\"rrn\":").append("\"").append(rrn).append("\"");
        }
        if ("00".equals(rc)) {
            stringBuffer.append(",\"acq_order\":").append("\"").append(acqOrder).append("\"");
            stringBuffer.append(",\"acq_info\":").append("\"").append(acqInfo).append("\"");
            stringBuffer.append(",\"acq_time\":").append("\"").append(acqTime).append("\"");

        }
        stringBuffer.append("}");
        return stringBuffer.toString();
    }
}
