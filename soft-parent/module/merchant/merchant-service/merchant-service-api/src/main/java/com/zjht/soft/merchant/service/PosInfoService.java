package com.zjht.soft.merchant.service;

import com.zjht.soft.merchant.entity.PosInfo;

import java.util.List;

/**
 * @author wuqiyang 。
 * 2017/11/1。
 */
public interface PosInfoService {

    void save(PosInfo info);

    void update(PosInfo info);

    PosInfo findByMidAndTid(String mid, String tid);

    List<PosInfo> findAll();

    /**
     * 通过商户号、终端号获取最新的批次号。
     *
     * @param mid 商户号
     * @param tid 终端号
     * @return 批次号
     */
    String getBatchNo(String mid, String tid);

    /**
     * 通过商户号、终端号获取最新的流水号。
     *
     * @param mid 商户号
     * @param tid 终端号
     * @return 流水号
     */
    String getSystrace(String mid, String tid);

    /**
     * 通过商户号终端号获取此次交易的批次号流水号。
     *
     * @param mid 商户号
     * @param tid 终端号
     * @return result
     */
    String[] getBatchNoAndSystrace(String mid, String tid);


}
