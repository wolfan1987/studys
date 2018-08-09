package com.zjht.asyniobiframework.exception;

public class HandlerRequestException extends AsynIoBIBaseException{

	private static final long serialVersionUID = -1975966956761889669L;
	
	public HandlerRequestException(Exception exception) {
		super(exception);
	}

	public HandlerRequestException(Exception exception, String message) {
		super(exception, message);
	}

	public HandlerRequestException(String message) {
		super(message);
	}
	

}