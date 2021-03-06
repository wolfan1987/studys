package org.andrewliu.thread.jdblib;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 
 * @author de
 *
 */
public class CountDownLatchDemo {

	static final int SIZE = 100;
	public static void main(String[] args) {
		ExecutorService exec = Executors.newCachedThreadPool();
		CountDownLatch latch = new CountDownLatch(SIZE);//初始值为100的CountDownLatch
		for(int i = 0; i < 10; i++){
			exec.execute(new WaitingTask(latch));  //启动10个等待100个执行doWork后的线程在await
		}
		for(int i = 0; i < SIZE; i++){
			exec.execute(new TaskPortion(latch));//启动100个执行doWork的线程
		}
		System.out.println("Launched all tasks!");
		exec.shutdown();
	}
}

class TaskPortion implements Runnable{
	private static int counter = 0;
	private final int id = counter++;
	private static Random rand = new Random(47);
	private final CountDownLatch latch;
	TaskPortion(CountDownLatch latch){
		this.latch = latch;
	}
	@Override
	public void run() {
		try{
			doWork();
			latch.countDown();//执行一次，重设一次数值（-1）
		}catch(InterruptedException e){
			
		}
	}
	
	public void doWork() throws InterruptedException{
		TimeUnit.MILLISECONDS.sleep(rand.nextInt(2000));
		System.out.println(this+"completed");
	}
	
	public String toString(){
		return String.format("%1$-3d",id);
	}
}

class WaitingTask implements Runnable{
	private static int counter = 0;
	private final int id = counter++;
	private final CountDownLatch latch;
	WaitingTask(CountDownLatch latch){
		this.latch = latch;
	}
	@Override
	public void run() {
		try{
			latch.await();  //等TaskPortion任务全部执行完
			System.out.println("latch barrier passed for "+this);
		}catch(InterruptedException e){
			System.out.println(this + "  interrupted!");
		}
	}
	
	public String toString(){
		return  String.format("WaitingTask %1$-3d ",id);
	}
	
	
}

