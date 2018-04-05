package org.andrewliu.java7thread.java7executor;

import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Executor  Framework(�߳�ִ������ܣ������������ڵ㣺Executor��Executor���ӽӿ�ExecutorService
 * ThreadPoolExecutor��ʵ�����������ӿ�.
 * ���׻��Ʒ���������Ĵ�����ִ�У����轫ʵ����Runnable�ӿڵĶ��� �����͸�ִ����ִ�м���,������Runnable����Ĵ�����ʵ����������
 * ��������̳߳ؼ�������߳������ܣ�ִ������ӳ������߳���ִ�����񣬱����˲��ϵش����������̶߳�����ϵͳ�����½���
 * ִ��������һ��������:Runnable�Ľӿ�:Callable�������ӿڵ���������Ϊ��call�����Է��ؽ����������һ��Callable�����
 * ִ����ʱ�������һ��ʵ����Future�ӿڵĶ��󣬿���ʹ���������������Callable�����״̬�ͽ��.
 * ע�⣺�����̳߳�Ҳ��ȱ�㣬������͹���������ִ������ϵͳ�ĸ��ɽ�����أ���˽����̵߳������Ǻ���Ļ����߳�ֻ�����кܶ�
 * ��ʱ��ʱ���ʺϲ���Executors�������newCachedThreadPool()����������ִ������
 * @author de
 * ��ִ������ִ������
 */
public class ThreadExecutorTest_4_2 {

	public static void main(String[] args) {
		ExecutorServer  server = new ExecutorServer();  //ʵ���������
		for ( int i = 0; i < 100; i++){ 
			ExecutorTask task = new ExecutorTask("Task"+i); //ʵ��������
			server.executeTask(task); //�ύ���񵽷����ִ������
		}
		
		server.endServer(); //����ִ����(����������ִ�����)
	}
	
}

/**
 * �������
 * @author de
 *
 */
class ExecutorTask implements Runnable{
	private Date initDate;
	private String name;
	public ExecutorTask(String name){
		initDate = new Date();
		this.name = name;
	}
	@Override
	public void run() {
		System.out.printf("%s: Task %s: Created on : %s\n", Thread.currentThread().getName(),name,initDate);
		System.out.printf("%s: Task %s: Started on: %s \n", Thread.currentThread().getName(),name,new Date());
		try{
			Long duration = (long)(Math.random()*10);
			System.out.printf("$s: Task %s: Doing a task during %d seconds \n", Thread.currentThread().getName(),name,duration);
			TimeUnit.SECONDS.sleep(duration);
		}catch(InterruptedException e){
			e.printStackTrace();
		}
		
		System.out.printf("%s: Task %s: Finished on : %s\n", Thread.currentThread().getName(),name,new Date());
	}
	
}

/**
 * �������ˣ� ��ִ����ʵ��
 * @author de
 *
 */
class ExecutorServer {
	
	private ThreadPoolExecutor executor;
	public  ExecutorServer(){
		//����������С�Ļ����̳߳�
		executor = (ThreadPoolExecutor)Executors.newCachedThreadPool();
		//�����̶������̵߳��̳߳ٳ�
		//executor = (ThreadPoolExecutor)Executors.newFixedThreadPool(5);
		//����ֻ��һ��ִ��һ�������ִ����
		//executor = (ThreadPoolExecutor)Executors.newSingleThreadExecutor();
	}
	
	public void executeTask(ExecutorTask task){
		System.out.printf("Server: A new task has arrived\n");
		executor.execute(task);//ִ������
		System.out.printf("Server: pool Size: %d\n", executor.getPoolSize());  //�̳߳ش�С
		System.out.printf("Server: Active Count: %d\n", executor.getActiveCount()); //��ǰ����߳�
		System.out.printf("Server: Completed Tasks: %d\n", executor.getCompletedTaskCount()); //�Ѿ���ɵ��߳�
		
		//System.out.printf("Server: Task Count: %d\n", executor.getTaskCount());  �ڴ����̶������߳�ʱ�ã�������ʾ�õ���ǰ�ж��ٸ������Ѿ����͸�ִ����
	}
	
	public void endServer(){
		executor.shutdown();
	}
	
	
}
