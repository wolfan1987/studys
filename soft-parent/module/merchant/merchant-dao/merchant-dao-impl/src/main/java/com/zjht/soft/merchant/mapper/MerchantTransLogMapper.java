package com.zjht.soft.merchant.mapper;

import com.zjht.soft.merchant.entity.MerchantTransLog;

import java.util.List;

/**
 * Created by wuqiyang on 2017/9/19。
 */
public interface MerchantTransLogMapper {

    MerchantTransLog findById(Long id);

    List<MerchantTransLog> findByOrderId(String orderId);

    void create(MerchantTransLog merchantOrderLog);

    int update(MerchantTransLog merchantOrderLog);

    List<MerchantTransLog> findAll();
}
