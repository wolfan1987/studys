package com.zjht.soft.merchant.socket;

public class ResponseConstant {

    public static final String BUSINESS_EXCEPTION_INFO = "exception";
    
    public static final String BUSINESS_ERROR_CODE = "10000";
    public static final String BUSINESS_ERROR_MESG = "商家平台出现异常！";
    
    public static final String BUSINESS_ID_ERROR_CODE = "10001";
    public static final String BUSINESS_ID_ERROR_MESG = "systrace不能为空";

    public static final String BUSINESS_TYPE_ERROR_CODE = "10002";
    public static final String BUSINESS_TYPE_ERROR_MESG = "不支持的业务类型，请检查txn_id字段";

    public static final String TXN_DATE_ERROR_CODE = "10003";
    public static final String TXN_DATE_ERROR_MESG = "交易日期必填";

    public static final String TXN_TIME_ERROR_CODE = "10004";
    public static final String TXN_TIME_ERROR_MESG = "交易时间必填";

    public static final String MID_ERROR_CODE = "10005";
    public static final String MID_ERROR_MESG = "商户号必填";

    public static final String TID_ERROR_CODE = "10006";
    public static final String TID_ERROR_MESG = "终端号必填";

    
    
}
