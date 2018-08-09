package com.zjht.soft.merchant.entity;

import com.zjht.solar.ecard.commons.database.dao.Model;

/**
 * @author wuqiyang 。
 * 2017/11/1。
 */
public class PosInfo implements Model<Long> {

    private Long id;

    private String mid;

    private String tid;

    private String batchNo;

    private String systrace;

    //原交易流水,更新流水SQL语句时使用，不用保存到数据库
    private String oldSystrace;

    private String updateTime;

    //默认为1，暂未使用
    private Integer flag = 1;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public String getSystrace() {
        return systrace;
    }

    public void setSystrace(String systrace) {
        this.systrace = systrace;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }

    public String getOldSystrace() {
        return oldSystrace;
    }

    public void setOldSystrace(String oldSystrace) {
        this.oldSystrace = oldSystrace;
    }

    @Override
    public Long getKey() {
        return this.id;
    }
}
