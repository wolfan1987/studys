package com.zjht.asyniobiframework.message;


public  interface  IMessage<T>{
	
	public  byte[]  encoder();
	
	public  T  decoder(byte[]  responseByte);
}