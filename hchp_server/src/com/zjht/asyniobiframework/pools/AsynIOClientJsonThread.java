package com.zjht.asyniobiframework.pools;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.pool2.impl.GenericObjectPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zjht.asyniobiframework.callback.AsynIoClientJsonReadCompletionHandler;
import com.zjht.asyniobiframework.config.AbstractConfig;
import com.zjht.asyniobiframework.exception.BIHandlerException;
import com.zjht.asyniobiframework.handler.HandlerConstantData;
import com.zjht.asyniobiframework.handler.IBIJsonHandler;


public class AsynIOClientJsonThread  extends AbstractAsynIOClientThread implements Runnable {

	private static final Logger logger = LoggerFactory.getLogger(AsynIOClientJsonThread.class);
	protected GenericObjectPool<AsynIOClientJsonThread>  ioThreadPool = null;
	private CountDownLatch latch = null;
	private  ByteBuffer  writeBuffer = ByteBuffer.allocate(HandlerConstantData.BUFFER_SIZE);
	private  ByteBuffer  tempBuffer = ByteBuffer.allocate(HandlerConstantData.BUFFER_SIZE);
	private ByteBuffer readBuffer = ByteBuffer.allocate(1024);  
	private  IBIJsonHandler  encoderHandler  = null;
	private String jsonData = "";
	private Lock  lock = new ReentrantLock();
	private  volatile  boolean  hasReturn = false;
	@Override
	public  AsynIOClientJsonThread  init(AsynchronousChannelGroup asyncChannelGroup,AbstractConfig  configInfo){
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
		hasReturn = false;
		while(true){
			 jsonData = customerJsonQueue.poll();
			if(jsonData!=null){  
				latch = new CountDownLatch(1);
				lock.lock();
			      // encoderHandler = jsonHandlers.get(HandlerConstantData.ENDECODER_HANDLER);
				try {   
					writeBuffer = encoderHandler.encoder(jsonData);
				} catch (BIHandlerException e1) {
					e1.printStackTrace();   
				}
				 socketChannel.write(writeBuffer);
				 readBuffer.clear();
				 socketChannel.read(readBuffer, readBuffer, new AsynIoClientJsonReadCompletionHandler(socketChannel,latch));
	 			try {
	 				
					latch.await();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}   
	 			latch = null;
	 			lock.unlock();
			}else{
				try {
					if(!hasReturn){
						ioThreadPool.returnObject(this);
						hasReturn = true;
					}
					Thread.sleep(200);
				} catch (InterruptedException e) {
					logger.debug("消费json数据失败!"+jsonData);
					try {
						socketChannel.shutdownInput();
						socketChannel.shutdownOutput();
						socketChannel.close();
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

	public AsynIOClientJsonThread setJsonHandlersQueue(ConcurrentLinkedQueue<Class<?>> jsonHandlers,ConcurrentLinkedQueue<String> customerJsonQueue) {
		try {
			this.encoderHandler = (IBIJsonHandler) jsonHandlers.peek().newInstance();
			//this.jsonHandlers.put(HandlerConstantData.ENDECODER_HANDLER, jsonHandler);
			//this.jsonHandlers.put(HandlerConstantData.FRAME_HANDLER, jsonHandlers.peek());
			this.customerJsonQueue = customerJsonQueue;
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return this;  
	}
	
	public AsynIOClientJsonThread setThreadPool(GenericObjectPool<AsynIOClientJsonThread>  ioThreadPool) {
		this.ioThreadPool = ioThreadPool;
		return this;
	}
	
	public static void main(String[] args) {
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		buffer.putInt(1123451111);
		buffer.putChar('A');
		buffer.putShort((short) 3305);
		buffer.putFloat((float)3.14);
		buffer.putDouble(3.1415926);
		buffer.putLong(12434534545L);
		byte[]  str = "Hello World!".getBytes();
		System.out.println(buffer.getInt(0));
		System.out.println(buffer.getChar(1));
		System.out.println(buffer.getShort(2));
		System.out.println(buffer.getFloat(3));
		System.out.println(buffer.getDouble(4));
		System.out.println(buffer.getLong(5));
		//System.out.println(buffer.getInt(index);
	}
}
