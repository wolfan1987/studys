package org.andrewliu.java7thread.java7synch;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 使用Lock（ReentrantLock/ReentrantReadWriteLock实现同步）
 * Lock:支持更灵活的同步代码结构,可得到更灵活的临界区。提供更多功能，如：tryLock()，可以先预获取锁，如果可以得到锁返回true，否则返回false，这种拉探测方便
 * 编程处理。  允许分离读和写的操作，允许多个读线程和一个写线程，比Synchronized关键字有更好的性能。
 * ReentrantLock类也允许递归调用，如果一个线程获取了锁并且进行了递归调用，它将继续持有这个锁，因此调用lock()方法也将立即返回，
 * 并且线程将继续执行递归调用。
 * ReentrantLock/ReentrantReadWriteLock:的构造构造函数中有一个boolean型参数：fair，表示获得锁的机会是否公平，默认为false(不公平),
 * 当使用ReentrantLock(true)\ReentrantReadWriteLock(true)时，是公平型，fair=false时，当有很多线程在等待锁时，锁将随机选择它们中的一个来访问
 * 临界区，当fair=true时，为公平模式，此时，当有很多线程在等待锁时，锁将选择等待时间最长的来访问临界区。
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
		queueLock.lock();//获得锁,此时开始了一个临界区
		try{
			Long duration = (long)(Math.random()*10000);
			System.out.println(Thread.currentThread().getName()+": PrintQueue: Printing a Job during "+(duration/1000)+" secnds");
			Thread.sleep(duration);
		}catch(InterruptedException e){
			e.printStackTrace();
		}finally {
			queueLock.unlock();//释放锁,临界区结束
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
