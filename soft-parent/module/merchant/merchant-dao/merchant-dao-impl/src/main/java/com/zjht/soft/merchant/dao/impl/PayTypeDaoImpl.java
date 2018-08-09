package com.zjht.soft.merchant.dao.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.zjht.soft.merchant.dao.PayTypeDao;
import com.zjht.soft.merchant.entity.PayType;
import com.zjht.soft.merchant.mapper.PayTypeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author wuqiyang 。
 * 2017/10/25。
 */
@Service(version = "1.0.0")
public class PayTypeDaoImpl implements PayTypeDao {
    private final Logger logger = LoggerFactory.getLogger(PayTypeDaoImpl.class);

    @Autowired
    private PayTypeMapper payTypeMapper;

    @Override
    public PayType findByMidAndTid(String mid, String tid) {
        logger.debug("。。。。根据商户号终端号查询支持的支付方式。。。。");
        return payTypeMapper.findByMidAndTid(mid, tid);
    }

    @Override
    public List<PayType> findAll() {
        return payTypeMapper.findAll();
    }
}
