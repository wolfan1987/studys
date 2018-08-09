package com.zjht.asyniobiframework.pools;

import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.charset.Charset;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.zjht.asyniobiframework.config.AbstractConfig;
import com.zjht.asyniobiframework.handler.IBIJsonHandler;
import com.zjht.asyniobiframework.handler.IBIMessageHandler;
import com.zjht.asyniobiframework.message.AbstractRequestMessage;

public  abstract  class AbstractAsynIOClientThread  implements Runnable{

	protected static Charset utf8 = Charset.forName("utf-8");  
    protected AsynchronousSocketChannel socketChannel;
    protected ConcurrentHashMap<String,IBIMessageHandler<AbstractRequestMessage>>  messageHandlers = new ConcurrentHashMap<String,IBIMessageHandler<AbstractRequestMessage>>();
	protected ConcurrentHashMap<String,IBIJsonHandler>  jsonHandlers =  new ConcurrentHashMap<String,IBIJsonHandler>();
	protected ConcurrentLinkedQueue<String>   customerJsonQueue = null;
	protected ConcurrentLinkedQueue<AbstractRequestMessage>  customerMessageQueue = null;
	
	public abstract   AbstractAsynIOClientThread  init(AsynchronousChannelGroup asyncChannelGroup,AbstractConfig  configInfo);

	public AsynchronousSocketChannel getSocketChannel() {
		return socketChannel;
	}
	

	@Override
	public void run() {
		
	}
}
