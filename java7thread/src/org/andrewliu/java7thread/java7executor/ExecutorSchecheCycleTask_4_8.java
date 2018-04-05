package org.andrewliu.java7thread.java7executor;

import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;


/**
 * 用ScheduledThreadPoolExecutor来周期性的执行任务,它有两个方法来周期性的执行任务，并有一个包装返回结果的ScheduledFuture类(扩展于Future接口)
 * scheduledAtFixedRate(task,firstExeDelayTime,nextDelay,TimeUnit);如: scheduledAtFixedRate(task,2,3,TimeUnit.SECONDS)
 * 即：task在延迟2秒后第一次执行，以后执行时间隔为上一次执行开始时间开始累计间隔3秒。以上次执行开始时间为计算点
 * scheduledWithFixedDelay(task,firstExeDelayTime,nextDelay,TimeUnit):如 :scheduledWithFixedDelay(task,3,4,TimeUnit.SECONDS)
 * 即：task在延迟3秒第一次执行，以后执行时间间隔为上一次执行结束时间开始累计间隔4秒.以上次执行结束时间为计算点
 * ScheduledThreadPoolExecutor实现的shutdown()方法的默认行为是调用后，定时任务就结束了。但可以通过ScheduledThreadPoolExecutor的
 * setContinueExistingPeriodicTasksAfterShutdownPolicy()方法来改变这个行为，传递参数true给这个方法，即使调用shutdown()方法后，
 * 周期性任务仍将继续.
 * @author de
 *
 */
public class ExecutorSchecheCycleTask_4_8 {

	public static void main(String[] args) {
		//得到一个ScheduleExecutorService
		ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
		System.out.printf("Main: Starting at : %s\n", new Date());
		ScheduledCycleTask  task = new ScheduledCycleTask("ScheduledCycleTask");
		//让任务在开始1秒后执行，以后每隔两秒执行一次(间隔以起始时间来计算),这里是实现Runnable方法，所有返回结果为不可知，
		ScheduledFuture<?>  result = executor.scheduleAtFixedRate(task, 1, 2, TimeUnit.SECONDS);
		for ( int i = 0; i < 10; i++){
			System.out.printf("Main: Delay: %d\n", result.getDelay(TimeUnit.MILLISECONDS));
			try{
				TimeUnit.MILLISECONDS.sleep(500);
			}catch(InterruptedException e){
				e.printStackTrace();
			}
		}
		
		executor.shutdown();
		try{
			TimeUnit.SECONDS.sleep(5);
		}catch(InterruptedException e){
			e.printStackTrace();
		}
		
		System.out.printf("Main: Finished at : %s\n", new Date());
	}
}


class ScheduledCycleTask implements Runnable{

	private String name;
	public ScheduledCycleTask(String name){
		this.name = name;
	}
	@Override
	public void run() {
		System.out.printf("%s : Starting at : %s\n", name,new Date());
	}
	
}