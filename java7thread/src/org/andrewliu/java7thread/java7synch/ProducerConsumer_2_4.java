package org.andrewliu.java7thread.java7synch;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * 生产者--消费者模式实现
 * 用synchronized、wait()、notify()、nofifyAll()方法来实现生产者---消费者模式
 * wait()、notify()、nofifyAll()方法一定要在synchronized块里面调用 ，否则会在运行时报：IllegalMonitorStateException异常.
 * 调用 wait()方法，当前线程将释放此对象的锁，那么其它线程就可以去得到锁进行操作，当其它线程在一定条件下调用notify()或notifyAll()后
 * 需要同一个对象锁的正在wait()的线程将被唤醒，然后继续持有锁，进行自己的操作.
 * @author de
 *
 */
public class ProducerConsumer_2_4 {

	public static void main(String[] args) {
		//存储器
		EventStorage storage = new EventStorage();
		//生产者及生产者线程
		Producer producer = new Producer(storage);
		Thread thread1 = new Thread(producer);
		//消费者及消费者线程
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
		//省略具体类型的写法只有在jdk1.7中才支持
		storage = new LinkedList<>();
	}
	
	public synchronized void set(){
		//必须在while中调用wait()，并且不断查询while的条件，直到条件为真的时候才能继续
		while(storage.size() == maxSize){  //当生产的满了，就等待消费
			try{
				wait();
			}catch(InterruptedException e){
				e.printStackTrace();
			}
		}
		
		storage.add(new Date());  //不然则继续生产，然后通知所有在等待生产的线程
		System.out.printf("Set: %d", storage.size());
		notifyAll();
	}
	
	public synchronized void get(){
		while(storage.size()==0){  //如果没有可消费的，就一直等待有元素生产进来
			try{
				wait();
			}catch(InterruptedException e){
				e.printStackTrace();
			}
		}
		//不然就消费，然后通知所有在等待消费的线程
		System.out.printf("Get: %d: %s",storage.size(),((LinkedList<?>)storage).poll());
		notifyAll();
	}
}

/**
 * 生产线程
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
 * 消费者线程
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