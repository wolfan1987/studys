package com.zjht.cardsys.orgquery.model;

import java.util.Date;

/**
 * Created by zjhtadmin on 2018/1/8.
 */
public class AcceptOrgRelation   {

    /**
     * 主键
     */
    private Long id;
    /**
     * 卡号
     */
    private  String  pan;
    /**
     * 发卡机构ID
     */
    private  String  issuId;
    /**
     * 映射关系创建时间
     */
    private Date createTime;

    /**
     * 主键
     */
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 卡号
     */
    public String getPan() {
        return pan;
    }

    public void setPan(String pan) {
        this.pan = pan;
    }

    /**
     * 发卡机构ID
     */
    public String getIssuId() {
        return issuId;
    }

    public void setIssuId(String issuId) {
        this.issuId = issuId;
    }

    /**
     * 创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "AcceptOrgRelation{" +
                "id=" + getId() +
                ", pan='" + getPan() + '\'' +
                ", issuId='" + getIssuId() + '\'' +
                ", createTime=" + getCreateTime() +
                '}';
    }
}
