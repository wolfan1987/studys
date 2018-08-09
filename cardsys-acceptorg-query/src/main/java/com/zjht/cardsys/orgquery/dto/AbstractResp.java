package com.zjht.cardsys.orgquery.dto;

/**
 * Created by zjhtadmin on 2018/1/9.
 */
public  abstract class AbstractResp {

    private  String  respCode;

    private  String   respMsg;

  public AbstractResp(String respCode, String respMsg){
      this.respCode =  respCode;
      this.respMsg = respMsg;
  }

    public String getRespCode() {
        return respCode;
    }

    public void setRespCode(String respCode) {
        this.respCode = respCode;
    }

    public String getRespMsg() {
        return respMsg;
    }

    public void setRespMsg(String respMsg) {
        this.respMsg = respMsg;
    }
}
