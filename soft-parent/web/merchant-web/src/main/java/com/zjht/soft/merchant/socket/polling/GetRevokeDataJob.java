package com.zjht.soft.merchant.socket.polling;

import com.alibaba.dubbo.config.annotation.Reference;
import com.zjht.soft.bluelotus.socket.dao.RevokeApplyWriteDao;
import com.zjht.soft.bluelotus.socket.dao.TransactionQueryWriteDao;
import com.zjht.soft.merchant.entity.MerchantTrans;
import com.zjht.soft.merchant.service.MerchantTransService;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 获取需要撤单的订单数据。
 */
@Component("getRevokeDataJob")
public class GetRevokeDataJob {

    private static final Logger log = LoggerFactory.getLogger(GetRevokeDataJob.class);

    private static final ExecutorService Executor = Executors.newFixedThreadPool(100);

    @Autowired
    private CuratorFramework zkClient;

    @Value("${lock.polling.rootPath}")
    private String pollingLockPath;

    @Value("${order.timeout}")
    private int timeout;

    /**
     * 超时自动撤单，超过配置的天数就需要人工干预。
     */
    @Value("${order.revoke.timeout}")
    private int revokeTime;

    @Reference(version = "1.0.0")
    private MerchantTransService transService;

    @Reference(version = "1.0.0")
    private TransactionQueryWriteDao pospQueryDao;

    @Reference(version = "1.0.0")
    private RevokeApplyWriteDao pospRevokeDao;

    //    @Scheduled(cron = "0/10 * * * * ?")
    @Scheduled(fixedDelay = 1000 * 5, initialDelay = 3000)
    public void run() {
        log.debug("开始--->从数据库取出需要撤消的数据");
        String lockNode = pollingLockPath + GetRevokeDataJob.class.getName();
        boolean isCreateNodeSuccess = false;
        try {
            if (null == zkClient.checkExists().forPath(lockNode)) {
                zkClient.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(
                    lockNode);
                isCreateNodeSuccess = true;
                log.debug("成功创建锁节点[{}]", lockNode);
                // 从数据库取出需要轮询的数据

                log.debug("开始---> 更新状态为4、6的超时订单为-2");
                Date flagDate = DateUtils.addSeconds(new Date(), -timeout);
                transService.updatePayTimeOutOrders(flagDate);
                log.debug("结束 --->更新状态为4、6的超时订单");

                List<MerchantTrans> dataList = transService.findRevokeOrders(-revokeTime, -timeout);
                for (MerchantTrans data : dataList) {
                    RevokeTask task = new RevokeTask(
                        data, pospRevokeDao, pospQueryDao, transService);
                    Executor.submit(task);
                }
                if (dataList != null) {
                    dataList.clear();
                }
            } else {
                log.debug("在其它机器上运行着！");
            }
        } catch (Exception e) {
            log.error("从数据库取出需要撤消的数据时，出现异常", e);
            e.printStackTrace();
        } finally {
            this.release(isCreateNodeSuccess, lockNode);
            log.debug("结束--->从数据库取出需要撤消的数据");
        }
    }

    /* 释放资源 */
    private void release(boolean isCreateNodeSuccess, String lockNode) {
        if (isCreateNodeSuccess) {
            try {
                if (null != zkClient.checkExists().forPath(lockNode)) {
                    log.debug("成功释放[{}]节点", lockNode);
                    zkClient.delete().forPath(lockNode);
                }
            } catch (Exception ex) {
                log.error(lockNode + "节点释放失败:", ex);
            }
        }
    }

}
