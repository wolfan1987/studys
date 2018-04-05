package org.andrewliu.java7thread.java7synch;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * ʹ��Lock��ReentrantLock/ReentrantReadWriteLockʵ��ͬ����
 * Lock:֧�ָ�����ͬ������ṹ,�ɵõ��������ٽ������ṩ���๦�ܣ��磺tryLock()��������Ԥ��ȡ����������Եõ�������true�����򷵻�false��������̽�ⷽ��
 * ��̴���  ����������д�Ĳ��������������̺߳�һ��д�̣߳���Synchronized�ؼ����и��õ����ܡ�
 * ReentrantLock��Ҳ����ݹ���ã����һ���̻߳�ȡ�������ҽ����˵ݹ���ã����������������������˵���lock()����Ҳ���������أ�
 * �����߳̽�����ִ�еݹ���á�
 * ReentrantLock/ReentrantReadWriteLock:�Ĺ��칹�캯������һ��boolean�Ͳ�����fair����ʾ������Ļ����Ƿ�ƽ��Ĭ��Ϊfalse(����ƽ),
 * ��ʹ��ReentrantLock(true)\ReentrantReadWriteLock(true)ʱ���ǹ�ƽ�ͣ�fair=falseʱ�����кܶ��߳��ڵȴ���ʱ���������ѡ�������е�һ��������
 * �ٽ�������fair=trueʱ��Ϊ��ƽģʽ����ʱ�����кܶ��߳��ڵȴ���ʱ������ѡ��ȴ�ʱ������������ٽ�����
 * @author de
 *
 */
public class LockSynch_2_5 {

	public static void main(String[] args) {
		PrintQueue printQueue = new PrintQueue();
		Thread thread[] = new Thread[10];
		for(int i = 0; i < 10; i++){
			thread[i] = new Thread(new Job(printQueue),"Thread "+i);
		}
		
		for( int i = 0; i < 10; i++){
			thread[i].start();
		}
	}
}


class PrintQueue{
	private final Lock queueLock = new ReentrantLock();
	
	//private final Lock queueLock = new ReentrantLock(fair);
	//private final Lock queueLock = new ReentrantReadWriteLock(fair);
	public void printJob(Object document){
		queueLock.lock();//�����,��ʱ��ʼ��һ���ٽ���
		try{
			Long duration = (long)(Math.random()*10000);
			System.out.println(Thread.currentThread().getName()+": PrintQueue: Printing a Job during "+(duration/1000)+" secnds");
			Thread.sleep(duration);
		}catch(InterruptedException e){
			e.printStackTrace();
		}finally {
			queueLock.unlock();//�ͷ���,�ٽ�������
		}
	}
}

class Job implements Runnable{
	private PrintQueue printQueue;
	public Job(PrintQueue printQueue){
		this.printQueue = printQueue;
	}
	@Override
	public void run() {
		System.out.printf("%s: Going to print a document\n", Thread.currentThread().getName());
		printQueue.printJob(new Object());
		System.out.printf("%s: The document has been printed\n",Thread.currentThread().getName());
	}
	
	
}
