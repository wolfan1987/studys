package com.zjht.asyniobiframework.exception;

public class MessageTypeRegisterException  extends AsynIoBIBaseException{

	private static final long serialVersionUID = -1975966956761889669L;
	
	public MessageTypeRegisterException(Exception exception) {
		super(exception);
	}

	public MessageTypeRegisterException(Exception exception, String message) {
		super(exception, message);
	}

	public MessageTypeRegisterException(String message) {
		super(message);
	}
	

}
