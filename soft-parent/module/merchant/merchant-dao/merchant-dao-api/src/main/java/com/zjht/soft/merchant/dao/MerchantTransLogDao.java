package com.zjht.soft.merchant.dao;

import com.zjht.soft.merchant.entity.MerchantTransLog;

import java.util.List;

/**
 * Created by wuqiyang on 2017/9/19。
 */
public interface MerchantTransLogDao {

    MerchantTransLog findById(Long id);

    List<MerchantTransLog> findByOrderId(String orderId);

    List<MerchantTransLog> findAll();
}
