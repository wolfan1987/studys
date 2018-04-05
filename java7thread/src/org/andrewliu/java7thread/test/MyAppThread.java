package org.andrewliu.java7thread.test;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 自定义公共线程类，由线程工厂调用，如:newThrread
 * @author de
 *
 */
public class MyAppThread  extends Thread{

	public static final String DEFAULT_NAME = "MyAppThread";
	private static volatile boolean debugLifecycle = false;
	private static final AtomicInteger created = new AtomicInteger();
	private static final AtomicInteger alive = new AtomicInteger();
	private static final Logger logger = Logger.getAnonymousLogger();
	
	public MyAppThread(Runnable runnable,String name){
		super(runnable,name+"-"+created.incrementAndGet());
		setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
			
			@Override
			public void uncaughtException(Thread t, Throwable e) {
				logger.log(Level.SEVERE, "UNCAUGHT in thread "+t.getName(),e );
			}
		});
		
	}
	
	public void run(){
		boolean debug = debugLifecycle;
		if(debug){
			logger.log(Level.FINE,"Created "+getName());
		}
		try{
			alive.incrementAndGet();
			super.run();
		}finally{
			alive.decrementAndGet();
			if(debug){
				logger.log(Level.FINE,"Exiting "+getName());
			}
		}
	}
	
	public static int getThreadsCreated(){
		return created.get();
	}
	
	public static int getThreadsAlive(){
		return alive.get();
	}
	
	public static boolean getDebug(){
		return debugLifecycle;
	}
	
	public static void setDebug(boolean b){
		debugLifecycle = b;
	}
}
