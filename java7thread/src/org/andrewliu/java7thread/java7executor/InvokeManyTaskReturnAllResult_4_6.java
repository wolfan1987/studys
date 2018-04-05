package org.andrewliu.java7thread.java7executor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * 通过：invokeAll()方法，执行多个线程，并用Future得到任务中的执行结果
 * 可以调用:Future接口的isDone()方法来确定任务是否执行结束，true表示结束.
 * 另一种是在调用shutdown()方法后，ThreadPoolExecutor类的awaitTermination()方法会将线程
 * 休眠，直到所有的任务执行结束。如果不调用shutdown()方法就调用awaitTermination()方法，线程将立即返回
 * invokeAll()方法的另一版本：
 * invokeAll(Collection<? extends Callable<T>>  tasks, long timeut,TimeUnit unit)
 * 当所有任务执行完成时，或者超时的时候（无论哪个首先发生）,这个方法将返回保持任务状态和结果的Future列表。
 * @author de
 *
 */
public class InvokeManyTaskReturnAllResult_4_6 {

	public static void main(String[] args) {
		ExecutorService executor = Executors.newCachedThreadPool();
		List<AllResultTask>  taskList = new ArrayList<>();
		for ( int i = 0; i < 3; i++){
			AllResultTask task = new AllResultTask(i+"");
			taskList.add(task);
		}
		List<Future<Result>> resultList = null;
		try{
			//用resultList保存，用invokeAll()执行完所有任务的返回结果
			resultList = executor.invokeAll(taskList);
		}catch(InterruptedException e){
			e.printStackTrace();
		}
		
		executor.shutdown();
		System.out.println("Main: Printing the resuults!");
		for ( int i = 0; i < resultList.size(); i++){
			Future<Result>  future = resultList.get(i); //得到执行Future<Result>
			System.out.println("future.isdone="+future.isDone());  //此时所有的任务都已经结束
			try{
				Result result = future.get(); //从Future中得到实际返回对象Result
				System.out.println(result.getName()+":  "+result.getValue());
			}catch(InterruptedException | ExecutionException e){
				e.printStackTrace();
			}
		}
		
		
	}
}

/**
 *作为Future中的保存结果
 * @author de
 *
 */
class Result{
	private String name;
	private int value;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}
	
}

class AllResultTask implements Callable<Result>{
	private String name;
	public AllResultTask (String name){
		this.name = name;
	}
	@Override
	public Result call() throws Exception {
		System.out.printf("%s: Staring\n", this.name);
		try{
			long duration = (long)(Math.random()*10);
			System.out.printf("%s: Waiting %d seconds for results.\n", this.name,duration);
			TimeUnit.SECONDS.sleep(duration);
		}catch(InterruptedException e){
			e.printStackTrace();
		}
		int value = 0;
		for ( int i = 0; i < 5; i++){
			value+=(int)(Math.random()*100);
		}
		Result result = new Result();
		result.setName(this.name);
		result.setValue(value);
		System.out.println(this.name+": Ends");
		return  result;
	}
	
	
}