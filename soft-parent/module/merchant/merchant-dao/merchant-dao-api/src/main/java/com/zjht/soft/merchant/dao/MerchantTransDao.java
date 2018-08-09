package com.zjht.soft.merchant.dao;

import com.zjht.soft.merchant.entity.MerchantTrans;

import java.util.Date;
import java.util.List;

/**
 * 订单Dao--读。
 *
 * @author wuqiyang on 2017/09/18.
 */
public interface MerchantTransDao {
    MerchantTrans findByPrimaryKey(Long id);

    /**
     * 查询商户软pos订单数据。
     *
     * @param orderId 订单编号
     * @return 订单数据
     */
    MerchantTrans findByOrderId(String orderId, String txnId);

    /**
     * 查询出状态未知或用户输入密码的订单（需要轮询的订单集合）。
     *
     * @param secondTime
     * @return
     */
    List<MerchantTrans> findAskOrders(Date secondTime);

    /**
     * 查询出需要撤销的订单集合 订单状态标识为-2。
     *
     * @param beforday
     * @param secondTime
     * @return
     */
    List<MerchantTrans> findRevokeOrders(Date beforday, Date secondTime);

    /**
     * 将支付超时的订单状态由4或6改为-2标记为即将撤单的状态。
     *  @param beforDate 当前系统时间减去超时时间。
     *
     */
    void updatePayTimeOutOrders(Date beforDate);
}
