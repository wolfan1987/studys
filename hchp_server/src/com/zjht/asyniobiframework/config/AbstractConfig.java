package com.zjht.asyniobiframework.config;

/**
 * 异步I/O服务端配置信息
 * 
 * @Date  2015-8-18
 * @author AndreLiu
 *
 */
public abstract  class AbstractConfig {

	/**
	 * 服务端监听端口
	 */
	 protected int listenerPort = 9999; 
	 
	 /**
	  * 可等待链接的最大socket数
	  */
	 protected int backlog = 50;  
	  
	 /**
	  * 线程池最大线程数（即可同时并发处理业务数）
	  */
	 protected int threadPoolSize = 100;  
	
	 /**
	  * 
	  */
	 protected int initialSize = 20;
	   
	 protected String  serverIp = "localhost";

	public int getListenerPort() {
		return listenerPort;
	}

	public void setListenerPort(int listenerPort) {
		this.listenerPort = listenerPort;
	}

	public int getBacklog() {
		return backlog;
	}

	public void setBacklog(int backlog) {
		this.backlog = backlog;
	}

	public int getThreadPoolSize() {
		return threadPoolSize;
	}

	public void setThreadPoolSize(int threadPoolSize) {
		this.threadPoolSize = threadPoolSize;
	}

	public int getInitialSize() {
		return initialSize;
	}

	public void setInitialSize(int initialSize) {
		this.initialSize = initialSize;
	}

	public String getServerIp() {
		return serverIp;
	}

	public void setServerIp(String serverIp) {
		this.serverIp = serverIp;
	}  
	
	
	 
}
