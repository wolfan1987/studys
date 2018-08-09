package com.zjht.asyniobiframework.pools;

import java.io.IOException;
import java.nio.channels.AsynchronousChannelGroup;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zjht.asyniobiframework.config.AbstractConfig;
import com.zjht.asyniobiframework.config.AsynIoClientConfig;
import com.zjht.asyniobiframework.config.ServerSocketConfig1;

/**
 * 提供异步IO组对象
 * 
 * @author de
 * 
 */
public class AsynIOGroupProvider {

	private static final Logger logger = LoggerFactory
			.getLogger(AsynIOGroupProvider.class);

	private AsynchronousChannelGroup asyncChannelGroup;
	private ExecutorService executorService = null;
	private  AsynIoClientConfig    asynIoConfig = null;
	private  GenericObjectPoolConfig config  = new GenericObjectPoolConfig();

	public AsynIOGroupProvider(AsynIoClientConfig    asynIoConfig) {
		this.asynIoConfig = asynIoConfig;
	}

	public AsynchronousChannelGroup getAsynChannelGroup() {
		try {
			executorService = Executors.newFixedThreadPool(asynIoConfig.getAsynIoGroupThreadNum());
			asyncChannelGroup = AsynchronousChannelGroup
					.withThreadPool(executorService);
			logger.debug("------初始化异步IO信道组线程数为{}个，初始化成功。-------", asynIoConfig.getAsynIoGroupThreadNum());
		} catch (IOException e) {
			logger.debug(
					"------初始化异步IO信道组线程数为{}个，初始化失败。-------" + e.getMessage(),
					asynIoConfig.getAsynIoGroupThreadNum());
			e.printStackTrace();
		}
		return asyncChannelGroup;
	}
	
	public AsynIoClientConfig  getAsynIoConfig(){
		return asynIoConfig;
	}
	
	public GenericObjectPoolConfig  getPoolConfig(String propertiesFilePath){
		System.out.println("未实现按配置文件初始化对象池！..................");
		return config;
	}
}
