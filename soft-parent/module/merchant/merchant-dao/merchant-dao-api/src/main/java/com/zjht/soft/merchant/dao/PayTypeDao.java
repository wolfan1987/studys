package com.zjht.soft.merchant.dao;

import com.zjht.soft.merchant.entity.PayType;

import java.util.List;

/**
 * @author wuqiyang 。
 * 2017/10/25。
 */
public interface PayTypeDao {

    PayType findByMidAndTid(String mid, String tid);

    List<PayType> findAll();
}
