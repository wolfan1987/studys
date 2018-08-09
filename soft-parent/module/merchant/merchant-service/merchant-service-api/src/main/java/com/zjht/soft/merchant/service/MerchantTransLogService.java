package com.zjht.soft.merchant.service;

import com.zjht.soft.merchant.entity.MerchantTransLog;

import java.util.List;

/**
 * Created by wuqiyang on 2017/9/18。
 */
public interface MerchantTransLogService {

    MerchantTransLog create(MerchantTransLog merchantOrderLog);

    boolean update(MerchantTransLog merchantOrderLog);

    MerchantTransLog findById(Long id);

    List<MerchantTransLog> findByOrderId(String orderId);

    List<MerchantTransLog> findAll();

}
