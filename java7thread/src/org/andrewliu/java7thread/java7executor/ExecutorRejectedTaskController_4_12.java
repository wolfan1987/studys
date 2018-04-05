package org.andrewliu.java7thread.java7executor;

import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


/**
 * ��RejectedExecutionHandler�ӿڣ�����ִ�������ܾ�������
 * ��shutdow������ִ��������֮�䷢��һ�������ִ�������������ᱻ�ܾ�����Ϊ���ʱ���ִ�����Ѳ��ٽ������񡣽�������ǣ�
 * �Լ�ʵ������ܾ��������ӿڣ�RejectedExecutionHandler��Ȼ���ý�����Ϊִ����������ܾ���������
 * ����ӿ�����һ������:rejectedExecution()����������������������
 * һ����Runnable���������洢���ܾ�������һ����Executor���������洢���񱻾ܾ���ִ����.
 * ��ִ�����ܾ���ÿһ�����񶼽��������������ǰ��ʱִ�������е���setRejectedExecutionHandler()�������������ڱ��ܾ�������Ĵ������
 * Executor�ڽ���һ������ʱ�����ȿ��Ƿ������shutdown() ����������е��ã��;ܾ������񣬾ܾ�ʱ������Ѱ��ͨ��setRejectedExecutionHandler()����
 * ���õ����ڱ��ܾ�������Ĵ������������ҵ�һ���������ִ�����͵��� rejectedExecution()������������׳�RejecedExecutionException�쳣��
 * ����һ������ʱ�쳣����Ҫ��catch����
 * @author de
 *
 */
public class ExecutorRejectedTaskController_4_12 {

	public static void main(String[] args) {
		RejectedTaskController controller = new RejectedTaskController();  //ʵ����һ���ܾ���������
		ThreadPoolExecutor executor = (ThreadPoolExecutor)Executors.newCachedThreadPool();
		executor.setRejectedExecutionHandler(controller);  //��������ܾ�������
		System.out.printf("Main: Starting.\n");
		for ( int i = 0; i < 3; i++){
			RejectedTask task = new RejectedTask("RejectedTask"+i);
			executor.submit(task);
		}
		
		System.out.printf("Main: Shutting down the Executor.\n");
		executor.shutdown();
		System.out.printf("Main: Sending another Task.\n");
		RejectedTask  rejectedTask = new RejectedTask("RejectedTask");
		executor.submit(rejectedTask);
		System.out.println("Main : End");
	}
   
}

class RejectedTaskController implements RejectedExecutionHandler{
	
	@Override
	public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
		System.out.printf("RejectedTaskController: The task %s has been rejected\n",r.toString());
		System.out.printf("RejectedTaskController: %s\n",executor.toString());
		System.out.printf("RejectedTaskController: Terminating: %s\n", executor.isTerminating());
		System.out.printf("RejectedTaskController: Terminated: %s\n", executor.isTerminated());
		
	}
	
}

class RejectedTask implements Runnable{
	private String name;
	public RejectedTask(String name){
		this.name = name;
	}
	@Override
	public void run() {
		System.out.println("RejectedTask" + name+": Starting");
		try{
			long duration = (long)(Math.random()*10);
			System.out.printf("RejectedTask %s: ReportGenerator : Generating a report during %d seconds\n", name,duration);
			TimeUnit.SECONDS.sleep(duration);
		}catch(InterruptedException e){
			e.printStackTrace();
		}
		System.out.printf("RejectedTask %s: Ending\n", name);
	}
	
	public String toString(){
		return name;
	}
}