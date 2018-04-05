package org.andrewliu.java7thread.test;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * Swing中的线程封闭机制
 * @author de
 *
 */
public class SwingUtilities {
	
	private static final ExecutorService exec = Executors.newSingleThreadExecutor(new SwingThreadFactory());
	private static volatile Thread swingThread;
	private static class SwingThreadFactory implements ThreadFactory{
		@Override
		public Thread newThread(Runnable r) {
			swingThread = new Thread(r);
			return swingThread;
		}
		
	}
	/**
	 * 用于判断当前线程是否是事件线程
	 * @return
	 */
	public static boolean isEventDispatchThread(){
		return Thread.currentThread() == swingThread;
	}
	/**
	 * 该方法可以将一个Runnable任务调度到事件线程中执行(可以从任意线程中执行)
	 * @param task
	 */
	public static void invodeLater(Runnable task){
		exec.execute(task);
	}
	/***
	 * 该方法可以将一个Runnable任务调度到事件线程中执行，并阻塞当前线程直到任务完成（只能从非GUI线程中调用)
	 * @param task
	 * @throws InterruptedException
	 * @throws InvocationTargetException
	 */
	public static void invodeAndWait(Runnable task) throws InterruptedException, InvocationTargetException{
		java.util.concurrent.Future<?> f = exec.submit(task);
		try{
			f.get();
		}catch(ExecutionException e){
			throw new InvocationTargetException(e);
		}
	}

}
