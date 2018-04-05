package org.andrewliu.java7thread.java7executor;

import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;


/**
 * ��ScheduledThreadPoolExecutor�������Ե�ִ������,�������������������Ե�ִ�����񣬲���һ����װ���ؽ����ScheduledFuture��(��չ��Future�ӿ�)
 * scheduledAtFixedRate(task,firstExeDelayTime,nextDelay,TimeUnit);��: scheduledAtFixedRate(task,2,3,TimeUnit.SECONDS)
 * ����task���ӳ�2����һ��ִ�У��Ժ�ִ��ʱ���Ϊ��һ��ִ�п�ʼʱ�俪ʼ�ۼƼ��3�롣���ϴ�ִ�п�ʼʱ��Ϊ�����
 * scheduledWithFixedDelay(task,firstExeDelayTime,nextDelay,TimeUnit):�� :scheduledWithFixedDelay(task,3,4,TimeUnit.SECONDS)
 * ����task���ӳ�3���һ��ִ�У��Ժ�ִ��ʱ����Ϊ��һ��ִ�н���ʱ�俪ʼ�ۼƼ��4��.���ϴ�ִ�н���ʱ��Ϊ�����
 * ScheduledThreadPoolExecutorʵ�ֵ�shutdown()������Ĭ����Ϊ�ǵ��ú󣬶�ʱ����ͽ����ˡ�������ͨ��ScheduledThreadPoolExecutor��
 * setContinueExistingPeriodicTasksAfterShutdownPolicy()�������ı������Ϊ�����ݲ���true�������������ʹ����shutdown()������
 * �����������Խ�����.
 * @author de
 *
 */
public class ExecutorSchecheCycleTask_4_8 {

	public static void main(String[] args) {
		//�õ�һ��ScheduleExecutorService
		ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
		System.out.printf("Main: Starting at : %s\n", new Date());
		ScheduledCycleTask  task = new ScheduledCycleTask("ScheduledCycleTask");
		//�������ڿ�ʼ1���ִ�У��Ժ�ÿ������ִ��һ��(�������ʼʱ��������),������ʵ��Runnable���������з��ؽ��Ϊ����֪��
		ScheduledFuture<?>  result = executor.scheduleAtFixedRate(task, 1, 2, TimeUnit.SECONDS);
		for ( int i = 0; i < 10; i++){
			System.out.printf("Main: Delay: %d\n", result.getDelay(TimeUnit.MILLISECONDS));
			try{
				TimeUnit.MILLISECONDS.sleep(500);
			}catch(InterruptedException e){
				e.printStackTrace();
			}
		}
		
		executor.shutdown();
		try{
			TimeUnit.SECONDS.sleep(5);
		}catch(InterruptedException e){
			e.printStackTrace();
		}
		
		System.out.printf("Main: Finished at : %s\n", new Date());
	}
}


class ScheduledCycleTask implements Runnable{

	private String name;
	public ScheduledCycleTask(String name){
		this.name = name;
	}
	@Override
	public void run() {
		System.out.printf("%s : Starting at : %s\n", name,new Date());
	}
	
}