package com.anmf.exception;

/**
 * ���ֹ������쳣
 * 
 * @author Administrator
 * 
 */
public class FactoryException extends ANMFException {

	public FactoryException(Exception exception) {
		super(exception);
		// TODO Auto-generated constructor stub
	}

	public FactoryException(Exception exception, String message) {
		super(exception, message);
		// TODO Auto-generated constructor stub
	}

	public FactoryException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

}
