package com.zjht.soft.merchant.mapper;

import com.zjht.soft.merchant.entity.MerchantTrans;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 商户软pos订单Mapper。
 *
 * @author wuqiyang。
 */
public interface MerchantTransMapper {

    MerchantTrans findByOrderId(
        @Param("orderId") String orderId, @Param("txnId") String txnId);

    MerchantTrans findById(@Param("id") Long id);

    void create(MerchantTrans merchantOrder);

    int update(MerchantTrans merchantOrder);

    List<MerchantTrans> findAskOrders(
        @Param("secondTime") Date secondTime, @Param("sysDate") Date sysDate);

    List<MerchantTrans> findRevokeOrders(
        @Param("beforday") Date beforday, @Param("secondTime") Date secondTime);

    void updatePayTimeOutOrders(@Param("beforDate") Date beforDate);
}
