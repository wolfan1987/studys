package com.zjht.soft.merchant.dao;

import com.zjht.soft.merchant.entity.MerchantTransLog;

/**
 * Created by wuqiyang on 2017/9/19。
 */
public interface MerchantTransLogWriteDao extends MerchantTransLogDao {
    MerchantTransLog create(MerchantTransLog merchantOrderLog);

    boolean update(MerchantTransLog merchantOrderLog);
}
