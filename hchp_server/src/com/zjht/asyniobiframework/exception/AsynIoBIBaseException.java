package com.zjht.asyniobiframework.exception;

public class AsynIoBIBaseException extends Exception {

	private static final long serialVersionUID = 7250083108129495413L;
	
	private Exception exception;

	public AsynIoBIBaseException(Exception exception) {
		this.exception = exception;
	}

	public AsynIoBIBaseException(Exception exception, String message) {
		super(message);
		this.exception = exception;
	}

	public AsynIoBIBaseException(String message) {
		super(message);
	}

	public Exception getException() {
		return exception;
	}
}
