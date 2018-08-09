package com.zjht.soft.merchant.service;

import com.zjht.soft.merchant.entity.CodeAcq;

import java.util.List;

/**
 * @author wuqiyang 。
 * 2017/10/24。
 */
public interface CodeAcqService {

    void create(CodeAcq codeAcq);

    boolean update(CodeAcq codeAcq);

    CodeAcq findByHeadCode(String headCode);

    List<CodeAcq> findAll();
}
