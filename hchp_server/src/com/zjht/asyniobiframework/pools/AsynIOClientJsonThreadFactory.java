package com.zjht.asyniobiframework.pools;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Logger;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import com.zjht.asyniobiframework.handler.IBIJsonHandler;


public class AsynIOClientJsonThreadFactory extends BasePooledObjectFactory<AsynIOClientJsonThread> implements IAsynIORegisterBinding{

	private static final Logger logger = Logger.getLogger(AsynIOClientJsonThreadFactory.class.getName());
	private  AsynIOGroupProvider  provider = null;
	private  GenericObjectPoolConfig config =  new GenericObjectPoolConfig();
	private  ConcurrentLinkedQueue<Class<?>> jsonHandlers = null;
	private   ConcurrentLinkedQueue<String> customerJsonQueue =null;
	private  GenericObjectPool<AsynIOClientJsonThread> ioThreadPool;
	public static AsynIOClientJsonThreadFactory  newFactory(){
		return new AsynIOClientJsonThreadFactory();  
	}
	public  void   init(ConcurrentLinkedQueue<String> customerJsonQueue, ConcurrentLinkedQueue<Class<?>> jsonHandlers,GenericObjectPool<AsynIOClientJsonThread> ioThreadPool){
		this.customerJsonQueue = customerJsonQueue;
		this.jsonHandlers = jsonHandlers;
		this.ioThreadPool = ioThreadPool;
	}
	@Override
	public AsynIOClientJsonThread create() throws Exception {
		AsynIOClientJsonThread  newThread = new AsynIOClientJsonThread();
		newThread.init(provider.getAsynChannelGroup(),provider.getAsynIoConfig());  
		newThread.setJsonHandlersQueue(jsonHandlers, customerJsonQueue).setThreadPool(ioThreadPool);
		return newThread;
	}

	@Override
	public PooledObject<AsynIOClientJsonThread> wrap(AsynIOClientJsonThread obj) {
		return new DefaultPooledObject<AsynIOClientJsonThread>(obj);
	}

	@Override
	public AsynIOClientJsonThreadFactory registerProviderJson(AsynIOGroupProvider provider) {
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
	public AsynIOClientMessageThreadFactory registerProviderMsg(
			AsynIOGroupProvider provider) {
		return null;
	}
}
