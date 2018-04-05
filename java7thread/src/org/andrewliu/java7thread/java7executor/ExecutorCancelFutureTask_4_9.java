package org.andrewliu.java7thread.java7executor;

import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 通过Future的cancel(true｜false)方法，来控制任务的取消
 * 如果任务已完成，或已被取消，或其他原因不能取消，传入true时，方法将返回false，并且任务不能取消。
 * 如果任务在等待分配线程，传入true时，任务被取消，并且不会开始执行，如果已经在运行，则任务被取消，如果传入false，则正在
 * 运行的任务不会被取消。
 * 如果Future对象所控制任务已经被取消，那么使用Future对象的get()方法时将抛出CancellationException异常.
 * @author de
 *
 */
public class ExecutorCancelFutureTask_4_9 {

	public static void main(String[] args) {
		ThreadPoolExecutor executor = (ThreadPoolExecutor)Executors.newCachedThreadPool();
		CancelFutureTask task = new CancelFutureTask();
		System.out.printf("Main: Executing the Task\n");
		//通过Future得到任务的执行结果，在这里任务永远不会结束，所有result永远不会有结果
		Future<String> result = executor.submit(task);
		
		try{
			TimeUnit.SECONDS.sleep(2);
		}catch(InterruptedException e){
			e.printStackTrace();
		}
		
		System.out.printf("Main: Canceling the Task\n");
		//将任务取消执行
		result.cancel(true);
		System.out.printf("Main: Cancelled: %s\n", result.isCancelled());
		System.out.printf("Main: Done: %s\n", result.isDone());
		executor.shutdown();
		System.out.printf("Main: The executor has finished\n");
	}
}


class CancelFutureTask implements Callable<String>{

	@Override
	public String call() throws Exception {
		//有无限循环执行时，可以不用return
		while(true){
			System.out.printf("Task: Test\n");
			Thread.sleep(100);
		}
	}
	
}