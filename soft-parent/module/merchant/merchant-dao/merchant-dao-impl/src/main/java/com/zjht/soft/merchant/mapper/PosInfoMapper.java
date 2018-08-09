package com.zjht.soft.merchant.mapper;

import com.zjht.soft.merchant.entity.PosInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author wuqiyang 。
 * 2017/11/1。
 */
public interface PosInfoMapper {
    void create(PosInfo info);

    int update(PosInfo info);

    List<PosInfo> findAll();

    PosInfo findByMidAndTid(@Param("mid") String mid,@Param("tid") String tid);
}
