package org.andrewliu.thread.test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * ���������ڲ����Լ�ʵ�ֵ�ThreadFactory[DaemonThreadFactory)
 * @author de
 *
 */
public class DaemonFromFactory  implements Runnable{

	@Override
	public void run() {
		try {
			while(true){
			TimeUnit.MILLISECONDS.sleep(200);
			System.out.println(Thread.currentThread()+ "" + this);
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) throws InterruptedException {
		ExecutorService exec = Executors.newCachedThreadPool(new DaemonThreadFactory());//ָ���̹߳���
		for(int i = 0; i < 10; i++){
			exec.execute(new DaemonFromFactory());
		}
		System.out.println(" all daemons started...");
		TimeUnit.MILLISECONDS.sleep(500);//�Ⱥ�̨�߳�ִ���꣬��ȻMain�߳̽����󣬺�̨�̻߳�ȫ�ҵ�
		System.out.println(" all thread end...");
	}

}
