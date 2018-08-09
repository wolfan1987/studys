package com.zjht.soft.merchant.entity;

import com.zjht.solar.ecard.commons.database.dao.Model;

/**
 * 商户支付方式开关实体。
 *
 * @author wuqiyang 。
 * 2017/10/25。
 */
public class PayType implements Model<Long> {

    private Long id;

    private String mid;

    private String tid;

    private int payCode;

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

    public int getPayCode() {
        return payCode;
    }

    public void setPayCode(int payCode) {
        this.payCode = payCode;
    }

    @Override
    public Long getKey() {
        return null;
    }
}
