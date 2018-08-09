package com.zjht.soft.merchant.service.impl;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.zjht.soft.merchant.dao.MerchantTransDao;
import com.zjht.soft.merchant.dao.MerchantTransLogWriteDao;
import com.zjht.soft.merchant.dao.MerchantTransWriteDao;
import com.zjht.soft.merchant.dao.PosInfoDao;
import com.zjht.soft.merchant.entity.MerchantTrans;
import com.zjht.soft.merchant.entity.PosInfo;
import com.zjht.soft.merchant.service.MerchantTransService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by wuqiyang on 2017/9/18。
 */
@Service(version = "1.0.0")
@Transactional
public class MerchantTransServiceImpl implements MerchantTransService {
    /**
     * 日志输出。
     */
    private final Logger logger = LoggerFactory.getLogger(MerchantTransServiceImpl.class);

    private static final SimpleDateFormat DATE8 = new SimpleDateFormat("yyyyMMdd");

    private static final String INIT_BATCH_NO = "000001";

    private static final String INIT_SYSTRACE = "A00001";

    private static final String MAX_BATCH_NO = "999999";

    private static final String MAX_SYSTRACE = "Z99999";

    @Reference(version = "1.0.0")
    private PosInfoDao posInfoDao;

    @Reference(version = "1.0.0")
    private MerchantTransWriteDao merchantOrderWriteDao;

    @Reference(version = "1.0.0")
    private MerchantTransDao merchantOrderDao;

    @Reference(version = "1.0.0")
    private MerchantTransLogWriteDao merchantOrderLogWriteDao;

    @Override
    public MerchantTrans create(
        MerchantTrans merchantOrder) {
        String mid = merchantOrder.getMid();
        String tid = merchantOrder.getTid();
        PosInfo   newPosInfo =  this.getNextPosInfo(mid,tid);
        if(newPosInfo!=null){
            merchantOrder.setBatchNo(newPosInfo.getBatchNo());
            merchantOrder.setSystrace(newPosInfo.getSystrace());
        }else{
            throw new IllegalArgumentException("交易失败，创建订单时2次获取流水号失败");
        }
        return merchantOrderWriteDao.create(merchantOrder);
//        for (int i = 0; i < 2; i++) {
//            PosInfo posInfo = posInfoDao.findByMidAndTid(mid, tid);
//            PosInfo newPosInfo = this.getNewPosInfo(posInfo, mid, tid);
//            int status = posInfoDao.update(newPosInfo);
//            if (status == 1) {
//                merchantOrder.setBatchNo(newPosInfo.getBatchNo());
//                merchantOrder.setSystrace(newPosInfo.getSystrace());
//                break;
//            } else {
//                if (i == 1) {
//                    throw new IllegalArgumentException("交易失败，获取流水号异常");
//                }
//            }
//        }

    }

    @Override
    public boolean update(MerchantTrans merchantOrder) {
        try {
            return merchantOrderWriteDao.update(merchantOrder);
        } catch (Exception ex) {
            logger.error("MerchantTransServiceImpl.update exception {}", ex);
            return false;
        }

    }

    @Override
    public MerchantTrans findById(Long id) {
        try {
            return merchantOrderDao.findByPrimaryKey(id);
        } catch (Exception ex) {
            logger.error("MerchantTransServiceImpl .findById exception {}", ex);
            return null;
        }

    }

    @Override
    public MerchantTrans findByOrderId(String orderId, String txnId) {
        try {
            return merchantOrderDao.findByOrderId(orderId, txnId);
        } catch (Exception ex) {
            logger.error("MerchantTransServiceImpl.findByOrderId exception {}", ex);
            return null;
        }
    }

    /**
     * 查询出状态未知或用户输入密码的订单（需要轮询的订单集合）。
     *
     * @param mis
     * @return
     */
    @Override
    public List<MerchantTrans> findAskOrders(int mis) {
        Date date = new Date();//获取当前时间
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.SECOND, mis);//30秒前
        Date secondTime = calendar.getTime();
        try {
            return merchantOrderDao.findAskOrders(secondTime);
        } catch (Exception ex) {
            logger.error("MerchantTransServiceImpl.findAskOrders 轮询订单集合 exception {}", ex);
            return null;
        }
    }

    /**
     * 查询出需要撤销的订单集合 订单状态标识为-2。
     *
     * @return
     */
    @Override
    public List<MerchantTrans> findRevokeOrders(int minute, int mis) {
        Date date = new Date();//获取当前时间
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE, minute);//30分钟前
        Date beforday = calendar.getTime();
        calendar.setTime(date);
        calendar.add(Calendar.SECOND, mis);//30秒前
        Date secondTime = calendar.getTime();

        try {
            return merchantOrderDao.findRevokeOrders(beforday, secondTime);
        } catch (Exception ex) {
            logger.error("轮询撤销订单异常 :{}", ex);
            return null;
        }
    }

    /**
     * 将支付超时的订单状态由4或6改为-2标记为即将撤单的状态。
     *
     * @param beforDate 当前系统时间减去超时时间。
     */
    @Override
    public void updatePayTimeOutOrders(Date beforDate) {
        try {
            merchantOrderDao.updatePayTimeOutOrders(beforDate);
        } catch (Exception ex) {
            logger.error("修改超时订单状态异常 ：{}", ex);
        }
    }

    @Override
    public  synchronized PosInfo getNextPosInfo(String mid, String tid) {
        for (int i = 0; i < 2; i++) {
            PosInfo posInfo = posInfoDao.findByMidAndTid(mid, tid);
            PosInfo newPosInfo = this.getNewPosInfo(posInfo, mid, tid);
            int status = posInfoDao.update(newPosInfo);
            if (status == 1) {
              return newPosInfo;
            } else {
                if (i == 1) {
                    throw new IllegalArgumentException("交易失败，获取终端流水号异常");
                }
            }
        }
        return null;
    }

    /**
     * 获取批次号流水号。
     *
     * @param posInfo
     * @param mid
     * @param tid
     * @return
     */
    private PosInfo getNewPosInfo(PosInfo posInfo, String mid, String tid) {

        String newBatchNo = "";
        String newSystrace = "";
        PosInfo info = new PosInfo();
        info.setTid(tid);
        info.setMid(mid);
        if (posInfo == null) {
            info.setSystrace(INIT_SYSTRACE);
            info.setBatchNo(INIT_BATCH_NO);
            info.setUpdateTime(DATE8.format(new Date()));
            posInfoDao.save(info);
            return info;
        } else {
            //取批次号 如果更新日期等于当天则直接返回批次号
            String oldBatchNo = posInfo.getBatchNo();
            if (!DATE8.format(new Date()).equals(posInfo.getUpdateTime())) {
                //若是不等则返回批次号+1
                //如果是999999则返回000001否则加1
                if (MAX_BATCH_NO.equals(oldBatchNo)) {
                    posInfo.setBatchNo(INIT_BATCH_NO);
                } else {
                    newBatchNo = String.format("%06d", Integer.parseInt(oldBatchNo) + 1);
                    posInfo.setBatchNo(newBatchNo);
                    posInfo.setUpdateTime(DATE8.format(new Date()));
                }
            }

            //取流水号
            String oldSystrace = posInfo.getSystrace();
            posInfo.setOldSystrace(oldSystrace);
            if (StringUtils.isEmpty(oldSystrace) || MAX_SYSTRACE.equals(oldSystrace)) {
                posInfo.setSystrace(INIT_SYSTRACE);
            } else {
                if (oldSystrace.endsWith("99999")) {
                    char index = oldSystrace.charAt(0);
                    int newIndex = (int) index + 1;
                    newSystrace = String.format((char) newIndex + "%05d", 1);
                    posInfo.setSystrace(newSystrace);
                } else {
                    String num = oldSystrace.substring(1);
                    newSystrace = String.format(oldSystrace.charAt(0) + "%05d",
                                                Integer.parseInt(num) + 1);
                    posInfo.setSystrace(newSystrace);
                }
            }
            return posInfo;
        }
    }
}
