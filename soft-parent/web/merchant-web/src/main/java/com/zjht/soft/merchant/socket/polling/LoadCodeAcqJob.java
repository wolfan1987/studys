package com.zjht.soft.merchant.socket.polling;

import com.alibaba.dubbo.config.annotation.Reference;
import com.zjht.soft.merchant.dao.PayTypeDao;
import com.zjht.soft.merchant.entity.CodeAcq;
import com.zjht.soft.merchant.entity.PayType;
import com.zjht.soft.merchant.service.CodeAcqService;
import com.zjht.soft.merchant.service.MerchantTransService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 从数据库获取最新的卡bin配置
 */
@Component("LoadCodeAcqJob")
public class LoadCodeAcqJob {

    private static final Logger log = LoggerFactory.getLogger(LoadCodeAcqJob.class);

    public static ConcurrentHashMap<String, Integer> PAY_FLAG_MAP = new ConcurrentHashMap<>();

    private static ConcurrentHashMap<String, CodeAcq> CARD_BIN_MAP = new ConcurrentHashMap<>();

    private static TreeSet<Integer> binHead = new TreeSet<>((o1, o2) -> o2.compareTo(o1));

    @Reference(version = "1.0.0")
    private CodeAcqService codeAcqService;

    @Reference(version = "1.0.0")
    private PayTypeDao payTypeDao;

    @Reference(version = "1.0.0")
    private MerchantTransService transService;

    @Value("${order.timeout}")
    private int timeout;

    //应用启动时调用，间隔5分钟
    @Scheduled(fixedDelay = 1000 * 60 * 30, initialDelay = 3000)
    public void run() {
        log.debug("开始--->从数据库获取最新的卡bin配置");
        try {
            CARD_BIN_MAP.clear();
            PAY_FLAG_MAP.clear();
            binHead.clear();
            List<PayType> payTypes = payTypeDao.findAll();
            for (PayType type : payTypes) {
                PAY_FLAG_MAP.put(type.getMid() + type.getTid(), type.getPayCode());
            }
            List<CodeAcq> list = codeAcqService.findAll();
            setCardBinMap(list);
            log.debug("结束--->从数据库获取最新的卡bin配置");
        } catch (Exception e) {
            log.warn("从数据库获取最新的卡bin配置时，出现异常:", e);
        }
    }

    private static void setCardBinMap(List<CodeAcq> list) {
        if (!list.isEmpty()) {
            for (CodeAcq codeAcq : list) {
                binHead.add(codeAcq.getHeadLen());
                CARD_BIN_MAP.put(codeAcq.getHeadCode(), codeAcq);
            }
            log.debug("CARD_BIN_LIST大小为:{}", list.size());
        }
    }

    /**
     * 根据付款码[auth_code]的开头，来判断是哪种支付类型。
     *
     * @param authCode 付款码
     * @return CodeAcq
     */
    public static CodeAcq getPayType(String authCode) {
        CodeAcq result;
        if (StringUtils.isNotEmpty(authCode)) {
            authCode = StringUtils.trimToEmpty(authCode);
            for (Integer headLen : binHead) {
                String cardBinStr = authCode.substring(0, headLen);
                result = CARD_BIN_MAP.get(cardBinStr);
                if (result != null) {
                    return result;
                }
            }
        }
        log.warn("无法判断支付类型，authCode={}", authCode);
        return null;
    }
}
