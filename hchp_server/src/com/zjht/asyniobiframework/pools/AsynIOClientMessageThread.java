package com.zjht.asyniobiframework.pools;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.commons.pool2.impl.GenericObjectPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zjht.asyniobiframework.callback.AsynIoClientJsonWriteCompletionHandler;
import com.zjht.asyniobiframework.callback.AsynIoClientMessageReadCompletionHandler;
import com.zjht.asyniobiframework.callback.ReadedClearCallback;
import com.zjht.asyniobiframework.config.AbstractConfig;
import com.zjht.asyniobiframework.context.RequestSession;
import com.zjht.asyniobiframework.exception.BIHandlerException;
import com.zjht.asyniobiframework.handler.HandlerConstantData;
import com.zjht.asyniobiframework.handler.IBIMessageHandler;
import com.zjht.asyniobiframework.message.AbstractRequestMessage;
import com.zjht.asyniobiframework.message.AbstractResponseMessage;

public class AsynIOClientMessageThread extends AbstractAsynIOClientThread implements Runnable{

	private static final Logger logger = LoggerFactory.getLogger(AsynIOClientJsonThread.class);
	protected GenericObjectPool<AsynIOClientMessageThread>  ioThreadPool = null;
	@Override
	public  AsynIOClientMessageThread  init(AsynchronousChannelGroup asyncChannelGroup,AbstractConfig  configInfo){
		try {
			socketChannel  = AsynchronousSocketChannel.open(asyncChannelGroup);
			socketChannel.connect(new InetSocketAddress(configInfo
					.getServerIp(), configInfo.getListenerPort()));
			socketChannel.setOption(StandardSocketOptions.TCP_NODELAY,
				      true);
			socketChannel.setOption(StandardSocketOptions.SO_REUSEADDR,
				      true);    
			socketChannel.setOption(StandardSocketOptions.SO_KEEPALIVE,
				      true);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return this;
	}
		
	@Override
	public void run() {
		AbstractResponseMessage  respMessage =  null;
		ByteBuffer  writeBuffer = ByteBuffer.allocate(1024);
		while(true){
			if(customerMessageQueue.size()>0){
				AbstractRequestMessage reqMessage = customerMessageQueue.poll();
				IBIMessageHandler<AbstractRequestMessage> handler = messageHandlers.get(HandlerConstantData.ENDECODER_HANDLER);
			   try {
				reqMessage = handler.handlerRequestMessage(reqMessage);
			} catch (BIHandlerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  //进行编码和数据加工、业务管理
				writeBuffer.putInt(28);
				writeBuffer.put(utf8.encode(reqMessage.getMsgSeq()));
				//往服务器写数据，并设定写完后的回调对象
				socketChannel.write(writeBuffer, socketChannel, new  AsynIoClientJsonWriteCompletionHandler());
				ByteBuffer readBuffer = ByteBuffer.allocate(1024);
				ReadedClearCallback  callback = new ReadedClearCallback(new RequestSession(),ioThreadPool,this);
				//准备好读取服务器返回的数据  
				socketChannel.read(readBuffer, readBuffer, new AsynIoClientMessageReadCompletionHandler());
			
			}else{
				try {
					Thread.sleep(400);
				} catch (InterruptedException e) {
					logger.debug("消费json数据失败!");
					try {
						//将当前对象在池中失效
						ioThreadPool.invalidateObject(this);
					} catch (Exception e1) {
						e1.printStackTrace();
					}
					e.printStackTrace();
				}
			}
		}
	}
	
	public AsynIOClientMessageThread setMessageHandlersQueue(ConcurrentLinkedQueue<IBIMessageHandler<AbstractRequestMessage>> handlers,ConcurrentLinkedQueue<AbstractRequestMessage>  customerMessageQueue) {
		this.messageHandlers.put(HandlerConstantData.ENDECODER_HANDLER, handlers.peek());
		this.messageHandlers.put(HandlerConstantData.FRAME_HANDLER, handlers.peek());
		this.customerMessageQueue = customerMessageQueue;
		return this;
	}
	
}
