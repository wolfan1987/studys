package org.andrewliu.java7thread.test;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * Swing�е��̷߳�ջ���
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
	 * �����жϵ�ǰ�߳��Ƿ����¼��߳�
	 * @return
	 */
	public static boolean isEventDispatchThread(){
		return Thread.currentThread() == swingThread;
	}
	/**
	 * �÷������Խ�һ��Runnable������ȵ��¼��߳���ִ��(���Դ������߳���ִ��)
	 * @param task
	 */
	public static void invodeLater(Runnable task){
		exec.execute(task);
	}
	/***
	 * �÷������Խ�һ��Runnable������ȵ��¼��߳���ִ�У���������ǰ�߳�ֱ��������ɣ�ֻ�ܴӷ�GUI�߳��е���)
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
