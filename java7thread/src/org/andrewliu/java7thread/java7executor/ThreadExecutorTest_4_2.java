package org.andrewliu.java7thread.java7executor;

import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Executor  Framework(线程执行器框架），它有两个节点：Executor及Executor的子接口ExecutorService
 * ThreadPoolExecutor类实现了这两个接口.
 * 这套机制分离了任务的创建和执行，仅需将实现了Runnable接口的对象 ，发送给执行器执行即可,它负责Runnable对象的创建、实例化、运行
 * 里面包含线程池技术来提高程序性能，执行器会从池中拿线程来执行任务，避免了不断地创建和销毁线程而导致系统性能下降。
 * 执行器中有一个类似于:Runnable的接口:Callable。它个接口的主方法名为：call，可以返回结果，当发送一个Callable对象给
 * 执行器时，将获得一个实现了Future接口的对象，可以使用这个对象来控制Callable对象的状态和结果.
 * 注意：缓存线程池也有缺点，如果发送过多的任务给执行器，系统的负荷将会过载，因此仅当线程的数量是合理的或者线程只会运行很短
 * 的时间时，适合采用Executors工厂类的newCachedThreadPool()方法来创建执行器。
 * @author de
 * 用执行器来执行任务
 */
public class ThreadExecutorTest_4_2 {

	public static void main(String[] args) {
		ExecutorServer  server = new ExecutorServer();  //实例化服务端
		for ( int i = 0; i < 100; i++){ 
			ExecutorTask task = new ExecutorTask("Task"+i); //实例化任务
			server.executeTask(task); //提交任务到服务端执行任务
		}
		
		server.endServer(); //结束执行器(等所有任务执行完后)
	}
	
}

/**
 * 任务对象
 * @author de
 *
 */
class ExecutorTask implements Runnable{
	private Date initDate;
	private String name;
	public ExecutorTask(String name){
		initDate = new Date();
		this.name = name;
	}
	@Override
	public void run() {
		System.out.printf("%s: Task %s: Created on : %s\n", Thread.currentThread().getName(),name,initDate);
		System.out.printf("%s: Task %s: Started on: %s \n", Thread.currentThread().getName(),name,new Date());
		try{
			Long duration = (long)(Math.random()*10);
			System.out.printf("$s: Task %s: Doing a task during %d seconds \n", Thread.currentThread().getName(),name,duration);
			TimeUnit.SECONDS.sleep(duration);
		}catch(InterruptedException e){
			e.printStackTrace();
		}
		
		System.out.printf("%s: Task %s: Finished on : %s\n", Thread.currentThread().getName(),name,new Date());
	}
	
}

/**
 * 服务器端： 用执行器实现
 * @author de
 *
 */
class ExecutorServer {
	
	private ThreadPoolExecutor executor;
	public  ExecutorServer(){
		//创建不定大小的缓存线程池
		executor = (ThreadPoolExecutor)Executors.newCachedThreadPool();
		//创建固定个数线程的线程迟池
		//executor = (ThreadPoolExecutor)Executors.newFixedThreadPool(5);
		//创建只能一次执行一个任务的执行器
		//executor = (ThreadPoolExecutor)Executors.newSingleThreadExecutor();
	}
	
	public void executeTask(ExecutorTask task){
		System.out.printf("Server: A new task has arrived\n");
		executor.execute(task);//执行任务
		System.out.printf("Server: pool Size: %d\n", executor.getPoolSize());  //线程池大小
		System.out.printf("Server: Active Count: %d\n", executor.getActiveCount()); //当前活动的线程
		System.out.printf("Server: Completed Tasks: %d\n", executor.getCompletedTaskCount()); //已经完成的线程
		
		//System.out.printf("Server: Task Count: %d\n", executor.getTaskCount());  在创建固定个数线程时用：用于显示得到当前有多少个任务已经发送给执行器
	}
	
	public void endServer(){
		executor.shutdown();
	}
	
	
}
