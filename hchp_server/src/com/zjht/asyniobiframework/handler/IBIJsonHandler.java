package com.zjht.asyniobiframework.handler;

import java.nio.ByteBuffer;

import com.zjht.asyniobiframework.exception.BIHandlerException;

public interface IBIJsonHandler {

    public void preHandler(String json);
	public ByteBuffer  encoder(String jsonData) throws BIHandlerException;
	public String    decoder(ByteBuffer  byteBuffer) throws BIHandlerException;
	public String handlerJson(String json) throws BIHandlerException;
	public  void  destory();
	
}
