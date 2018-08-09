package com.zjht.soft.bluelotus.socket.dao.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.zjht.soft.bluelotus.socket.dao.PayOrderSocketWriteDao;
import com.zjht.soft.bluelotus.socket.entity.PayOrderReq;
import com.zjht.soft.bluelotus.socket.entity.PayOrderRes;
import com.zjht.solar.commons.socket.dao.AbstractDirectSocketDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by wuqiyang on 2017/9/20。
 * AbstractDirectSocketDao为短连接。
 */
@Service(version = "1.0.0")
public class PayOrderSocketDaoImpl extends AbstractDirectSocketDao
    implements PayOrderSocketWriteDao {

    private final Logger logger = LoggerFactory.getLogger(PayOrderSocketDaoImpl.class);

    /**
     * 扫码支付接口。
     *
     * @param payOrderReq 请求参数
     * @return posp返回数据
     */
    @Override
    public PayOrderRes payOrder(
        PayOrderReq payOrderReq) throws Exception {
        logger.debug("即将socket调用posp支付,请求参数： {}", payOrderReq);
        return this.send(payOrderReq, PayOrderRes.class);
    }
}
