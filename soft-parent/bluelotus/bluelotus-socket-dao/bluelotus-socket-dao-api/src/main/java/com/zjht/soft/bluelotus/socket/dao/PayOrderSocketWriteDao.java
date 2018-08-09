package com.zjht.soft.bluelotus.socket.dao;

import com.zjht.soft.bluelotus.socket.entity.PayOrderReq;
import com.zjht.soft.bluelotus.socket.entity.PayOrderRes;

/**
 * Created by wuqiyang on 2017/9/20。
 * 商户订单相关posp调用。
 */
public interface PayOrderSocketWriteDao extends PayOrderSocketDao {

    /**
     * 扫码支付接口。
     *
     * @param payOrderReq 请求参数
     * @return posp返回数据
     */
    PayOrderRes payOrder(PayOrderReq payOrderReq) throws Exception;
}
