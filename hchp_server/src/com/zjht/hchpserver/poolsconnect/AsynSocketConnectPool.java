package com.zjht.hchpserver.poolsconnect;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.logging.Logger;

import com.zjht.hchpserver.config.AbstractConfig;
import com.zjht.hchpserver.config.ServerConfig;

public class AsynSocketConnectPool extends  AbstractAsynSocketPool<AsynchronousSocketChannel> {

	private static final Logger logger = Logger.getLogger(AsynSocketConnectPool.class.getName());
	private AsynchronousChannelGroup asyncChannelGroup;
	private ExecutorService executor = null;
	private AbstractConfig configInfo = new ServerConfig();
	private static  AbstractAsynSocketPool<AsynchronousSocketChannel>  connectPool = new AsynSocketConnectPool();
	private   AsynchronousSocketChannel connector;
	private  int  beforafter = 1;
	public static synchronized AbstractAsynSocketPool<AsynchronousSocketChannel>  getInstance(){
		return connectPool;
	}
	
	private AsynSocketConnectPool (){
		logger.info("初始化异步IO客户端Socket。。。。。。");
		executor = Executors.newFixedThreadPool(4);
		try {
			asyncChannelGroup = AsynchronousChannelGroup
					.withThreadPool(executor);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@Override
	public boolean initPool(int size) {
		this.size = size;
		checkedOut = new  boolean[size];
		available = new Semaphore(size,true);
		//初始化池中的对象   
		long  initStartTime = System.currentTimeMillis();
		for(int i = 0; i < size; ++i){
			try {
				connector  = AsynchronousSocketChannel.open(asyncChannelGroup);
				connector.connect(new InetSocketAddress(configInfo
						.getServerIp(), configInfo.getListenerPort()));
				connector.setOption(StandardSocketOptions.TCP_NODELAY,
					      true);
					      connector.setOption(StandardSocketOptions.SO_REUSEADDR,
					      true);  
					      connector.setOption(StandardSocketOptions.SO_KEEPALIVE,
					      true);
				items.add(connector);
			} catch (IOException e) {
				e.printStackTrace();
			}   
		}
		logger.info("初始化"+size+"个连接,花费时间:"+(System.currentTimeMillis() - initStartTime));
		
		 Timer timer = new Timer();  
	        timer.scheduleAtFixedRate(new TimerTask() {  
	            @Override  
	            public void run() {  
//	            	System.out.println("checkInMap.size="+checkInMap.size());
//	            	for(boolean b : checkedOut){
//	            		System.out.println("checkedOUt = "+b);
//	            	}
	            	if(beforafter%2==1){
	            	for(int j = 0; j < 100/2; j++){
	            		String key = "key"+j;
	    				AsynchronousSocketChannel  t = checkInMap.get(key);
	    				if(t!=null){
	    					checkIn(t);
	    					checkInMap.remove(key);
	    				}
	            	}
	            	}else if(beforafter%2==0){
	            		for(int j = 100/2; j < 100; j++){
		            		String key = "key"+j;
		    				AsynchronousSocketChannel  t = checkInMap.get(key);
		    				if(t!=null){
		    					checkIn(t);
		    					checkInMap.remove(key);
		    				}
		            	}
	            	}
	            	if(beforafter==100){
	            		beforafter=0;
	            	}
	            	beforafter++;
	            }  
	        }, 1, 5000);  
		   
		return true;
	}
   
	
}
