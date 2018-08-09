package com.zjht.soft.merchant.dao.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.zjht.soft.merchant.dao.MerchantTransLogWriteDao;
import com.zjht.soft.merchant.entity.MerchantTransLog;
import com.zjht.soft.merchant.mapper.MerchantTransLogMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by wuqiyang on 2017/9/19。
 */
@Service(version = "1.0.0")
public class MerchantTransLogDaoImpl implements MerchantTransLogWriteDao {

    private final Logger logger = LoggerFactory.getLogger(MerchantTransLogDaoImpl.class);
    @Autowired
    MerchantTransLogMapper merchantOrderLogMapper;

    @Override
    public MerchantTransLog findById(
        Long id) {
        return merchantOrderLogMapper.findById(id);
    }

    @Override
    public List<MerchantTransLog> findByOrderId(String orderId) {
        return merchantOrderLogMapper.findByOrderId(orderId);
    }

    @Override
    public MerchantTransLog create(MerchantTransLog merchantOrderLog) {
        merchantOrderLogMapper.create(merchantOrderLog);
        return merchantOrderLogMapper.findById(merchantOrderLog.getId());
    }

    @Override
    public boolean update(MerchantTransLog merchantOrderLog) {
        int count = merchantOrderLogMapper.update(merchantOrderLog);
        if (count == 1) {
            return true;
        }
        return false;
    }

    @Override
    public List<MerchantTransLog> findAll() {
        return merchantOrderLogMapper.findAll();
    }
}
