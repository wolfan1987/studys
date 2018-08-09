package com.zjht.hchpserver.poolsthread;

import java.io.IOException;
import java.nio.channels.AsynchronousChannelGroup;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.logging.Logger;

import com.zjht.hchpserver.config.AbstractConfig;
import com.zjht.hchpserver.config.ServerConfig;

public class AsynSocketChannelThreadPool extends  AbstractAsynSocketChannelThreadPool<AsynSocketChannelThread> {

	private static final Logger logger = Logger.getLogger(AsynSocketChannelThreadPool.class.getName());
	private AsynchronousChannelGroup asyncChannelGroup;
	private ExecutorService executorService = null;
	private AbstractConfig configInfo = new ServerConfig();
	private static  AbstractAsynSocketChannelThreadPool<AsynSocketChannelThread>  connectPool = new AsynSocketChannelThreadPool();
	private   AsynSocketChannelThread channelThread;
	private  Executor	executor = Executors.newFixedThreadPool(100);
	private  int  beforafter = 1;
	public static synchronized AbstractAsynSocketChannelThreadPool<AsynSocketChannelThread>  getInstance(){
		return connectPool;
	}
	
	private AsynSocketChannelThreadPool (){
		logger.info("初始化异步IO客户端Socket。。。。。。");
		executorService = Executors.newFixedThreadPool(8);
		try {
			asyncChannelGroup = AsynchronousChannelGroup
					.withThreadPool(executorService);
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
				channelThread = new AsynSocketChannelThread(asyncChannelGroup,configInfo);
				executor.execute(channelThread);
				items.add(channelThread);
		}
		logger.info("初始化"+size+"个连接,花费时间:"+(System.currentTimeMillis() - initStartTime));
		
//		 Timer timer = new Timer();  
//	        timer.scheduleAtFixedRate(new TimerTask() {  
//	            @Override  
//	            public void run() {  
////	            	System.out.println("checkInMap.size="+checkInMap.size());
////	            	for(boolean b : checkedOut){
////	            		System.out.println("checkedOUt = "+b);
////	            	}
//	            	if(beforafter%2==1){
//	            	for(int j = 0; j < 100/2; j++){
//	            		String key = "key"+j;
//	            		AsynSocketChannelThread  t = checkInMap.get(key);
//	    				if(t!=null){
//	    					checkIn(t);
//	    					checkInMap.remove(key);
//	    				}
//	            	}
//	            	}else if(beforafter%2==0){
//	            		for(int j = 100/2; j < 100; j++){
//		            		String key = "key"+j;
//		            		AsynSocketChannelThread  t = checkInMap.get(key);
//		    				if(t!=null){
//		    					checkIn(t);
//		    					checkInMap.remove(key);
//		    				}  
//		            	}
//	            	}
//	            	if(beforafter==100){
//	            		beforafter=0;
//	            	}
//	            	beforafter++;
//	            }  
//	        }, 1, 4000);  
		   
		return true;
	}



	@Override
	public Executor getExecutor() {
		return executor;
	}
	
	
}
