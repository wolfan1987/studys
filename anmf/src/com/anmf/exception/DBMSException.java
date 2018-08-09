package com.anmf.exception;

/*
 * 非Hibernate操作数据库即直接用JDBC操作数据库时的公共异常
 */
public class DBMSException extends ANMFException {

	public DBMSException(Exception exception) {
		super(exception);
		// TODO Auto-generated constructor stub
	}

	public DBMSException(Exception exception, String message) {
		super(exception, message);
		// TODO Auto-generated constructor stub
	}

	public DBMSException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

}
