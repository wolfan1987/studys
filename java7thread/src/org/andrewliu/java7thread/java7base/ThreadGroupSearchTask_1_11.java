package org.andrewliu.java7thread.java7base;

import java.util.Date;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * 线程组允许把一个组的线程当成一个单一的单元，可对组内线程对象进行访问并操作它们（可批量控制）
 * ThreadGroup类表示一组线程，线程组可以包含线程对象，也可以包含其它线程组对象，它是一个树形结构.
 * @author de
 *
 */
public class ThreadGroupSearchTask_1_11  implements Runnable{

	private Result result;
	public ThreadGroupSearchTask_1_11(Result result){
		this.result = result;
	}
	
	@Override
	public void run() {
		String name = Thread.currentThread().getName();
		System.out.printf("Thread %s: Start\n", name);
		try{
		doTask();
		result.setName(name);
		}catch(InterruptedException e){
			System.out.printf("Thread %s: Interrupted\n",name);
			return;
		}
		System.out.printf("Thread %s: End\n", name);
	}
	
	private void doTask() throws InterruptedException{
		Random random = new Random((new Date()).getTime());
		int value = (int)(random.nextDouble()*100);
		System.out.printf("Thread  %s: %d\n", Thread.currentThread().getName(),value);
		TimeUnit.SECONDS.sleep(value);
	}
	
	
	//线程组测试
	public static void main(String[] args) {
		//声明一个name为Searcher的线程组
		ThreadGroup threadGroup = new ThreadGroup("Searcher");
		Result result = new Result();
		ThreadGroupSearchTask_1_11  searchTask = new ThreadGroupSearchTask_1_11(result);
		for(int i = 0; i < 5; i++){
			//将任务与线程组与线程关联，循环中的5个线程它们共同关联了任务searchTask，且他们在同一个线程组(searcher)
			Thread thread = new Thread(threadGroup,searchTask);
			thread.start();
			try {
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		/***打印线程组相关信息***/
		System.out.printf("Number of Threads: %d\n", threadGroup.activeCount());//打印线程组中线程数目
		System.out.printf("Information about the Thread Group\n");
		threadGroup.list();  //打印线程组对象信息
		Thread[]  threads = new Thread[threadGroup.activeCount()];//定义保存线程组中线程对象的数组
		threadGroup.enumerate(threads);//获取线程组包含的线程列表
		for ( int i = 0; i < threadGroup.activeCount();i++){
			System.out.printf("Thread: %s: %s\n", threads[i].getName(),threads[i].getState());
		}
		
		waitFinish(threadGroup);
	}
	
	
	private static void waitFinish(ThreadGroup threadGroup){
		while(threadGroup.activeCount()>9){
			try{
			TimeUnit.SECONDS.sleep(1);
			}catch(InterruptedException e){
				e.printStackTrace();
			}
		}
	}
	

}
class Result{
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
