package com.anmf.exception;

/*
 * ��Hibernate�������ݿ⼴ֱ����JDBC�������ݿ�ʱ�Ĺ����쳣
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
