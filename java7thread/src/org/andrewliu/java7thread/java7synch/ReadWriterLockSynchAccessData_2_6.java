package org.andrewliu.java7thread.java7synch;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 使用读写锁来访问数据
 * ReadWriterLock接口和它的唯一实现类ReentrantReadWriterLock，这个类有两个锁，一个读操作锁，一个写操作锁
 * 使用读操作锁可以允许多个线程同时访问，但写操作锁只允许一个线程进行，在一个线程执行写操作时，其它线程不能执行读操作。
 * @author de
 *
 */
public class ReadWriterLockSynchAccessData_2_6 {

	
	public static void main(String[] args) {
		//价格类
		PricesInfo pricesInfo = new PricesInfo();
		//5个读价格的线程
		Reader readers[] = new Reader[5];
		Thread threadsReader[] = new Thread[5];
		for ( int i = 0; i < 5; i++){
			readers[i] = new Reader(pricesInfo);
			threadsReader[i] = new Thread(readers[i]);
		}
		//1个写价格
		Writer writer = new Writer(pricesInfo);
		//写价格的线程
		Thread threadWriter = new Thread(writer);
		//启动5个读价格的线程
		for ( int i = 0; i < 5; i++){
			threadsReader[i].start();
		}
		//启动1个写价格的线程
		threadWriter.start();
	}
	
}


class PricesInfo{
	private double price1;
	private double price2;
	private ReadWriteLock lock;
	public PricesInfo(){
		price1 = 1.0;
		price2 = 2.0;
		lock = new ReentrantReadWriteLock();
	}
	
	
	public double getPrice1(){
		lock.readLock().lock();//得到读锁
		double value = price1;
		lock.readLock().unlock();//释放读锁
		return value;
	}
	
	public double getPrice2(){
		lock.readLock().lock(); //得到读锁
		double value = price2;
		lock.readLock().unlock();//释放读锁
		return value;
	}
	
	
	public void setPrices(double price1,double price2){
		lock.writeLock().lock(); //得到写锁,此时其它读锁不能读
		this.price1 = price1;
		this.price2 = price2;
		lock.writeLock().unlock(); //释放写锁
	}
}

class Reader implements Runnable{
	private PricesInfo pricesInfo;

	public Reader(PricesInfo pricesInfo) {
		this.pricesInfo = pricesInfo;
	}

	@Override
	public void run() {
		for ( int i = 0; i < 10; i++){
			System.out.printf("%s: Price 1: %f\n", Thread.currentThread().getName(),pricesInfo.getPrice1());
			System.out.printf("%s: Price 2: %f\n", Thread.currentThread().getName(),pricesInfo.getPrice2());
		}
	}
	
}

class Writer implements Runnable{
	private PricesInfo pricesInfo;
	public Writer(PricesInfo pricesInfo) {
		super();
		this.pricesInfo = pricesInfo;
	}
	@Override
	public void run() {
		for(int i = 0; i < 3; i++){
			 System.out.printf("Writer: Attempt to modify the prices.\n");
			 pricesInfo.setPrices(Math.random()*10, Math.random()*8);
			 System.out.printf("Writer: Prices have been modified.\n");
			 
			 try{
				 Thread.sleep(2);
			 }catch(InterruptedException e){
				 e.printStackTrace();
			 }
		}
	}
	
}

