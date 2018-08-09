package com.zjht.soft.merchant.dao;

import com.zjht.soft.merchant.entity.CodeAcq;

/**
 * Created by wuqiyang on 2017/9/19。
 */
public interface CodeAcqWriteDao extends CodeAcqDao {
    void create(CodeAcq codeAcq);

    boolean update(CodeAcq codeAcq);
}
