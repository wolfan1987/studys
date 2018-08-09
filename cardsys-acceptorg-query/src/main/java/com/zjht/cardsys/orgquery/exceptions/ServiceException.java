package com.zjht.cardsys.orgquery.exceptions;

/**
 * Created by zjhtadmin on 2018/1/9.
 */
public class ServiceException  extends AORBaseException {

    public ServiceException(Exception exception) {
        super(exception);

    }

    public ServiceException(Exception exception, String message) {
        super(exception, message);

    }

    public ServiceException(String message) {
        super(message);

    }
}
