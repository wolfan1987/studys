package com.zjht.soft.merchant.socket.business;

import com.alibaba.fastjson.JSONObject;
import com.zjht.soft.merchant.socket.ResponseConstant;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

/**
 * Created by zjhtadmin on 2017/11/9.
 */
public  abstract class AbstractBiz {


    protected  static final ExecutorService logExec = Executors.newFixedThreadPool(3);
    protected  static final ExecutorService orderExec = Executors.newFixedThreadPool(7);

    /**
     * 验证业务数据完整性，验证以下数据是否存在
     * 1、订单号
     * 2、终端号
     * 3、交易类型
     * @return
     */
    protected  boolean  validataBizData(JSONObject requestData  ){

        return false;
    }
    protected  Map<String, Object> checkData(JSONObject clientData) {
        Map<String, Object> result = new HashMap<String, Object>();
        String businessId = clientData.getString(BizInterface.BUSINESS_ID_FIELD);
        String businessTypeStr = clientData.getString(BizInterface.BUSINESS_TYPE_FIELD);
        BizTypeEnum businessType = BizTypeEnum.getEnumByValue(businessTypeStr);
        // 检验businessId
        if (StringUtils.isBlank(businessId)) {
            result.put("code", ResponseConstant.BUSINESS_ID_ERROR_CODE);
            result.put("message", ResponseConstant.BUSINESS_ID_ERROR_MESG);
            return result;
        }
        // 检验businessType
        if (businessType == null) {
            result.put("code", ResponseConstant.BUSINESS_TYPE_ERROR_CODE);
            result.put("message", ResponseConstant.BUSINESS_TYPE_ERROR_MESG);
            return result;
        }
        result.put("success", true);
        return result;
    }


}
