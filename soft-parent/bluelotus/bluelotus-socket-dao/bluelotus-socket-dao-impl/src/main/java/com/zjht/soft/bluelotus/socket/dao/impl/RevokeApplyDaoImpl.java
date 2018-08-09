package com.zjht.soft.bluelotus.socket.dao.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.zjht.soft.bluelotus.socket.dao.RevokeApplyWriteDao;
import com.zjht.soft.bluelotus.socket.entity.RevokeApplyReq;
import com.zjht.soft.bluelotus.socket.entity.RevokeApplyRes;
import com.zjht.solar.commons.socket.dao.AbstractDirectSocketDao;

/**
 * 撤销申请实体类。
 * Created by yuanyaping on 2017/9/20.
 */
@Service(version = "1.0.0")
public class RevokeApplyDaoImpl extends AbstractDirectSocketDao implements RevokeApplyWriteDao {

    /**
     * 撤销申请接口。
     *
     * @param revokeApplyReq 请求参数
     * @return posp返回数据
     */
    @Override
    public RevokeApplyRes revokeApply(RevokeApplyReq revokeApplyReq) throws Exception {
        return this.send(revokeApplyReq, RevokeApplyRes.class);
    }
}
