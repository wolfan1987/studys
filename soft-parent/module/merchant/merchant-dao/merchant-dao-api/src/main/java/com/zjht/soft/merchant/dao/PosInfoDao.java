package com.zjht.soft.merchant.dao;

import com.zjht.soft.merchant.entity.PosInfo;

import java.util.List;

/**
 * @author wuqiyang 。
 * 2017/11/1。
 * pos机信息dao
 */
public interface PosInfoDao {
    void save(PosInfo info);

    int update(PosInfo info);

    PosInfo findByMidAndTid(String mid, String tid);

    List<PosInfo> findAll();
}
