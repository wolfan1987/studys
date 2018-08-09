package com.zjht.soft.merchant.mapper;

import com.zjht.soft.merchant.entity.PayType;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author wuqiyang 。
 * 2017/10/25。
 */
public interface PayTypeMapper {
    PayType findByMidAndTid(@Param("mid") String mid, @Param("tid") String tid);

    List<PayType> findAll();
}
