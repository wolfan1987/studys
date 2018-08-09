package com.zjht.asyniobiframework.handler.impl;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

import com.zjht.asyniobiframework.exception.BIHandlerException;
import com.zjht.asyniobiframework.handler.HandlerConstantData;
import com.zjht.asyniobiframework.handler.JsonHandler;

/**
 * 对json数据进行编码，并算出消息固定长度
 * @author de
 *
 */
public class JSONEnDeCoderSendHandler extends JsonHandler{
	protected static Charset utf8 = Charset.forName("utf-8");  
	private  ByteBuffer  writeBuffer = ByteBuffer.allocate(HandlerConstantData.BUFFER_SIZE);
	private  ByteBuffer  tempBuffer = ByteBuffer.allocate(HandlerConstantData.BUFFER_SIZE);
	@Override  
	public void preHandler(String json) {
	}
	@Override
	public String handlerJson(String json) throws BIHandlerException {
		return json;
	}


	@Override  
	public ByteBuffer encoder(String jsonData) throws BIHandlerException {
		writeBuffer.clear();
		tempBuffer.clear();
		tempBuffer.put(utf8.encode(jsonData));
		tempBuffer.flip();   
		//System.out.println("tempBuffer.limit="+tempBuffer.limit());
		//System.out.println("tempBuffer="+tempBuffer);  
		int dataLimit = tempBuffer.limit();  
		//System.out.println("tempBuffer.position="+tempBuffer.position()+"tempBuffer.limit="+tempBuffer.limit());
		writeBuffer.putInt(dataLimit);  
		//writeBuffer.put(tempBuffer);      
		writeBuffer.put(utf8.encode(jsonData));
		writeBuffer.flip();         
		System.out.println("limit="+writeBuffer.limit());  
		//System.out.println("writeBuffer.limit="+writeBuffer.limit()+"==="+utf8.decode(writeBuffer));
		return writeBuffer;
	}

	@Override
	public String decoder(ByteBuffer byteBuffer) throws BIHandlerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void destory() {
		
	}
	
}
