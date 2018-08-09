package com.anmf.exception;

/**
 * 业务代理层异常
 * 
 * @author Administrator
 * 
 */
public class BRDelegateException extends ANMFException {

	public BRDelegateException(Exception exception) {
		super(exception);
		// TODO Auto-generated constructor stub
	}

	public BRDelegateException(Exception exception, String message) {
		super(exception, message);
		// TODO Auto-generated constructor stub
	}

	public BRDelegateException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

}
