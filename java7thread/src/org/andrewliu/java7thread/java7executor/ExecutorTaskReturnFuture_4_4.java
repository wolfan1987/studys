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
 * ͨ������ʵ��jdk��Callable<T>�ӿ��е�call������
 * ����Executor.submit(task)�����ύ����ִ�к󷵻ص�Future<T>��get()�����õ�call�������صĽ��
 * 
 * @author de
 *
 */
public class ExecutorTaskReturnFuture_4_4 {

	public static void main(String[] args) {
		ThreadPoolExecutor executor = (ThreadPoolExecutor)Executors.newFixedThreadPool(2);
		List<Future<Integer>> resultList = new ArrayList<>(); //����ִ�еĽ��
		Random random = new Random();
		for (int i = 0; i < 10; i++){
			Integer number = random.nextInt(10);
			FactorialCalculator calculator = new FactorialCalculator(number);
			Future<Integer> result = executor.submit(calculator);  //ÿ�ύ��ִ����һ�����񣬾ͽ����صĽ�����浽resultList��
			resultList.add(result);
		}
		
		//��do--whileѭ�����ִ������״̬
		do{
			System.out.printf("Main: Number of Completed Tasks: %d\n", executor.getCompletedTaskCount());//�õ������������
			for ( int i = 0; i < resultList.size(); i++){
				Future<Integer>  result = resultList.get(i); //��resultList�еõ����������ӡ�Ƿ�����������ִ��
				System.out.printf("Maiin: Task %d: %s\n", i,result.isDone());  //isDone()�ж������Ƿ����
			}
			
			try{
				TimeUnit.MILLISECONDS.sleep(50);
			}catch(InterruptedException e){
				e.printStackTrace();
			}
		}while(executor.getCompletedTaskCount()<resultList.size());  //һֱ�ȵ�ȫ��ִ����
		
		System.out.printf("Main: Results\n");
		for ( int i = 0; i < resultList.size(); i++){
			Future<Integer> result = resultList.get(i);  
			Integer number = null;
			try{
				number = result.get();  //(result�Ľ�����ڵ���get()����ʱ�Ż�ִ��call����)
			}catch(InterruptedException e){
				e.printStackTrace();
			}catch(ExecutionException e){  //����get()ʱ�׳�
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