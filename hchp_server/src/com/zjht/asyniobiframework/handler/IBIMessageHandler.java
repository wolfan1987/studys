package com.zjht.asyniobiframework.handler;

import java.nio.ByteBuffer;

import com.zjht.asyniobiframework.exception.BIHandlerException;

/**
 * 业务Handler接口
 * @author de
 *
 */
public interface IBIMessageHandler<T> {

	public  void  preHandler(T t);
	
	public  T  handlerRequestMessage(T  message) throws BIHandlerException;
	
	public  void  destory();
	
	public ByteBuffer  encoder(T  reqMessage) throws BIHandlerException;
	public T    decoder(ByteBuffer  byteBuffer) throws BIHandlerException;
	
}
