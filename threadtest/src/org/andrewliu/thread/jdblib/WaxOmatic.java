package org.andrewliu.thread.jdblib;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 给汽车抛光打蜡
 * @author de
 *
 */
public class WaxOmatic  {

	public static void main(String[] args) throws InterruptedException {
		Car car = new Car();
		ExecutorService exec = Executors.newCachedThreadPool();
		exec.execute(new WaxOff(car));//让打蜡线程在waxOn=false时，挂起,一直在while中循环判断waxOn是否不为false，当为ture时，其继续执行
		exec.execute(new WaxOn(car));//抛光线程开始抛光,抛光完将waxOn设为true，同时提醒其它线程干活，且自己挂起，等其它线程打蜡完，将waxOn设为false
		TimeUnit.SECONDS.sleep(5);
		exec.shutdownNow();
	}
}
class Car{
	private Lock lock = new ReentrantLock();
	private Condition condition = lock.newCondition();
	private boolean waxOn = false;
	public void waxed(){
		lock.lock();  //得到当前对象的锁
		try{
			waxOn = true;  //waxOn=true表示可以打蜡了，
			condition.signalAll();//然后唤醒打蜡线程
		}finally{
			lock.unlock();
		}
	}
	
	public void buffed(){
		lock.lock();//得到当前对象的锁
		try{
		waxOn = false;  //waxOn = false，表示打蜡完毕，可以抛光了
		condition.signalAll();//唤醒抛光线程继续执行
		}finally{
			lock.unlock();
		}
	}
	
	public void waitForWaxing() throws InterruptedException{
		lock.lock();   
		try{
			while(waxOn == false){  //当waxOn = false时，此时在抛光，打蜡线程要一直等待，直到抛光线程唤醒打蜡线程并将waxOn = true
				condition.await();
			}
		}finally{
			lock.unlock();
		}
	}
	public void waitForBuffing() throws InterruptedException{
		lock.lock();
		try{
			while(waxOn == true){  //当waxOn = true时，此时在打蜡，抛光线程要一直等待，直到打蜡线程唤醒抛光线程并将waxOn = false
				condition.await();
			}
		}finally{
			lock.unlock();
		}
	}
}

class WaxOn implements Runnable{
	private Car car;//对这一辆车抛光和打蜡
	public WaxOn(Car c){
		car = c;
	}
	public void run(){
		try{
			while(!Thread.interrupted()){  //当线程状态不是中断状态
				System.out.println("Wax on !");  //开始抛光
				TimeUnit.MILLISECONDS.sleep(200);//抛光200毫秒后完成
				car.waxed();//更改waxOn状态为true,并唤醒打蜡线程
				car.waitForBuffing();//等待waxOn=false,再继续执行线程
			}
		}catch(InterruptedException e){
			System.out.println("Exiting via interrupt");
		}
		
		System.out.println("Ending Wax On task!");
	}
}

class WaxOff implements Runnable{
	private  Car car;
	public WaxOff(Car c ){
		car = c;
	}
	
	public void run(){
		try{
			while(!Thread.interrupted()){ //当线程不是中断状态
				car.waitForWaxing();  //因为先是抛光，所以要先挂起自己，等抛光结束，检测waxOn，当为true表示可以打蜡了
				System.out.println("Wax Off!");//打蜡开始
				TimeUnit.MILLISECONDS.sleep(200);//打蜡时长200毫秒后完成
				car.buffed();//打蜡完，更改waxOn状态为false，并唤醒抛光线程干活
			}
		}catch(InterruptedException e){
			System.out.println("Exiting via interrupt");
		}
		System.out.println(" Ending wax Off task!");
	}
}

