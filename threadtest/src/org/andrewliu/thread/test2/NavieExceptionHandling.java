package org.andrewliu.thread.test2;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * java 多线程---未捕获的异常
 * 在任务的run方法中抛出的异常不能thorws出来到客户端处理
 * Thread对象中有设置异常处理器的接口如：
 * Thread.UncaughtExceptionHandler
 * 这可以让每个Thread对象上都附着一个异常处理器
 * Thread.UncaughtExceptonHandler.uncaughtException() 会在线线程因未捕获异常而临近死亡时被调用
 * @author de
 *
 */
public class NavieExceptionHandling {

	public static void main(String[] args) {
		//无法捕获异常测试
		try{
			ExecutorService exec2 = Executors.newCachedThreadPool();
			exec2.execute(new ExceptionThread());
		}catch(RuntimeException ue){//不会到这里执行
			System.out.println("Exception has been handled!");
		}
		
		//自定义异常处理器且在线程工厂中指定线程异常处理器的测试
		ExecutorService exec = Executors.newCachedThreadPool(new HandlerThreadFactory());
		exec.execute(new ExceptionThread2());
		
		//设置Thread在没有设置异常处理器的情况下的默认异常处理器
		Thread.setDefaultUncaughtExceptionHandler(new MyUncaughtExceptionHandler());
		//此时再如下调用，ExcetionThread中的异常可以得到处理
		ExecutorService exec1 = Executors.newCachedThreadPool();
		exec1.execute(new ExceptionThread());
	}
}

class ExceptionThread2 implements Runnable{
	public void run(){
		Thread t =  Thread.currentThread();
		System.out.println("run() by "+t);
		//得到当前线程的异常处理器
		System.out.println("eh = "+ t.getUncaughtExceptionHandler());
		throw new RuntimeException();
	}
}

/**
 * 定义自己的线程异常处理器
 * @author de
 *
 */
class MyUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler{

	@Override
	public void uncaughtException(Thread t, Throwable e) {
		System.out.println("thread.name="+ t.getName() + " caught exception = "+e.getMessage());
		e.printStackTrace();
	}
	
}

/**
 * 构建包含线程异常处理器的线程工厂
 * @author de
 *
 */
class HandlerThreadFactory implements ThreadFactory{

	@Override
	public Thread newThread(Runnable r) {
		System.out.println(this+" creating new Thread...");
		Thread t = new Thread(r);
		System.out.println("created "+ t);
		t.setUncaughtExceptionHandler(new MyUncaughtExceptionHandler());//设置线程的异常处理器
		System.out.println("eh = "+ t.getUncaughtExceptionHandler());
		return t;
	}
	
	
	
	
}