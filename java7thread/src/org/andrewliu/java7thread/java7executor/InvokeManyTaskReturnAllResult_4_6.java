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
 * ͨ����invokeAll()������ִ�ж���̣߳�����Future�õ������е�ִ�н��
 * ���Ե���:Future�ӿڵ�isDone()������ȷ�������Ƿ�ִ�н�����true��ʾ����.
 * ��һ�����ڵ���shutdown()������ThreadPoolExecutor���awaitTermination()�����Ὣ�߳�
 * ���ߣ�ֱ�����е�����ִ�н��������������shutdown()�����͵���awaitTermination()�������߳̽���������
 * invokeAll()��������һ�汾��
 * invokeAll(Collection<? extends Callable<T>>  tasks, long timeut,TimeUnit unit)
 * ����������ִ�����ʱ�����߳�ʱ��ʱ�������ĸ����ȷ�����,������������ر�������״̬�ͽ����Future�б�
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
			//��resultList���棬��invokeAll()ִ������������ķ��ؽ��
			resultList = executor.invokeAll(taskList);
		}catch(InterruptedException e){
			e.printStackTrace();
		}
		
		executor.shutdown();
		System.out.println("Main: Printing the resuults!");
		for ( int i = 0; i < resultList.size(); i++){
			Future<Result>  future = resultList.get(i); //�õ�ִ��Future<Result>
			System.out.println("future.isdone="+future.isDone());  //��ʱ���е������Ѿ�����
			try{
				Result result = future.get(); //��Future�еõ�ʵ�ʷ��ض���Result
				System.out.println(result.getName()+":  "+result.getValue());
			}catch(InterruptedException | ExecutionException e){
				e.printStackTrace();
			}
		}
		
		
	}
}

/**
 *��ΪFuture�еı�����
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