package org.andrewliu.java7thread.java7base;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * 当线程间没有需要共享的对象数据时，为了干净区分各自的变量可以用线程局部变量来定义各线程的变量，其性能也比较好。
 * 
 * 线程局部变量也提供了remove()方法，用来删除当前线程已存储的值，java并发API包含了InheritableThreadLocal类，
 * 如果一个线程是从其他线程中创建的，这个类将提供继承的值。如果一个线程A在线程局部变量已有值，当它创建其他某个线程B时，
 * 线程B的线程局部变量将跟线程A是一样的。可以覆盖childValue()方法，这个方法用来初始化子线程在线程局部变量中的值，它使用
 * 父线程在线程局部变量中的值作为传入参数。
 * @author de
 *
 */
public class ThreadLocalVarSafeTask_1_10 implements Runnable{

	/**
	 * 定义一个线程局部变量，变量类型为Date型，并重写其initalValue方法，为这些变量赋予初始值(在第一次用get()方法访问其值时若为空则会调用)
	 * 这种方式定义的变量，当个启动此任务的线程都会有各自的值，而不会被各线程共享。这种变量有自己的get()和set()方法来获取或修改
	 * 自己的值.
	 */
	private static ThreadLocal<Date>  startDate = new ThreadLocal<Date>(){
		protected Date initialValue(){
			return new Date();
		}
	};

	@Override
	public void run() {
		System.out.printf("Sttarting Thread: %s : %s\n", Thread.currentThread().getId(),startDate.get());
		
		try {
			TimeUnit.SECONDS.sleep((int)Math.rint(Math.random()*10));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		System.out.printf("Thread Finished: %s : %s\n", Thread.currentThread().getId(),startDate.get());
	}
	
	public static void main(String[] args) {
		ThreadLocalVarSafeTask_1_10  task = new ThreadLocalVarSafeTask_1_10();
		for(int i = 0; i < 10; i ++){
			Thread thread = new Thread(task);
			thread.start();
			try{
				TimeUnit.SECONDS.sleep(2);
			}catch(InterruptedException e){
				e.printStackTrace();
			}
		}
		
	}
}
