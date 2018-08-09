package com.zjht.soft.merchant.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.zjht.soft.merchant.dao.CodeAcqWriteDao;
import com.zjht.soft.merchant.entity.CodeAcq;
import com.zjht.soft.merchant.service.CodeAcqService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author wuqiyang 。
 * 2017/10/24。
 */
@Service(version = "1.0.0")
public class CodeAcqServiceImpl implements CodeAcqService {

    /**
     * 日志输出。
     */
    private final Logger logger = LoggerFactory.getLogger(CodeAcqServiceImpl.class);
    @Reference(version = "1.0.0")
    private CodeAcqWriteDao codeAcqDao;

    @Override
    public void create(CodeAcq codeAcq) {
        logger.debug(".调用.....CodeAcqServiceImpl.create");
        codeAcqDao.create(codeAcq);
    }

    @Override
    public boolean update(CodeAcq codeAcq) {
        logger.debug(".调用.....CodeAcqServiceImpl.update");
        return codeAcqDao.update(codeAcq);
    }

    @Override
    public CodeAcq findByHeadCode(String headCode) {
        logger.debug(".调用.....CodeAcqServiceImpl.findByHeadCode");
        return codeAcqDao.findByHeadCode(headCode);
    }

    @Override
    public List<CodeAcq> findAll() {
        logger.debug(".调用.....CodeAcqServiceImpl.findAll");
        return codeAcqDao.findAll();
    }
}
