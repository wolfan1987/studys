package com.anmf.exception;

/**
 * Hibernate操作数据库公共层异常
 * 
 * @author Administrator
 * 
 */
public class CommonException extends ANMFException {
	public CommonException(Exception exception) {
		super(exception);
	}

	public CommonException(Exception exception, String message) {
		super(exception, message);
	}

	public CommonException(String message) {
		super(message);
	}

}
