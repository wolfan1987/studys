package com.zjht.cardsys.orgquery.exceptions;

/**
 * Created by zjhtadmin on 2018/1/9.
 */
public class AORBaseException  extends Exception {
    private Exception exception;

    public AORBaseException(Exception exception) {
        this.exception = exception;
    }

    public AORBaseException(Exception exception, String message) {
        super(message);
        this.exception = exception;
    }

    public AORBaseException(String message) {
        super(message);
    }

}
