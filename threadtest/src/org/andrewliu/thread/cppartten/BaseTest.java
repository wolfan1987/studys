package org.andrewliu.thread.cppartten;

public class BaseTest {

	public static void main(String[] args) {
		Object lock = new Object();
		BaseTestT1 t1 = new BaseTestT1(lock);
		t1.start();
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		BaseTestT2 t2 = new  BaseTestT2(lock);
		t2.start();
	}
	
	
	
}

class  BaseTestT1  extends Thread{
	private  Object lock;
	public  BaseTestT1(Object lock){
		super();
		this.lock = lock;
	}
	
	@Override
	public void run(){
		try {
		synchronized(lock){
			System.out.println("开始  wait time="+System.currentTimeMillis());
					lock.wait();
			System.out.println("结束  wait time="+System.currentTimeMillis());
		}
		} catch (InterruptedException e) {
		
			e.printStackTrace();
		}
	}
}

class  BaseTestT2  extends Thread{
	private  Object lock;
	public  BaseTestT2(Object lock){
		super();
		this.lock = lock;
	}
	
	@Override
	public void run(){
		synchronized(lock){
			System.out.println("开始  wait time="+System.currentTimeMillis());
					lock.notify();
			System.out.println("结束  wait time="+System.currentTimeMillis());
		}
		
	}
}