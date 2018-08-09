package com.zjht.soft.bluelotus.socket.entity;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;

/**
 * 交易查询实体。
 * Created by yuanyaping on 2017/9/20.
 */
public class TransactionQueryReq implements Serializable {
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
     * 订单号。
     */
    @JSONField(name = "order_id")
    private String orderId;

    /**
     * 交易流水号。
     */
    @JSONField(name = "systrace")
    private String systrace;
    /**
     * 交易类型。
     * 默认值：841100
     */
    @JSONField(name = "txn_id")
    private String txnId;

    /**
     * 终端号。
     */
    @JSONField(name = "tid")
    private String tid;

    /**
     * 批次号。
     */
    @JSONField(name = "batch_no")
    private String batchNo;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = (version == null) ? null : version.trim();
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = (mid == null) ? null : mid.trim();
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = (orderId == null) ? null : orderId.trim();
    }

    public String getSystrace() {
        return systrace;
    }

    public void setSystrace(String systrace) {
        this.systrace = (systrace == null) ? null : systrace.trim();
    }

    public String getTxnId() {
        return txnId;
    }

    public void setTxnId(String txnId) {
        this.txnId = (txnId == null) ? null : txnId.trim();
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = (tid == null) ? null : tid.trim();
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = (batchNo == null) ? null : batchNo.trim();
    }

    @Override
    public String toString() {
        return "{" + "version='" + version + '\'' + ", mid='" + mid + '\'' + ", orderId='" +
               orderId + '\'' + ", systrace='" + systrace + '\'' + ", txnId='" + txnId + '\'' +
               ", tid='" + tid + '\'' + ", batchNo='" + batchNo + '\'' + '}';
    }
}
