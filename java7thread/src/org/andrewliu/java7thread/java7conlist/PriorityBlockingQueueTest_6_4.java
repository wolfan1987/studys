package org.andrewliu.java7thread.java7conlist;

import java.util.concurrent.PriorityBlockingQueue;


/**
 * PriorityBlockingQueue ���ȼ��������в���,Ҫ����µ�Ԫ�ر���ʵ��Comparable�ӿڣ�ʵ��compareTo()����.�Դ���ʵ��Ԫ�ص���������
 * add(E): ���Ԫ�ص�����
 * poll(): ���ض����е�һ��Ԫ�أ�����Ԫ���Ƴ�.
 * clear(): �Ƴ������е�����Ԫ�ء�
 * take(): ���ض����еĵ�һ��Ԫ�ز������Ƴ����������Ϊ�գ��߳�����ֱ���������п���Ԫ�ء�
 * put(E e): E ��PriorityBlockingQueue�ķ��Ͳ�������ʾ������������ͣ��˷����Ѳ�����Ӧ��Ԫ�ز��뵽�����С�
 * peek(): ���ض����еĵ�һ��Ԫ�أ����������Ƴ�.
 * @author de
 *
 */
public class PriorityBlockingQueueTest_6_4 {

	public static void main(String[] args) {
		PriorityBlockingQueue<PriorityEvent>  queue = new PriorityBlockingQueue<>();
		Thread taskThreads[] = new Thread[5];
		for(int i = 0; i < taskThreads.length;i++){
			PriorityTask  task = new PriorityTask(i,queue);
			taskThreads[i] = new Thread(task);
		}
		
		for ( int i = 0; i < taskThreads.length; i++){
			taskThreads[i].start(); //�����߳��������Ԫ��
		}
		
		for ( int i = 0; i < taskThreads.length; i++){
			try{
				taskThreads[i].join();  //�������̶߳�ִ����
			}catch(InterruptedException e){
				e.printStackTrace();
			}
		}
		
		System.out.printf("Main: Queue Size: %d\n", queue.size());
		for ( int i = 0; i< taskThreads.length*1000;i++){
			PriorityEvent event = queue.poll();
			System.out.printf("Thread %s: Priority %d\n", event.getThread(),event.getPriority());
		}
		
		System.out.printf("Main: Queue Size: %d\n", queue.size());
		System.out.printf("Main: End of the program\n");
	}
}


class PriorityEvent implements Comparable<PriorityEvent>{
	private int thread;
	private int priority;
	public PriorityEvent(int thread, int priority) {
		super();
		this.thread = thread;
		this.priority = priority;
	}
	public int getThread() {
		return thread;
	}
	public int getPriority() {
		return priority;
	}
	
	/**
	 * ʵ��compareTo�������������ȼ��Ƚ�ȷ��
	 */
	@Override
	public int compareTo(PriorityEvent o) {
		if(this.priority> o.getPriority()){
			return -1;
		}else if(this.priority<o.getPriority()){
			return 1;
		}else{
			return 0;
		}
	}
}

class PriorityTask implements Runnable{
	private int id;
	private PriorityBlockingQueue<PriorityEvent>   queue;
	
	public PriorityTask(int id, PriorityBlockingQueue<PriorityEvent> queue) {
		super();
		this.id = id;
		this.queue = queue;
	}

	@Override
	public void run() {
		for ( int i = 0; i < 1000; i++){
			PriorityEvent event = new PriorityEvent(id, i);
			queue.add(event);
		}
	}
	
	
	
	
}