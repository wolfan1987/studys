package com.zjht.asyniobiframework.message.beans;

import com.zjht.asyniobiframework.message.AbstractResponseMessage;

public class TestMessageResponse  extends AbstractResponseMessage{

	public  TestMessageResponse(int count){
		System.out.println("消息"+count+"已处理返回！");
	}
	
	@Override
	public byte[] encoder() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TestMessageResponse decoder(byte[] responseByte) {
		// TODO Auto-generated method stub
		return null;
	}

}
