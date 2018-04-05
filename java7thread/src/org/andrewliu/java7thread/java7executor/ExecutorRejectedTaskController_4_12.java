package org.andrewliu.java7thread.java7executor;

import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


/**
 * 用RejectedExecutionHandler接口，处理执行器被拒绝的任务
 * 在shutdow方法与执行器结束之间发送一个任务给执行器，这个任务会被拒绝，因为这个时间段执行器已不再接受任务。解决方法是：
 * 自己实现任务拒绝处理器接口：RejectedExecutionHandler，然后让将其设为执行器的任务拒绝处理器。
 * 这个接口中有一个方法:rejectedExecution()方法，其中有两个参数：
 * 一个是Runnable对象，用来存储被拒绝的任务；一个是Executor对象，用来存储任务被拒绝的执行器.
 * 被执行器拒绝的每一个任务都将调用这个方法，前提时执行器类有调用setRejectedExecutionHandler()方法来设置用于被拒绝的任务的处理程序。
 * Executor在接受一个任务时，会先看是否调用了shutdown() 方法，如果有调用，就拒绝此任务，拒绝时他会先寻找通过setRejectedExecutionHandler()方法
 * 设置的用于被拒绝的任务的处理程序。如果有找到一个处理程序，执行器就调用 rejectedExecution()方法，否则就抛出RejecedExecutionException异常。
 * 其是一个运行时异常，不要用catch处理。
 * @author de
 *
 */
public class ExecutorRejectedTaskController_4_12 {

	public static void main(String[] args) {
		RejectedTaskController controller = new RejectedTaskController();  //实例化一个拒绝任务处理器
		ThreadPoolExecutor executor = (ThreadPoolExecutor)Executors.newCachedThreadPool();
		executor.setRejectedExecutionHandler(controller);  //设置任务拒绝处理器
		System.out.printf("Main: Starting.\n");
		for ( int i = 0; i < 3; i++){
			RejectedTask task = new RejectedTask("RejectedTask"+i);
			executor.submit(task);
		}
		
		System.out.printf("Main: Shutting down the Executor.\n");
		executor.shutdown();
		System.out.printf("Main: Sending another Task.\n");
		RejectedTask  rejectedTask = new RejectedTask("RejectedTask");
		executor.submit(rejectedTask);
		System.out.println("Main : End");
	}
   
}

class RejectedTaskController implements RejectedExecutionHandler{
	
	@Override
	public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
		System.out.printf("RejectedTaskController: The task %s has been rejected\n",r.toString());
		System.out.printf("RejectedTaskController: %s\n",executor.toString());
		System.out.printf("RejectedTaskController: Terminating: %s\n", executor.isTerminating());
		System.out.printf("RejectedTaskController: Terminated: %s\n", executor.isTerminated());
		
	}
	
}

class RejectedTask implements Runnable{
	private String name;
	public RejectedTask(String name){
		this.name = name;
	}
	@Override
	public void run() {
		System.out.println("RejectedTask" + name+": Starting");
		try{
			long duration = (long)(Math.random()*10);
			System.out.printf("RejectedTask %s: ReportGenerator : Generating a report during %d seconds\n", name,duration);
			TimeUnit.SECONDS.sleep(duration);
		}catch(InterruptedException e){
			e.printStackTrace();
		}
		System.out.printf("RejectedTask %s: Ending\n", name);
	}
	
	public String toString(){
		return name;
	}
}