package com.zjht.soft.bluelotus.socket.dao;

import com.zjht.soft.bluelotus.socket.entity.TransactionQueryReq;
import com.zjht.soft.bluelotus.socket.entity.TransactionQueryRes;

/**
 * 交易查询只读接口。
 * Created by yuanyaping on 2017/9/20.
 */
public interface TransactionQueryWriteDao extends TransactionQueryDao {

    /**
     * 交易查询接口。
     *
     * @param transactionQueryReq 请求参数
     * @return posp返回数据
     */
    TransactionQueryRes transactionQuery(TransactionQueryReq transactionQueryReq) throws Exception;
}
