package org.andrewliu.java7thread.java7conlist;

import java.util.concurrent.PriorityBlockingQueue;


/**
 * PriorityBlockingQueue 优先级阻塞队列测试,要求加下的元素必须实现Comparable接口，实现compareTo()方法.以此来实现元素的排序优先
 * add(E): 添加元素到队列
 * poll(): 返回队列中第一个元素，并将元素移除.
 * clear(): 移除队列中的所有元素。
 * take(): 返回队列中的第一个元素并将其移除。如果队列为空，线程阻塞直到队列中有可用元素。
 * put(E e): E 是PriorityBlockingQueue的泛型参数，表示传入参数的类型，此方法把参数对应的元素插入到队列中。
 * peek(): 返回队列中的第一个元素，但不将其移除.
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
			taskThreads[i].start(); //启动线程往里面加元素
		}
		
		for ( int i = 0; i < taskThreads.length; i++){
			try{
				taskThreads[i].join();  //让所有线程都执行完
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
	 * 实现compareTo方法，进行优先级比较确定
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