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
	 * 在外部线程中安排中断线程（不要这么做）
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
	 * 在专门的线程中中断任务
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
	 * 通过Future来取消任务
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
			System.out.println(" 任务超时!");
		} catch ( ExecutionException e){
			//如果在任务中抛出了异常，那么重新抛出该异常
			System.out.println(e.getCause());
		}finally{
			//如果任务已经结束，那么执行取消操作也不会带来任务影响，如果任务正在运行，那么将被中断
			task.cancel(true);
		}
	}
	
	
}
