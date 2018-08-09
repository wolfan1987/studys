package com.zjht.soft.bluelotus.socket.dao.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.zjht.soft.bluelotus.socket.dao.TransactionQueryWriteDao;
import com.zjht.soft.bluelotus.socket.entity.TransactionQueryReq;
import com.zjht.soft.bluelotus.socket.entity.TransactionQueryRes;
import com.zjht.solar.commons.socket.dao.AbstractDirectSocketDao;
import com.zjht.solar.commons.socket.dao.AbstractSocketDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 交易查询实体类。
 * Created by yuanyaping on 2017/9/20.
 */
@Service(version = "1.0.0")
public class TransactionQueryDaoImpl extends AbstractDirectSocketDao
    implements TransactionQueryWriteDao {

    private final Logger logger = LoggerFactory.getLogger(TransactionQueryDaoImpl.class);

    /**
     * 交易查询接口。
     *
     * @param transactionQueryReq 请求参数
     * @return posp返回数据
     */
    @Override
    public TransactionQueryRes transactionQuery(TransactionQueryReq transactionQueryReq)
        throws Exception {
        logger.debug("即将socket调用posp查询,请求参数： {}", transactionQueryReq);
        return this.send(transactionQueryReq, TransactionQueryRes.class);
    }
}
