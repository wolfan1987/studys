package com.zjht.soft.merchant.dao;

import com.zjht.soft.merchant.entity.CodeAcq;

import java.util.List;

/**
 * Dao--读。
 *
 * @author wuqiyang on 201。
 */
public interface CodeAcqDao {

    CodeAcq findByHeadCode(String headCode);

    List<CodeAcq> findAll();
}
