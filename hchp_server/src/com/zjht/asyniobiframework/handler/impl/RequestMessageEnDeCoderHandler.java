package com.zjht.asyniobiframework.handler.impl;

import java.nio.ByteBuffer;

import com.zjht.asyniobiframework.exception.BIHandlerException;
import com.zjht.asyniobiframework.handler.MessageHandler;
import com.zjht.asyniobiframework.message.AbstractRequestMessage;

public class RequestMessageEnDeCoderHandler  extends MessageHandler {

	@Override
	public void preHandler(AbstractRequestMessage t) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public AbstractRequestMessage handlerRequestMessage(
			AbstractRequestMessage message) throws BIHandlerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void destory() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ByteBuffer encoder(AbstractRequestMessage reqMessage)
			throws BIHandlerException {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public AbstractRequestMessage decoder(ByteBuffer byteBuffer) throws BIHandlerException {
		// TODO Auto-generated method stub
		return null;
	}

}
