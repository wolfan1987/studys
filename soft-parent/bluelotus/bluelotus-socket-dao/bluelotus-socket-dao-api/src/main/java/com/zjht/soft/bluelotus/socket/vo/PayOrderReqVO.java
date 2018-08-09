package com.zjht.soft.bluelotus.socket.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.zjht.soft.bluelotus.socket.entity.PayOrderReq;

/**
 * 
 * 描述说明
 *<br>
 * Created by 黄灿贤 on 2017年9月28日
 * @version 1.0-SNAPSHOT
 */
public class PayOrderReqVO extends PayOrderReq {
	
    private static final long   serialVersionUID = 1L;
    
    /**
     * 交易日期
     */
    @JSONField(name = "txn_date")
    private String txnDate;

    /**
     * 交易时间
     */
    @JSONField(name = "txn_time")
    private String orderId;

}
