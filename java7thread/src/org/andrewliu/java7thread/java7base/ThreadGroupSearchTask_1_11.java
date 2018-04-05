package org.andrewliu.java7thread.java7base;

import java.util.Date;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * �߳��������һ������̵߳���һ����һ�ĵ�Ԫ���ɶ������̶߳�����з��ʲ��������ǣ����������ƣ�
 * ThreadGroup���ʾһ���̣߳��߳�����԰����̶߳���Ҳ���԰��������߳����������һ�����νṹ.
 * @author de
 *
 */
public class ThreadGroupSearchTask_1_11  implements Runnable{

	private Result result;
	public ThreadGroupSearchTask_1_11(Result result){
		this.result = result;
	}
	
	@Override
	public void run() {
		String name = Thread.currentThread().getName();
		System.out.printf("Thread %s: Start\n", name);
		try{
		doTask();
		result.setName(name);
		}catch(InterruptedException e){
			System.out.printf("Thread %s: Interrupted\n",name);
			return;
		}
		System.out.printf("Thread %s: End\n", name);
	}
	
	private void doTask() throws InterruptedException{
		Random random = new Random((new Date()).getTime());
		int value = (int)(random.nextDouble()*100);
		System.out.printf("Thread  %s: %d\n", Thread.currentThread().getName(),value);
		TimeUnit.SECONDS.sleep(value);
	}
	
	
	//�߳������
	public static void main(String[] args) {
		//����һ��nameΪSearcher���߳���
		ThreadGroup threadGroup = new ThreadGroup("Searcher");
		Result result = new Result();
		ThreadGroupSearchTask_1_11  searchTask = new ThreadGroupSearchTask_1_11(result);
		for(int i = 0; i < 5; i++){
			//���������߳������̹߳�����ѭ���е�5���߳����ǹ�ͬ����������searchTask����������ͬһ���߳���(searcher)
			Thread thread = new Thread(threadGroup,searchTask);
			thread.start();
			try {
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		/***��ӡ�߳��������Ϣ***/
		System.out.printf("Number of Threads: %d\n", threadGroup.activeCount());//��ӡ�߳������߳���Ŀ
		System.out.printf("Information about the Thread Group\n");
		threadGroup.list();  //��ӡ�߳��������Ϣ
		Thread[]  threads = new Thread[threadGroup.activeCount()];//���屣���߳������̶߳��������
		threadGroup.enumerate(threads);//��ȡ�߳���������߳��б�
		for ( int i = 0; i < threadGroup.activeCount();i++){
			System.out.printf("Thread: %s: %s\n", threads[i].getName(),threads[i].getState());
		}
		
		waitFinish(threadGroup);
	}
	
	
	private static void waitFinish(ThreadGroup threadGroup){
		while(threadGroup.activeCount()>9){
			try{
			TimeUnit.SECONDS.sleep(1);
			}catch(InterruptedException e){
				e.printStackTrace();
			}
		}
	}
	

}
class Result{
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
