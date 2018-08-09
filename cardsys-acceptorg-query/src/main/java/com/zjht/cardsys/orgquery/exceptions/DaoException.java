package com.zjht.cardsys.orgquery.exceptions;

/**
 * Created by zjhtadmin on 2018/1/9.
 */
public class DaoException extends AORBaseException {

    public DaoException(Exception exception) {
        super(exception);

    }

    public DaoException(Exception exception, String message) {
        super(exception, message);

    }

    public DaoException(String message) {
        super(message);

    }
}