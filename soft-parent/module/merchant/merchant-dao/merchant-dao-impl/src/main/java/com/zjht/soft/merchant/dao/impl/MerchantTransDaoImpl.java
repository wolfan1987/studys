package com.zjht.soft.merchant.dao.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.zjht.soft.merchant.dao.MerchantTransWriteDao;
import com.zjht.soft.merchant.entity.MerchantTrans;
import com.zjht.soft.merchant.mapper.MerchantTransMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

/**
 * Created by wuqiyang on 2017/9/18。
 */
@Service(version = "1.0.0")
public class MerchantTransDaoImpl implements MerchantTransWriteDao {

    /**
     * 日志输出。
     */
    private final Logger logger = LoggerFactory.getLogger(MerchantTransDaoImpl.class);
    @Autowired
    private MerchantTransMapper merchantOrderMapper;

    @Override
    public MerchantTrans findByPrimaryKey(Long id) {
        return merchantOrderMapper.findById(id);
    }

    /**
     * 查询商户软pos订单数据。
     *
     * @param orderId 订单编号
     * @return 订单数据
     */
    @Override
    public MerchantTrans findByOrderId(String orderId, String txnId) {
        return merchantOrderMapper.findByOrderId(orderId, txnId);
    }

    @Override
    public MerchantTrans create(MerchantTrans merchantOrder) {
        merchantOrderMapper.create(merchantOrder);
        return merchantOrderMapper.findById(merchantOrder.getId());
    }

    @Override
    public boolean update(MerchantTrans merchantOrder) {
        boolean result = false;
        int count = merchantOrderMapper.update(merchantOrder);
        if (count == 1) {
            result = true;
        }
        return result;
    }

    /**
     * 查询出状态未知或用户输入密码的订单（需要轮询的订单集合）。
     *
     * @param secondTime
     * @return
     */
    @Override
    public List<MerchantTrans> findAskOrders(Date secondTime) {
        return merchantOrderMapper.findAskOrders(secondTime, new Date());
    }

    /**
     * 查询出需要撤销的订单集合 订单状态标识为-2。
     *
     * @param beforday
     * @param secondTime
     * @return
     */
    @Override
    public List<MerchantTrans> findRevokeOrders(Date beforday, Date secondTime) {
        return merchantOrderMapper.findRevokeOrders(beforday, secondTime);
    }


    /**
     * 将支付超时的订单状态由4或6改为-2标记为即将撤单的状态。
     *  @param beforDate 当前系统时间减去超时时间。
     *
     */
    @Override
    public void updatePayTimeOutOrders(Date beforDate) {
        merchantOrderMapper.updatePayTimeOutOrders(beforDate);
    }
}
