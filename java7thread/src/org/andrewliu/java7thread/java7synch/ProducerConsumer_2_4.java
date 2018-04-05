package org.andrewliu.java7thread.java7synch;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * ������--������ģʽʵ��
 * ��synchronized��wait()��notify()��nofifyAll()������ʵ��������---������ģʽ
 * wait()��notify()��nofifyAll()����һ��Ҫ��synchronized��������� �������������ʱ����IllegalMonitorStateException�쳣.
 * ���� wait()��������ǰ�߳̽��ͷŴ˶����������ô�����߳̾Ϳ���ȥ�õ������в������������߳���һ�������µ���notify()��notifyAll()��
 * ��Ҫͬһ��������������wait()���߳̽������ѣ�Ȼ������������������Լ��Ĳ���.
 * @author de
 *
 */
public class ProducerConsumer_2_4 {

	public static void main(String[] args) {
		//�洢��
		EventStorage storage = new EventStorage();
		//�����߼��������߳�
		Producer producer = new Producer(storage);
		Thread thread1 = new Thread(producer);
		//�����߼��������߳�
		Consumer consumer = new Consumer(storage);
		Thread thread2 = new Thread(consumer);
		
		thread2.start();
		thread1.start();
	}
}

class EventStorage{
	private int maxSize;
	private List<Date>  storage;
	
	public EventStorage(){
		maxSize = 10;
		//ʡ�Ծ������͵�д��ֻ����jdk1.7�в�֧��
		storage = new LinkedList<>();
	}
	
	public synchronized void set(){
		//������while�е���wait()�����Ҳ��ϲ�ѯwhile��������ֱ������Ϊ���ʱ����ܼ���
		while(storage.size() == maxSize){  //�����������ˣ��͵ȴ�����
			try{
				wait();
			}catch(InterruptedException e){
				e.printStackTrace();
			}
		}
		
		storage.add(new Date());  //��Ȼ�����������Ȼ��֪ͨ�����ڵȴ��������߳�
		System.out.printf("Set: %d", storage.size());
		notifyAll();
	}
	
	public synchronized void get(){
		while(storage.size()==0){  //���û�п����ѵģ���һֱ�ȴ���Ԫ����������
			try{
				wait();
			}catch(InterruptedException e){
				e.printStackTrace();
			}
		}
		//��Ȼ�����ѣ�Ȼ��֪ͨ�����ڵȴ����ѵ��߳�
		System.out.printf("Get: %d: %s",storage.size(),((LinkedList<?>)storage).poll());
		notifyAll();
	}
}

/**
 * �����߳�
 * @author de
 *
 */
class Producer implements Runnable{
	private EventStorage storage;
	public Producer(EventStorage storage){
		this.storage = storage;
	}
	@Override
	public void run() {
		for(int i = 0; i < 100; i++){
			storage.set();
		}
	}
	
}

/**
 * �������߳�
 * @author de
 *
 */
class Consumer implements Runnable{
	private EventStorage storage;
	public Consumer(EventStorage storage){
		this.storage = storage;
	}
	@Override
	public void run() {
		for(int i = 0; i < 100; i++){
			storage.get();
		}
	}
	
	
}