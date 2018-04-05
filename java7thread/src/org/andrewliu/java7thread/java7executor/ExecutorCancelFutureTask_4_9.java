package org.andrewliu.java7thread.java7executor;

import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * ͨ��Future��cancel(true��false)�����������������ȡ��
 * �����������ɣ����ѱ�ȡ����������ԭ����ȡ��������trueʱ������������false������������ȡ����
 * ��������ڵȴ������̣߳�����trueʱ������ȡ�������Ҳ��Ὺʼִ�У�����Ѿ������У�������ȡ�����������false��������
 * ���е����񲻻ᱻȡ����
 * ���Future���������������Ѿ���ȡ������ôʹ��Future�����get()����ʱ���׳�CancellationException�쳣.
 * @author de
 *
 */
public class ExecutorCancelFutureTask_4_9 {

	public static void main(String[] args) {
		ThreadPoolExecutor executor = (ThreadPoolExecutor)Executors.newCachedThreadPool();
		CancelFutureTask task = new CancelFutureTask();
		System.out.printf("Main: Executing the Task\n");
		//ͨ��Future�õ������ִ�н����������������Զ�������������result��Զ�����н��
		Future<String> result = executor.submit(task);
		
		try{
			TimeUnit.SECONDS.sleep(2);
		}catch(InterruptedException e){
			e.printStackTrace();
		}
		
		System.out.printf("Main: Canceling the Task\n");
		//������ȡ��ִ��
		result.cancel(true);
		System.out.printf("Main: Cancelled: %s\n", result.isCancelled());
		System.out.printf("Main: Done: %s\n", result.isDone());
		executor.shutdown();
		System.out.printf("Main: The executor has finished\n");
	}
}


class CancelFutureTask implements Callable<String>{

	@Override
	public String call() throws Exception {
		//������ѭ��ִ��ʱ�����Բ���return
		while(true){
			System.out.printf("Task: Test\n");
			Thread.sleep(100);
		}
	}
	
}