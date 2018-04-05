package org.andrewliu.java7thread.java7forkjoin;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.TimeUnit;

/**
 * 通过ForkJoinTask的cancel()方法来取消任务.
 * 注意：
 * ForkJoinPool类不提供任务方法来取消线程池中正在运行或者等待运行的所有任务。
 * 取消任务时，不能取消已经被执行的任务。
 * 用例：寻找可以被取消的剩余任务数，用一个辅助类来实现取消任务操作。
 * 
 * @author de
 *
 */
public class ForkJoinThreadPoolCancelTask_5_6 {

	public static void main(String[] args) {
		ArrayGenerator generator  = new ArrayGenerator();
		int array[] = generator.generateArray(1000);
		TaskManager manager = new TaskManager();  //
		ForkJoinPool pool = new ForkJoinPool();
		SearchNumberTask  task =  new SearchNumberTask(array, 0, 1000, 5, manager);
		pool.execute(task);
		pool.shutdown();
		try{
			pool.awaitTermination(1, TimeUnit.DAYS);
		}catch(InterruptedException e){
			e.printStackTrace();
		}
		
		System.out.printf("Main: The program has finished\n");
	}
}

class ArrayGenerator{
	public int[] generateArray(int size){
		int array[] = new int[size];
		Random random = new Random();
		for ( int i = 0 ; i < size; i++){
			array[i] = random.nextInt(10);
		}
		return array;
	}
}

/**
 * 任务管理辅助类,将所有发送到线程池的任务保存起来
 * @author de
 *
 */
class TaskManager{
	
	private List<ForkJoinTask<Integer>> tasks ;
	public TaskManager(){
		tasks = new ArrayList<>();
	}
	
	public void addTask(ForkJoinTask<Integer> task){
		tasks.add(task);
	}
	//取消所有任务，
	public void cancelTasks(ForkJoinTask<Integer> cancelTask){
		for ( ForkJoinTask<Integer> task : tasks){
			if(task != cancelTask){
				task.cancel(true);  //如果是非当前任务就取消
				((SearchNumberTask)task).writeCancelMessage();
			}
		}
	}
}

class SearchNumberTask extends RecursiveTask<Integer>{

	private static final long serialVersionUID = 3424963508140295858L;

	
	private int numbers[];
	private int start,end;
	private int number;
	private TaskManager manager;
	private final static int NOT_FOUND = -1;
	
	public SearchNumberTask(int[] numbers, int start, int end, int number,
			TaskManager manager) {
		super();
		this.numbers = numbers;
		this.start = start;
		this.end = end;
		this.number = number;
		this.manager = manager;
	}

	@Override
	protected Integer compute() {
		System.out.println("Task: "+ start+":"+end);
		int ret;
		if(end - start>10){ //大于10时，多任务查找 
			ret = launchTasks();
		}else{
			ret = lookForNumber();  //否则一个方法查找
		}
		return ret;
	}
	
	private int lookForNumber(){
		for ( int i = start; i < end; i++){
			if(numbers[i] == number){
				System.out.printf("Task: Number %d found in position %d\n",number);
				manager.cancelTasks(this);  //找到了数字，就把其它任务全部取消
				return i;
			}
			try{
				TimeUnit.SECONDS.sleep(1);
			}catch(InterruptedException e){
				e.printStackTrace();
			}
		}
		
		return NOT_FOUND;
	}
	
	private int launchTasks(){
		int mid = (start+end)/2;
		SearchNumberTask task1 = new SearchNumberTask(numbers, start, mid, number, manager);
		SearchNumberTask task2 = new SearchNumberTask(numbers, mid, end, number, manager);
		manager.addTask(task1);
		manager.addTask(task2);
		task1.fork(); //异步执行这两个任务
		task2.fork(); 
		
		int returnValue;
		returnValue = task1.join();  //获得执行后的返回结果
		if ( returnValue != -1){
			return returnValue;
		}
		returnValue   = task2.join();   //获得执行后的返回结果
		return returnValue;
	}
	
	public void writeCancelMessage(){
		System.out.printf("Task: Cancelled task from %d to %d", start,end);
	}
}
