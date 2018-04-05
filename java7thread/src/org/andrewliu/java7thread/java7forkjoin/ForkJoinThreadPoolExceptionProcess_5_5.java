package org.andrewliu.java7thread.java7forkjoin;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.TimeUnit;

/**
 * 使用Fork/Joion框架时，如果处理在任务中出现的运行时异常.
 * ForkJoinTask类的compute()方法无法抛出蜚运行时异常，因为其没有throws实现。但compute()方法可以抛出运行时异常，如果
 * 异常不抛出，那么它只能简单的将异常吃掉，便我们能够利用ForkJoinTask类的一些方法来获知任务
 * 是否有异常抛出，以及抛出哪一种类型的异常.
 * @author de
 *
 */
public class ForkJoinThreadPoolExceptionProcess_5_5 {

	public static void main(String[] args) {
		int array[] = new int[100];
		ExceptionTask task =  new ExceptionTask(array,0,100);
		ForkJoinPool pool = new ForkJoinPool();
		pool.execute(task);
		pool.shutdown();
		
		try{
			pool.awaitTermination(1, TimeUnit.DAYS);
		}catch(InterruptedException e){
			e.printStackTrace();
		}
		
		if(task.isCompletedAbnormally()){  //检查主任务或子任务之一是否抛出了异常。可以用ForkJoinTask类的getException()方法获取异常.
			System.out.printf("Main: An exception has ocurred\n");
			System.out.printf("Main: %s\n",task.getException());
		}
		System.out.printf("Main: Result: %d ", task.join());
	}
}


class ExceptionTask extends RecursiveTask<Integer>{
	  
	private static final long serialVersionUID = -5034487746163697124L;
	private int array[];
	private int start,end;
	public ExceptionTask(int[] array, int start, int end) {
		super();
		this.array = array;
		this.start = start;
		this.end = end;
	}
	
	
	
	@Override
	protected Integer compute() {
		
		System.out.printf("Task: Start form %d to %d\n", start,end);
		if(end - start < 10){
			if((3>start)&&(3<end)){
				//另一种抛出异常的方式,这种方式与throw new 形式效果一样
				RuntimeException re = new RuntimeException("this task throws and Exception:"+ "Task"+ start + " to "+end);
				completeExceptionally(re);
			//	throw new RuntimeException("This task throws and Exception : Task from "+ start+"  to "+ end);
			}
			try{
				TimeUnit.SECONDS.sleep(1);
			}catch(InterruptedException e){
				e.printStackTrace();
			}
		}else{
			int mid = (end+start)/2	;
			ExceptionTask  task1 = new ExceptionTask(array, start, mid);
			ExceptionTask task2 = new ExceptionTask(array,mid,end);
			invokeAll(task1,task2);
		}
		
		System.out.printf("Task: End form %d to %d\n", start,end);
		return 0;
	}
	
	
	
	
	
}