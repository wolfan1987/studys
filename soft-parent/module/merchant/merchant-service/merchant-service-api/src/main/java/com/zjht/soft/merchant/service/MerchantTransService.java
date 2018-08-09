package com.zjht.soft.merchant.service;

import com.zjht.soft.merchant.entity.MerchantTrans;
import com.zjht.soft.merchant.entity.MerchantTransLog;
import com.zjht.soft.merchant.entity.PosInfo;

import java.util.Date;
import java.util.List;

/**
 * Created by wuqiyang on 2017/9/18。
 */
public interface MerchantTransService {

    MerchantTrans create(MerchantTrans merchantOrder);

    boolean update(MerchantTrans merchantOrder);

    MerchantTrans findById(Long id);

    /**
     * 根据订单号跟业务代码查询。
     *
     * @param orderId 订单号
     * @param txnId   业务代码
     * @return
     */
    MerchantTrans findByOrderId(String orderId, String txnId);

    /**
     * 查询出状态未知或用户输入密码的订单（需要轮询的订单集合）。
     *
     * @param mis
     * @return
     */
    List<MerchantTrans> findAskOrders(int mis);

    /**
     * 查询出需要撤销的订单集合 订单状态标识为-2。
     *
     * @param day 默认为自动撤单1天之内的订单。
     * @param mis 超时秒值
     * @return
     */
    List<MerchantTrans> findRevokeOrders(int day, int mis);

    /**
     * 将支付超时的订单状态由4或6改为-2标记为即将撤单的状态。
     *  @param beforDate 当前系统时间减去超时时间。
     *
     */
    void updatePayTimeOutOrders(Date beforDate);

    /**
     * 得到撤单时用的posInfo，主要是要得到最新的systrace
     * @param mid
     * @param tid
     * @return
     */
    PosInfo   getNextPosInfo(String  mid,String tid);
}
