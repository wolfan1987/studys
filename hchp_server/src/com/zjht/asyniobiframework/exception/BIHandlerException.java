package com.zjht.asyniobiframework.exception;

public class BIHandlerException extends AsynIoBIBaseException{

	private static final long serialVersionUID = -1975966956761889669L;
	
	public BIHandlerException(Exception exception) {
		super(exception);
	}

	public BIHandlerException(Exception exception, String message) {
		super(exception, message);
	}

	public BIHandlerException(String message) {
		super(message);
	}
	

}
