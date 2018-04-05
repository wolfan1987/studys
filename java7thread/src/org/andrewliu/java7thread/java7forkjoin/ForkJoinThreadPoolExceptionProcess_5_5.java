package org.andrewliu.java7thread.java7forkjoin;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.TimeUnit;

/**
 * ʹ��Fork/Joion���ʱ����������������г��ֵ�����ʱ�쳣.
 * ForkJoinTask���compute()�����޷��׳�������ʱ�쳣����Ϊ��û��throwsʵ�֡���compute()���������׳�����ʱ�쳣�����
 * �쳣���׳�����ô��ֻ�ܼ򵥵Ľ��쳣�Ե����������ܹ�����ForkJoinTask���һЩ��������֪����
 * �Ƿ����쳣�׳����Լ��׳���һ�����͵��쳣.
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
		
		if(task.isCompletedAbnormally()){  //����������������֮һ�Ƿ��׳����쳣��������ForkJoinTask���getException()������ȡ�쳣.
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
				//��һ���׳��쳣�ķ�ʽ,���ַ�ʽ��throw new ��ʽЧ��һ��
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