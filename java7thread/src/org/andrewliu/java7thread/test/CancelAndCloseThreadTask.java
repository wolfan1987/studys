package org.andrewliu.java7thread.test;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class CancelAndCloseThreadTask {

	
	private static final ScheduledExecutorService cancelExec = Executors.newScheduledThreadPool(3);
	/**
	 * ���ⲿ�߳��а����ж��̣߳���Ҫ��ô����
	 * @param r
	 * @param timeout
	 * @param unit
	 */
	public static void timedRun1(Runnable r,long timeout,TimeUnit unit){
		final Thread taskThread = Thread.currentThread();
		cancelExec.schedule(new Runnable(){
			@Override
			public void run() {
				 taskThread.interrupt();
			}
			
		}, timeout, unit);
		r.run();
	}
	
	/**
	 * ��ר�ŵ��߳����ж�����
	 * @param r
	 * @param timeout
	 * @param unit
	 * @throws InterruptedException 
	 */
	public static void timedRun2(final Runnable r,long timeout,TimeUnit unit) throws InterruptedException{
		
		class RethrowableTask implements Runnable{
			private volatile Throwable t;
			public void run(){
				try{
					r.run();
				}catch(Throwable t){
					this.t = t;
				}
			}
			void rethrow(){
				if(t != null){
					System.out.println(t.getCause());
				}
			}
		}
		
		
		RethrowableTask task = new RethrowableTask();
		final Thread taskThread = new Thread(task);
		taskThread.start();
		cancelExec.schedule(new Runnable(){
			public void run(){
				taskThread.interrupt();
			}
		}, timeout, unit);
		taskThread.join(unit.toMillis(timeout));
		task.rethrow();
	}
	
	/**
	 * ͨ��Future��ȡ������
	 * @param r
	 * @param timeout
	 * @param unit
	 * @throws InterruptedException 
	 */
	public  static void timedRun3(Runnable r,long timeout,TimeUnit unit) throws InterruptedException{
		Future<?>  task = cancelExec.submit(r);
		try{
			task.get(timeout, unit);
		}catch ( TimeoutException e){
			System.out.println(" ����ʱ!");
		} catch ( ExecutionException e){
			//������������׳����쳣����ô�����׳����쳣
			System.out.println(e.getCause());
		}finally{
			//��������Ѿ���������ôִ��ȡ������Ҳ�����������Ӱ�죬��������������У���ô�����ж�
			task.cancel(true);
		}
	}
	
	
}
