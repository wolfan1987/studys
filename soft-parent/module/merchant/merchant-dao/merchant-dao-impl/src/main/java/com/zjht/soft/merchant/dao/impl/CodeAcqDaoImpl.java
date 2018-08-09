package com.zjht.soft.merchant.dao.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.zjht.soft.merchant.dao.CodeAcqWriteDao;
import com.zjht.soft.merchant.entity.CodeAcq;
import com.zjht.soft.merchant.mapper.CodeAcqMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author wuqiyang 。
 * 2017/10/24。
 */
@Service(version = "1.0.0")
public class CodeAcqDaoImpl implements CodeAcqWriteDao {
    private final Logger logger = LoggerFactory.getLogger(CodeAcqDaoImpl.class);

    @Autowired
    private CodeAcqMapper codeAcqMapper;

    @Override
    public void create(CodeAcq codeAcq) {
        logger.debug(".调用...CodeAcqDaoImpl.create...");
        codeAcqMapper.create(codeAcq);
    }

    @Override
    public boolean update(CodeAcq codeAcq) {
        logger.debug(".调用...CodeAcqDaoImpl.update...");
        return codeAcqMapper.update(codeAcq) == 1;
    }

    @Override
    public CodeAcq findByHeadCode(String headCode) {
        logger.debug(".调用...CodeAcqDaoImpl.findByHeadCode...");
        return codeAcqMapper.findByHeadCode(headCode);
    }

    @Override
    public List<CodeAcq> findAll() {
        logger.debug(".调用...CodeAcqDaoImpl.findAll...");
        return codeAcqMapper.findAll();
    }
}
