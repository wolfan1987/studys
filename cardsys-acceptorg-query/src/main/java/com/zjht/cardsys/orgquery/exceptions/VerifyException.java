package com.zjht.cardsys.orgquery.exceptions;

/**
 * Created by zjhtadmin on 2018/1/9.
 */
public class VerifyException extends AORBaseException {

    public VerifyException(Exception exception) {
        super(exception);

    }

    public VerifyException(Exception exception, String message) {
        super(exception, message);

    }

    public VerifyException(String message) {
        super(message);

    }
}