package com.zjht.asyniobiframework.config;

/**
 * 异步客户端上下文初始化IO组，线程池，服务端相关端口
 * @author de
 *
 */
public class AsynIoClientConfig extends AbstractConfig{
	/**
	 * 异步IO组线程数
	 */
	private  int  asynIoGroupThreadNum;
	
	public  AsynIoClientConfig(int asynIoGroupThreadNum){
		this.asynIoGroupThreadNum = asynIoGroupThreadNum;
	}
	public  AsynIoClientConfig  (String serverIp,int serverPort,int asynIoGroupThreadNum,int threadPoolSize){
		this.asynIoGroupThreadNum = asynIoGroupThreadNum;
		this.listenerPort = serverPort;
		this.serverIp = serverIp;
		this.threadPoolSize = threadPoolSize;
	}
	
	

	public int getAsynIoGroupThreadNum() {
		return asynIoGroupThreadNum;
	}

	public void setAsynIoGroupThreadNum(int asynIoGroupThreadNum) {
		this.asynIoGroupThreadNum = asynIoGroupThreadNum;
	}
	
	
	
}
