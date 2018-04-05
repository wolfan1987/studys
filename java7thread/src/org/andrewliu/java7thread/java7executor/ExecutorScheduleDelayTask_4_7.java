package org.andrewliu.java7thread.java7executor;

import java.util.Date;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


/**
 * ��ִ��������ʱִ������:
 * ScheduledThreadPoolExecutor����ThreadPoolExecutor������࣬����schedule()��������ͬʱ����
 * ʵ����Runnable�ӿں�Callable<T>�ӿڵ�������������ڵ��� ��shutdown����������������Ҫִ��ʱ��Ĭ���������Щ
 * ������������Խ���ִ�У�������ͨ��setExecuteExistingDelayedTasksAfterShutdownPolicy(false)��������
 * ScheduledThreadPoolExecutor����Ϊ����������������񽫲��ᱻִ�С�
 * @author de
 *
 */
public class ExecutorScheduleDelayTask_4_7 {

	public static void main(String[] args) {
		//�õ�һ�������̳߳ص�ִ����,��ֻ�ܳ�ʼ��Ϊ1����С��
		ScheduledThreadPoolExecutor executor = (ScheduledThreadPoolExecutor) Executors.newScheduledThreadPool(1);
		
		System.out.printf("Main : Starting at : %s\n", new Date());
		for ( int i = 0; i < 5; i++){
			ScheduledTask task = new ScheduledTask("Task "+i);
			//ÿ�������ӳ�1��ִ��,�������������ӳ�1-5��
			executor.schedule(task, i+1, TimeUnit.SECONDS);
		}
		executor.shutdown();
		try{
			executor.awaitTermination(1, TimeUnit.DAYS);
		}catch(InterruptedException e){
			e.printStackTrace();
		}
		
		System.out.printf("Main: Ends at : %s\n", new Date());
	}
}


class ScheduledTask implements Callable<String> {
	private String name;

	public ScheduledTask(String name) {
		super();
		this.name = name;
	}

	@Override
	public String call() throws Exception {
		System.out.printf("%s: Starting at : %s\n", name,new Date());
		return "Hello, world";
	}
	
}