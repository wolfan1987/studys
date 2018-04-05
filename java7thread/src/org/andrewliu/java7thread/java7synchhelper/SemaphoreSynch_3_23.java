package org.andrewliu.java7thread.java7synchhelper;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * jdk自带同步辅助类： Semaphore(信号量)
 * 可以用它来实现二进制信息量，二进制信号量是一种比较特殊的信号量，用来保护对唯一的共享资源的访问。
 * 因而它的内部计数器只有0和1两个值。
 * Semaphore的其它两个方法:
 * acquireUninterruptibly():它就是acquire()方法，当信号量的内部计数器变成0的时候，信号量将阻塞线程直到其被释放，线程
 * 在被阻塞的这段时间中，可能会被告中断，从而导致acquire()方法抛出InterruptedException 异常，而acquireUninteruptibly()
 * 方法会忽略线程中断并且不会抛出任何异常。
 * tryAcquire()，试图获得信息量。如果能获得就返回ture，不能就返回false,从而避开线程的阻塞和等待信号量的释放。可根据此状态做出相关处理。
 * @author de
 *
 */
public class SemaphoreSynch_3_23 {

	public static void main(String[] args) {
//		/**单个打印机**/
//		PrintQueue printQueue = new PrintQueue();//只有一个PrintQueue，多个线程同步时，用信息量控制，只能由一个线程使用
//		Thread thread[] = new Thread[10];
//		for ( int i = 0; i < 10; i++){
//			thread[i] = new Thread(new Job(printQueue),"Thread"+i);
//		}
//		
//		for ( int i = 0; i < 10; i++){
//			thread[i].start();
//		}
		
		/**多个打印机**/
		PrintQueues printQueues = new PrintQueues();//有3个PrintQueue，多个线程同步时，超过3个时要排队等待
		Thread threads[] = new Thread[10];
		for ( int i = 0; i < 10; i++){
			threads[i] = new Thread(new Jobs(printQueues),"Threads"+i);
		}
		
		for ( int i = 0; i < 10; i++){
			threads[i].start();
		}
		
	}
}

class PrintQueue{
	private  final Semaphore  semaphore;
	
	public PrintQueue(){   
		//不管多少个线程，同一时刻只能由一个线程使用PrintQueue
		semaphore = new Semaphore(1);  
		//semaphore = new Semaphore(1,boolean fair); fair=true时为公平模式，fair=false时为不茌平模式（默认）
	}
	
	public void printJob (Object document){
		try{
			semaphore.acquire(); //获得一个信号，在释放前，其它线程不能获得信息量
			long duration = (long)(Math.random()*10);
			System.out.printf("%s: PrintQueue: Printing a Job during %d seconds\n",Thread.currentThread().getName(),duration);
			Thread.sleep(duration);
		}catch(InterruptedException e){
			e.printStackTrace();
		}finally{
			semaphore.release();//释放信息量，其它线程可以再借用
		}
	}
}

/**
 * 多个打印机，按是空闲排队等待打印
 * @author de
 *
 */
class PrintQueues{
     private  final Semaphore  semaphore;
     //保存空闲的打印机
	 private  boolean freePrinters[];
	 //保护对freePrinters的访问
	 private Lock lockPrinters;
	public PrintQueues(){   
		//不管多少个线程，同一时刻只能由一个线程使用PrintQueue
		semaphore = new Semaphore(3);  
		freePrinters = new boolean[3];
		for(int i = 0; i < 3; i++){
			freePrinters[i] = true;
		}
		lockPrinters = new ReentrantLock();
	}
	
	public void printJob (Object document){
		try{
			semaphore.acquire(); //获得一个信号，在释放前，其它线程不能获得信息量
			//通过私有函数getPrinter()获得分配打印工作的打印机编号
			int assignedPrinter = getPrinter();
			long duration = (long)(Math.random()*10);
			System.out.printf("%s: PrintQueue: Printing a Job during %d seconds\n",Thread.currentThread().getName(),assignedPrinter,duration);
			//Thread.sleep(duration);
			TimeUnit.SECONDS.sleep(duration);
			//将某一打印机设为空闲，同时释放信息量
			freePrinters[assignedPrinter] = true;
		}catch(InterruptedException e){
			e.printStackTrace();
		}finally{
			semaphore.release();//释放信息量，其它线程可以再借用
		}
	}
	/**
	 * 得到空闲打印机编号 
	 * @return
	 */
	private int getPrinter(){
		int ret = -1;
		try{
			//用锁，锁住对freePrinters的访问，来确保不会被其他线程抢走打印机
			lockPrinters.lock();
			for( int i = 0; i < freePrinters.length; i++){
				if(freePrinters[i]){//如果找到空闲的
					ret=i;  //保存当前打印机索引号
					freePrinters[i] = false; //将该打印机设为不可用
					break;
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			//释放锁
			lockPrinters.unlock();
		}
		
		return ret;
	}
	
}




class Job implements Runnable{
	private PrintQueue printQueue;
	public Job(PrintQueue printQueue){
		this.printQueue = printQueue;
	}
	@Override
	public void run() {
		System.out.printf("%s: Going to print a job \n", Thread.currentThread().getName());
		printQueue.printJob(new Object());
		System.out.printf("%s: The document has been printed\n",Thread.currentThread().getName());
	}
	
}

class Jobs implements Runnable{
	private PrintQueues printQueues;
	public Jobs(PrintQueues printQueues){
		this.printQueues = printQueues;
	}
	@Override
	public void run() {
		System.out.printf("%s: Going to print a job \n", Thread.currentThread().getName());
		printQueues.printJob(new Object());
		System.out.printf("%s: The document has been printed\n",Thread.currentThread().getName());
	}
	
}
