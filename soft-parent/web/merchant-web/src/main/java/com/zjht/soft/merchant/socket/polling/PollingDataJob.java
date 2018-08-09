package com.zjht.soft.merchant.socket.polling;

import com.alibaba.dubbo.config.annotation.Reference;
import com.zjht.soft.bluelotus.socket.dao.RevokeApplyWriteDao;
import com.zjht.soft.bluelotus.socket.dao.TransactionQueryWriteDao;
import com.zjht.soft.merchant.entity.MerchantTrans;
import com.zjht.soft.merchant.service.MerchantTransService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component("pollingDataJob")
public class PollingDataJob {

    private static final Logger log = LoggerFactory.getLogger(PollingDataJob.class);

    private static final ExecutorService Executor = Executors.newCachedThreadPool();

    public static List<MerchantTrans> QUERY_TASK_LIST = Collections.synchronizedList(
        new ArrayList<MerchantTrans>());

    @Value("${lock.polling.rootPath}")
    private String pollingLockPath;

    @Value("${order.timeout}")
    private int timeout;

    @Reference(version = "1.0.0")
    private MerchantTransService transService;

    @Reference(version = "1.0.0")
    private TransactionQueryWriteDao queryDao;

    @Reference(version = "1.0.0")
    private RevokeApplyWriteDao revokeWriteDao;

    //    @Scheduled(cron = "0/5 * * * * ?")
    @Scheduled(fixedDelay = 1000 * 3, initialDelay = 3000)
    public void run() {
        log.debug("开始--->从取出需要轮询的数据 QUERY_TASK_LIST.size:{}", QUERY_TASK_LIST.size());
        try {
            for (MerchantTrans order : QUERY_TASK_LIST) {
                QueryTask task = new QueryTask(order, queryDao, transService, timeout);
                Executor.submit(task);
            }
        } catch (Exception e) {
            log.error("从数据库取出需要轮询的数据时，出现异常", e);
        } finally {
            log.debug("结束--->从数据库取出需要轮询的数据");
        }
    }

}
