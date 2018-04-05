package org.andrewliu.java7thread.java7synch;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * ʹ�ö�д������������
 * ReadWriterLock�ӿں�����Ψһʵ����ReentrantReadWriterLock�����������������һ������������һ��д������
 * ʹ�ö������������������߳�ͬʱ���ʣ���д������ֻ����һ���߳̽��У���һ���߳�ִ��д����ʱ�������̲߳���ִ�ж�������
 * @author de
 *
 */
public class ReadWriterLockSynchAccessData_2_6 {

	
	public static void main(String[] args) {
		//�۸���
		PricesInfo pricesInfo = new PricesInfo();
		//5�����۸���߳�
		Reader readers[] = new Reader[5];
		Thread threadsReader[] = new Thread[5];
		for ( int i = 0; i < 5; i++){
			readers[i] = new Reader(pricesInfo);
			threadsReader[i] = new Thread(readers[i]);
		}
		//1��д�۸�
		Writer writer = new Writer(pricesInfo);
		//д�۸���߳�
		Thread threadWriter = new Thread(writer);
		//����5�����۸���߳�
		for ( int i = 0; i < 5; i++){
			threadsReader[i].start();
		}
		//����1��д�۸���߳�
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
		lock.readLock().lock();//�õ�����
		double value = price1;
		lock.readLock().unlock();//�ͷŶ���
		return value;
	}
	
	public double getPrice2(){
		lock.readLock().lock(); //�õ�����
		double value = price2;
		lock.readLock().unlock();//�ͷŶ���
		return value;
	}
	
	
	public void setPrices(double price1,double price2){
		lock.writeLock().lock(); //�õ�д��,��ʱ�����������ܶ�
		this.price1 = price1;
		this.price2 = price2;
		lock.writeLock().unlock(); //�ͷ�д��
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

