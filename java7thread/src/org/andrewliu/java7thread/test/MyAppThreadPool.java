package org.andrewliu.java7thread.test;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

/**
 * ��չ�̳߳أ�ThreadPoolExecutor�����Ը�д����������beforeExecute,afterExecute,terminated
 * �ֱ��ʾִ��ǰ��ִ�к�ִ�н���������Ӧ�ķ��������ǿ����������־��¼��ͳ����Ϣ�ռ���
 * @author de
 *
 */
public class MyAppThreadPool extends ThreadPoolExecutor{

	private final ThreadLocal<Long> startTime = new ThreadLocal<Long>();
	private final Logger logger = Logger.getLogger("MyAppThreadPool");
	private final AtomicLong numTasks = new AtomicLong();
	private final AtomicLong totalTime = new AtomicLong();
	
	public MyAppThreadPool(int corePoolSize, int maximumPoolSize,
			long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
		super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
	}
	
	protected void beforeExecute(Thread t,Runnable r){
		super.beforeExecute(t, r);
		logger.fine(String.format("Thread %s: start &s", t,r));
		startTime.set(System.nanoTime());
	}
	
	protected void afterExecute(Runnable r,Throwable t){
		try{
			long endTime = System.nanoTime();
			long taskTime = endTime - startTime.get();
			numTasks.incrementAndGet();
			totalTime.addAndGet(taskTime);
			logger.fine(String.format("Thread %s: end %s,time = %dns", t,r,taskTime));
		}finally{
			super.afterExecute(r, t);
		}
	}
	
	
	protected void terminated(){
		try{
			logger.info(String.format("Terminated: avg time=%dns", totalTime.get()/numTasks.get()));
		}finally{
			super.terminated();
		}
	}
	

}
