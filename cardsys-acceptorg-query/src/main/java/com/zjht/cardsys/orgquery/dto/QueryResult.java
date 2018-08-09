package com.zjht.cardsys.orgquery.dto;

/**
 * Created by zjhtadmin on 2018/1/9.
 */
public class QueryResult {

    private Long id;

    private  String  pan;

    private  String  issuId;

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
     * 受理机构ID
     */
    public String getIssuId() {
        return issuId;
    }

    public void setIssuId(String issuId) {
        this.issuId = issuId;
    }

}
