package com.zjht.soft.merchant.service.impl;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.zjht.soft.merchant.dao.PosInfoDao;
import com.zjht.soft.merchant.entity.PosInfo;
import com.zjht.soft.merchant.service.PosInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author wuqiyang 。
 * 2017/11/1
 */
@Service(version = "1.0.0")
public class PosInfoServiceImpl implements PosInfoService {
    /**
     * 日志输出。
     */
    private final Logger logger = LoggerFactory.getLogger(PosInfoServiceImpl.class);

    private static final SimpleDateFormat DATE8 = new SimpleDateFormat("yyyyMMdd");

    private static final String INIT_BATCH_NO = "000000";

    private static final String INIT_SYSTRACE = "A00000";

    private static final String MAX_BATCH_NO = "999999";

    private static final String MAX_SYSTRACE = "Z99999";


    @Reference(version = "1.0.0")
    private PosInfoDao posInfoDao;


    @Override
    @Transactional
    public String[] getBatchNoAndSystrace(String mid, String tid) {
        Assert.notNull(tid, "终端号不能为null");
        Assert.notNull(mid, "商户号不能为null");
        String[] result = new String[2];
        /*PosInfo info = new PosInfo();
        info.setTid(tid);
        info.setMid(mid);
        String newBatchNo = "";
        String newSystrace = "";

        PosInfo posInfo = posInfoDao.findByMidAndTid(mid, tid);
        if (posInfo == null) {
            info.setBatchNo(INIT_BATCH_NO);
            info.setSystrace(INIT_SYSTRACE);
            info.setUpdateTime(DATE8.format(new Date()));
            posInfoDao.save(info);
            newBatchNo = INIT_BATCH_NO;
            newSystrace = INIT_SYSTRACE;
        } else {
            //取批次号 如果更新日期等于当天则直接返回批次号
            String oldBatchNo = posInfo.getBatchNo();
            if (DATE8.format(new Date()).equals(posInfo.getUpdateTime())) {
                newBatchNo = oldBatchNo;
            }
            //若是不等则返回批次号+1
            else {
                //如果是999999则返回000001否则加1
                if (MAX_BATCH_NO.equals(oldBatchNo)) {
                    newBatchNo = INIT_BATCH_NO;
                } else {
                    newBatchNo = String.format("%06d", Integer.parseInt(oldBatchNo) + 1);
                    posInfo.setBatchNo(newBatchNo);
                    posInfo.setUpdateTime(DATE8.format(new Date()));
                    //                    posInfoDao.update(posInfo);
                }
            }

            //取流水号
            String oldSystrace = posInfo.getSystrace();
            if (StringUtils.isEmpty(oldSystrace) || MAX_SYSTRACE.equals(oldSystrace)) {
                posInfo.setSystrace(INIT_SYSTRACE);
                //                posInfoDao.update(posInfo);
                newSystrace = INIT_SYSTRACE;
            } else {
                if (oldSystrace.endsWith("99999")) {
                    char index = oldSystrace.charAt(0);
                    int newIndex = (int) index + 1;
                    newSystrace = String.format((char) newIndex + "%05d", 1);
                    posInfo.setSystrace(newSystrace);
                    //                    posInfoDao.update(posInfo);
                } else {
                    String num = oldSystrace.substring(1);
                    newSystrace = String.format(oldSystrace.charAt(0) + "%05d",
                                                Integer.parseInt(num) + 1);

                    posInfo.setSystrace(newSystrace);
                    //                    posInfoDao.update(posInfo);
                }
            }
            posInfoDao.update(posInfo);
        }
        result[0] = newBatchNo;
        result[1] = newSystrace;*/
        return result;
    }

    /**
     * 通过终端号获取最新的批次号。
     *
     * @param mid 商户号
     * @param tid 终端号
     * @return 最新的批次号
     */
    @Override
    public synchronized String getBatchNo(String mid, String tid) {
        Assert.notNull(tid, "终端号不能为null");
        Assert.notNull(mid, "商户号不能为null");
        PosInfo info = new PosInfo();
        info.setTid(tid);
        info.setMid(mid);
        PosInfo posInfo = posInfoDao.findByMidAndTid(mid, tid);
        if (posInfo == null) {
            info.setBatchNo(INIT_BATCH_NO);
            info.setUpdateTime(DATE8.format(new Date()));
            posInfoDao.save(info);
            return INIT_BATCH_NO;
        } else {
            //如果更新日期等于当天则直接返回批次号
            String oldBatchNo = posInfo.getBatchNo();
            if (DATE8.format(new Date()).equals(posInfo.getUpdateTime())) {
                return oldBatchNo;
            }
            //若是不等则返回批次号+1
            else {
                //如果是999999则返回000001否则加1
                if (MAX_BATCH_NO.equals(oldBatchNo)) {
                    return INIT_BATCH_NO;
                } else {
                    String newBatchNo = String.format("%06d", Integer.parseInt(oldBatchNo) + 1);
                    posInfo.setBatchNo(newBatchNo);
                    posInfo.setUpdateTime(DATE8.format(new Date()));
                    posInfoDao.update(posInfo);
                    return newBatchNo;
                }
            }
        }
    }

    /**
     * 通过终端号获取最新的流水号。
     *
     * @param mid 商户号
     * @param tid 终端号
     * @return 最新的流水号
     */
    @Override
    public synchronized String getSystrace(String mid, String tid) {
        Assert.notNull(tid, "终端号不能为null");
        Assert.notNull(mid, "商户号不能为null");
        PosInfo info = new PosInfo();
        info.setTid(tid);
        info.setMid(mid);
        PosInfo posInfo = posInfoDao.findByMidAndTid(mid, tid);
        if (posInfo == null) {
            info.setBatchNo(INIT_BATCH_NO);
            info.setSystrace(INIT_SYSTRACE);
            info.setUpdateTime(DATE8.format(new Date()));
            posInfoDao.save(info);
            return INIT_SYSTRACE;
        } else {
            String oldSystrace = posInfo.getSystrace();
            if (StringUtils.isEmpty(oldSystrace) || MAX_SYSTRACE.equals(oldSystrace)) {
                posInfo.setSystrace(INIT_SYSTRACE);
                posInfoDao.update(posInfo);
                return INIT_SYSTRACE;
            } else {
                String newSystrace = "";
                if (oldSystrace.endsWith("99999")) {
                    char index = oldSystrace.charAt(0);
                    int newIndex = (int) index + 1;
                    newSystrace = String.format((char) newIndex + "%05d", 1);
                    posInfo.setSystrace(newSystrace);
                    posInfoDao.update(posInfo);
                    return newSystrace;
                } else {
                    String num = oldSystrace.substring(1);
                    newSystrace = String.format(oldSystrace.charAt(0) + "%05d",
                                                Integer.parseInt(num) + 1);

                    posInfo.setSystrace(newSystrace);
                    posInfoDao.update(posInfo);
                    return newSystrace;
                }
            }
        }
    }

    @Override
    public void save(PosInfo info) {
        posInfoDao.save(info);
    }

    @Override
    public void update(PosInfo info) {
        posInfoDao.update(info);
    }

    @Override
    public PosInfo findByMidAndTid(String mid, String tid) {
        return posInfoDao.findByMidAndTid(mid, tid);
    }

    @Override
    public List<PosInfo> findAll() {
        return posInfoDao.findAll();
    }
}
