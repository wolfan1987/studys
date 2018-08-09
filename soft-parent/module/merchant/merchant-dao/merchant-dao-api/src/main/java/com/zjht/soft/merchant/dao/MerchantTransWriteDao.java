package com.zjht.soft.merchant.dao;

import com.zjht.soft.merchant.entity.MerchantTrans;

/**
 * 商户软pos订单数据Dao--写。
 *
 * @author wuqiyang on 2017/09/18。
 */
public interface MerchantTransWriteDao extends MerchantTransDao {

    MerchantTrans create(MerchantTrans merchantOrder);

    boolean update(MerchantTrans merchantOrder);
}
