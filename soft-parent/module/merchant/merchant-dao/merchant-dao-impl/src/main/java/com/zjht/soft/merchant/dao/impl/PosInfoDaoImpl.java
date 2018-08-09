package com.zjht.soft.merchant.dao.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.zjht.soft.merchant.dao.PosInfoDao;
import com.zjht.soft.merchant.entity.PosInfo;
import com.zjht.soft.merchant.mapper.PosInfoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author wuqiyang 。
 * 2017/11/1。
 */
@Service(version = "1.0.0")
public class PosInfoDaoImpl implements PosInfoDao {

    private final Logger logger = LoggerFactory.getLogger(PosInfoDaoImpl.class);
    @Autowired
    private PosInfoMapper posInfoMapper;

    @Override
    public void save(PosInfo info) {
        logger.debug("run PosInfoDaoImpl.save() ...");
        posInfoMapper.create(info);
    }

    @Override
    public int update(PosInfo info) {
        logger.debug("run PosInfoDaoImpl.update() ...");
        return posInfoMapper.update(info);
    }

    @Override
    public PosInfo findByMidAndTid(String mid, String tid) {
        logger.debug("run PosInfoDaoImpl.findByTid() ...");
        return posInfoMapper.findByMidAndTid(mid, tid);
    }

    @Override
    public List<PosInfo> findAll() {
        logger.debug("run PosInfoDaoImpl.findAll() ...");
        return posInfoMapper.findAll();
    }
}
