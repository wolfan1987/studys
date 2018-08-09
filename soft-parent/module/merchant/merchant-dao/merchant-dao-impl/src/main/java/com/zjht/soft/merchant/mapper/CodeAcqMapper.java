package com.zjht.soft.merchant.mapper;

import com.zjht.soft.merchant.entity.CodeAcq;

import java.util.List;

/**
 * @author wuqiyang 。
 * 2017/10/24。
 */
public interface CodeAcqMapper {
    void create(CodeAcq codeAcq);

    int update(CodeAcq codeAcq);

    CodeAcq findByHeadCode(String headCode);

    List<CodeAcq> findAll();

}
