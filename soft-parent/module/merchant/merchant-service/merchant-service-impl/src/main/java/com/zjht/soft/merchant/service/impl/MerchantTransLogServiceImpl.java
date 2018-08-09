package com.zjht.soft.merchant.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.zjht.soft.merchant.dao.MerchantTransLogDao;
import com.zjht.soft.merchant.dao.MerchantTransLogWriteDao;
import com.zjht.soft.merchant.entity.MerchantTransLog;
import com.zjht.soft.merchant.service.MerchantTransLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by wuqiyang on 2017/9/18。
 */
@Service(version = "1.0.0")
@Transactional
public class MerchantTransLogServiceImpl implements MerchantTransLogService {
    /**
     * 日志输出。
     */
    private final Logger logger = LoggerFactory.getLogger(MerchantTransLogServiceImpl.class);
    @Reference(version = "1.0.0")
    private MerchantTransLogWriteDao merchantOrderLogWriteDao;

    @Reference(version = "1.0.0")
    private MerchantTransLogDao merchantOrderLogDao;

    @Override
    public MerchantTransLog create(
        MerchantTransLog merchantOrderLog) {
        MerchantTransLog result;
        try {
            result = merchantOrderLogWriteDao.create(merchantOrderLog);
        } catch (Exception ex) {
            logger.error("MerchantTransLogServiceImpl.create exception:{}", ex);
            return null;
        }
        return result;
    }

    @Override
    public boolean update(MerchantTransLog merchantOrderLog) {
        boolean result;
        try {
            result = merchantOrderLogWriteDao.update(merchantOrderLog);
        } catch (Exception ex) {
            logger.error("MerchantTransLogServiceImpl.update exceptaion:{}", ex);
            return false;
        }

        return result;
    }

    @Override
    public MerchantTransLog findById(Long id) {
        MerchantTransLog result;
        try {
            result = merchantOrderLogDao.findById(id);
        } catch (Exception ex) {
            logger.error("MerchantTransLogServiceImpl.findById exception: {}", ex);
            return null;
        }
        return result;
    }

    @Override
    public List<MerchantTransLog> findByOrderId(String orderId) {
        try {
            return merchantOrderLogDao.findByOrderId(orderId);
        } catch (Exception ex) {
            logger.error("MerchantTransLogServiceImpl findByOrderId exception {}", ex);
            return null;
        }

    }

    @Override
    public List<MerchantTransLog> findAll() {
        try {
            return merchantOrderLogDao.findAll();
        } catch (Exception ex) {
            logger.error("MerchantTransLogServiceImpl findAll exception {}", ex);
            return null;
        }
    }
}
