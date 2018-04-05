package org.andrewliu.java7thread.java7executor;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 通过让类实现jdk的Callable<T>接口中的call方法，
 * 并用Executor.submit(task)方法提交任务执行后返回的Future<T>的get()方法得到call方法返回的结果
 * 
 * @author de
 *
 */
public class ExecutorTaskReturnFuture_4_4 {

	public static void main(String[] args) {
		ThreadPoolExecutor executor = (ThreadPoolExecutor)Executors.newFixedThreadPool(2);
		List<Future<Integer>> resultList = new ArrayList<>(); //保护执行的结果
		Random random = new Random();
		for (int i = 0; i < 10; i++){
			Integer number = random.nextInt(10);
			FactorialCalculator calculator = new FactorialCalculator(number);
			Future<Integer> result = executor.submit(calculator);  //每提交并执行完一个任务，就将返回的结果保存到resultList中
			resultList.add(result);
		}
		
		//用do--while循环监控执行器的状态
		do{
			System.out.printf("Main: Number of Completed Tasks: %d\n", executor.getCompletedTaskCount());//得到任务完成数量
			for ( int i = 0; i < resultList.size(); i++){
				Future<Integer>  result = resultList.get(i); //从resultList中得到结果，并打印是否完成了任务的执行
				System.out.printf("Maiin: Task %d: %s\n", i,result.isDone());  //isDone()判断任务是否被完成
			}
			
			try{
				TimeUnit.MILLISECONDS.sleep(50);
			}catch(InterruptedException e){
				e.printStackTrace();
			}
		}while(executor.getCompletedTaskCount()<resultList.size());  //一直等到全部执行完
		
		System.out.printf("Main: Results\n");
		for ( int i = 0; i < resultList.size(); i++){
			Future<Integer> result = resultList.get(i);  
			Integer number = null;
			try{
				number = result.get();  //(result的结果是在调用get()方法时才会执行call方法)
			}catch(InterruptedException e){
				e.printStackTrace();
			}catch(ExecutionException e){  //调用get()时抛出
				e.printStackTrace();
			}
			System.out.printf("Main: Task %d: %d\n", i,number);
		}
		executor.shutdown();
	}
}



class FactorialCalculator implements Callable<Integer>{

	private Integer number;
	public FactorialCalculator(Integer number){
		this.number = number;
	}
	@Override
	public Integer call() throws Exception {
		int result = 1;
		if((number==0) || (number==1)){
			result=1;
		}else{
			for ( int i = 2; i<= number;i++){
				result*=i;
				TimeUnit.MILLISECONDS.sleep(20);
			}
		}
		System.out.printf("%s: %d\n", Thread.currentThread().getName(),result);
		return result;
	}
	
}