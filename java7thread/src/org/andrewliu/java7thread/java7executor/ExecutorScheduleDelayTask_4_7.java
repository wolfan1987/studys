package org.andrewliu.java7thread.java7executor;

import java.util.Date;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


/**
 * 在执行器中延时执行任务:
 * ScheduledThreadPoolExecutor类是ThreadPoolExecutor类的子类，它的schedule()方法可以同时接受
 * 实现了Runnable接口和Callable<T>接口的两种任务。如果在调用 了shutdown方法后还有任务书需要执行时，默认情况下这些
 * 待处理的任务仍将被执行，但可以通过setExecuteExistingDelayedTasksAfterShutdownPolicy(false)方法设置
 * ScheduledThreadPoolExecutor的行为，这样待处理的任务将不会被执行。
 * @author de
 *
 */
public class ExecutorScheduleDelayTask_4_7 {

	public static void main(String[] args) {
		//得到一个调度线程池的执行器,且只能初始化为1个大小。
		ScheduledThreadPoolExecutor executor = (ScheduledThreadPoolExecutor) Executors.newScheduledThreadPool(1);
		
		System.out.printf("Main : Starting at : %s\n", new Date());
		for ( int i = 0; i < 5; i++){
			ScheduledTask task = new ScheduledTask("Task "+i);
			//每个任务延迟1秒执行,则依次任务则延迟1-5秒
			executor.schedule(task, i+1, TimeUnit.SECONDS);
		}
		executor.shutdown();
		try{
			executor.awaitTermination(1, TimeUnit.DAYS);
		}catch(InterruptedException e){
			e.printStackTrace();
		}
		
		System.out.printf("Main: Ends at : %s\n", new Date());
	}
}


class ScheduledTask implements Callable<String> {
	private String name;

	public ScheduledTask(String name) {
		super();
		this.name = name;
	}

	@Override
	public String call() throws Exception {
		System.out.printf("%s: Starting at : %s\n", name,new Date());
		return "Hello, world";
	}
	
}