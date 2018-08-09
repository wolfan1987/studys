package com.zjht.asyniobiframework.pools;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Logger;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import com.zjht.asyniobiframework.handler.IBIJsonHandler;
import com.zjht.asyniobiframework.handler.IBIMessageHandler;
import com.zjht.asyniobiframework.message.AbstractRequestMessage;

public class AsynIOClientMessageThreadFactory extends BasePooledObjectFactory<AsynIOClientMessageThread> implements IAsynIORegisterBinding{

	private static final Logger logger = Logger.getLogger(AsynIOClientJsonThreadFactory.class.getName());
	private  AsynIOGroupProvider  provider = null;
	private  GenericObjectPoolConfig config =  new GenericObjectPoolConfig();
	private  ConcurrentLinkedQueue<IBIMessageHandler<?>> contextMessageHandlerQueue = null;
	private  GenericObjectPool<AsynIOClientMessageThread> ioThreadPool;
	/**
	 * 待消费的消息队列（当要对消费对象进行编/解码、装帧时使用
	 */
	private  ConcurrentLinkedQueue<AbstractRequestMessage> requestMessageQueue = null;
	public static AsynIOClientJsonThreadFactory  newFactory(){
		return new AsynIOClientJsonThreadFactory();  
	}
	
	public  void   init(ConcurrentLinkedQueue<AbstractRequestMessage> requestMessageQueue, ConcurrentLinkedQueue<IBIMessageHandler<?>> contextMessageHandlerQueue,GenericObjectPool<AsynIOClientMessageThread> ioThreadPool){
		this.contextMessageHandlerQueue = contextMessageHandlerQueue;
		this.requestMessageQueue = requestMessageQueue;
		this.ioThreadPool = ioThreadPool;
	}
	
	@Override
	public AsynIOClientMessageThread create() throws Exception {
		AsynIOClientMessageThread  newThread = new AsynIOClientMessageThread();
		newThread.init(provider.getAsynChannelGroup(),provider.getAsynIoConfig());
		return newThread;
	}

	@Override
	public PooledObject<AsynIOClientMessageThread> wrap(AsynIOClientMessageThread obj) {
		return new DefaultPooledObject<AsynIOClientMessageThread>(obj);
	}

	@Override
	public AsynIOClientMessageThreadFactory registerProviderMsg(AsynIOGroupProvider provider) {
		this.provider = provider;
		return this;
	}
	
	@Override
	public GenericObjectPoolConfig bindingDefaultConfig() {
	     config.setMaxTotal(100); //整个池最大值  
	     config.setBlockWhenExhausted(true);  
	     config.setMaxWaitMillis(-1); //获取不到永远等待  
	     config.setNumTestsPerEvictionRun(Integer.MAX_VALUE); // always test all idle objects  
	     config.setTestOnBorrow(true);  
	     config.setTestOnReturn(false);  
	     config.setTestWhileIdle(false);  
	     config.setTimeBetweenEvictionRunsMillis(1 * 60000L); //-1不启动。默认1min一次  
	     config.setMinEvictableIdleTimeMillis(10 * 60000L); //可发呆的时间,10mins  
	     config.setTestWhileIdle(false);  //发呆过长移除的时候是否test一下先
		 return this.config;
	}

	@Override
	public AsynIOClientJsonThreadFactory registerProviderJson(
			AsynIOGroupProvider provider) {
		return null;
	}
	
}