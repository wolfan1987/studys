package com.zjht.soft.merchant.entity;

import com.zjht.solar.ecard.commons.database.dao.Model;

import java.util.Date;

/**
 * Created by wuqiyang on 2017/10/24。
 */
public class CodeAcq implements Model<String> {
    /**
     * 主键
     * 机构对应的码头数据类型。
     */
    private String  headCode;
    /**
     * 码头长度。
     */
    private Integer headLen;
    /**
     * 编号，（编号数字应按顺序，不能中间跳过数字）。
     */
    private Integer num;
    /**
     * 二维码长度。
     */
    private Integer codeLen;

    /**
     * 支付方式。
     */
    private Integer payType;
    /**
     * 生效标识。
     */
    private Integer flag;

    /**
     * 支付开头码二进制。
     */
    private int payFlag;
    /**
     * 机构号。
     */
    private String  acqId;
    /**
     * 备注。
     */
    private String  memo;
    /**
     * 操作员。
     */
    private String  teller;
    /**
     * 更新时间。
     */
    private Date    modiate;

    @Override
    public String getKey() {
        return this.headCode;
    }

    public String getHeadCode() {
        return headCode;
    }

    public void setHeadCode(String headCode) {
        this.headCode = headCode;
    }

    public Integer getHeadLen() {
        return headLen;
    }

    public void setHeadLen(Integer headLen) {
        this.headLen = headLen;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public Integer getCodeLen() {
        return codeLen;
    }

    public void setCodeLen(Integer codeLen) {
        this.codeLen = codeLen;
    }

    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }

    public String getAcqId() {
        return acqId;
    }

    public void setAcqId(String acqId) {
        this.acqId = acqId;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getTeller() {
        return teller;
    }

    public void setTeller(String teller) {
        this.teller = teller;
    }

    public Date getModiate() {
        return modiate;
    }

    public void setModiate(Date modiate) {
        this.modiate = modiate;
    }

    public Integer getPayType() {
        return payType;
    }

    public void setPayType(Integer payType) {
        this.payType = payType;
    }

    public int getPayFlag() {
        return payFlag;
    }

    public void setPayFlag(int payFlag) {
        this.payFlag = payFlag;
    }
}
