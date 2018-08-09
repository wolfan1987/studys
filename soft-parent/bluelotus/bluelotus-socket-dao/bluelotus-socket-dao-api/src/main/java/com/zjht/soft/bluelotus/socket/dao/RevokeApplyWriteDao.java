package com.zjht.soft.bluelotus.socket.dao;

import com.zjht.soft.bluelotus.socket.entity.RevokeApplyReq;
import com.zjht.soft.bluelotus.socket.entity.RevokeApplyRes;


/**
 * 撤销申请读写接口。
 * Created by yuanyaping on 2017/9/20.
 */
public interface RevokeApplyWriteDao extends RevokeApplyDao{

    /**
     * 撤销申请接口。
     *
     * @param revokeApplyReq 请求参数
     * @return posp返回数据
     */
    RevokeApplyRes revokeApply(RevokeApplyReq revokeApplyReq) throws Exception;
}
