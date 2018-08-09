package org.andrewliu.thread.locktest;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class Interrupting {

	private static ExecutorService exec = Executors.newCachedThreadPool();
	static void test(Runnable r) throws InterruptedException{
		Future<?> f = exec.submit(r);//执行一个任务
		TimeUnit.MILLISECONDS.sleep(100);
		System.out.println("Interrupting "+ r.getClass().getName());
		f.cancel(true);//通过Future向任务发出interrupted调用 
		System.out.println("Interrupt sent  to "+r.getClass().getName());
	}
	public static void main(String[] args) throws InterruptedException {
		
		test(new SleepBlocked());
		test(new IOBlocked(System.in));
		test(new SynchronizedBlocked());
		TimeUnit.SECONDS.sleep(3);
		System.out.println("Aborting with system.exit(0)");
		System.exit(0);
	}
	
}

/**
 * 可以用Thread.interrupt()中断任务
 * @author de
 *
 */
class  SleepBlocked implements Runnable{
	@Override
	public void run() {
		try {
			TimeUnit.SECONDS.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println(" Exiting Sleepblocked.run()  ");
	}
	
}
/**
 * 不能直接用Thread.interrupt()中断任务,而要先将流关闭流
 * 即：exec.shutdownNow();
 * in.close();
 * out.close();
 * 这是通过关闭任务在其上发生阻塞的底层资源来实现的.
 * @author de
 *
 */
class IOBlocked implements Runnable{
	private InputStream in;
	public IOBlocked(InputStream is){
		in = is;
	}
	
	@Override
	public void run() {
		System.out.println(" Waiting for read:");
		try {
			in.read();
		} catch (IOException e) {
			if(Thread.currentThread().isInterrupted()){
				System.out.println("Interrupted from blocked I/O");
			}else{
				throw new RuntimeException(e);
			}
		}
		
		System.out.println("Exiting IOBlocked.run()");
	}
	
}
/**
 * 不能中断正在获取同步块代码锁的任务，
 * @author de
 *
 */
class SynchronizedBlocked implements Runnable{

	/**
	 * 永远不会退出，只会让
	 */
	public synchronized void f(){
		while(true){
			Thread.yield();
		}
	}
	
	public SynchronizedBlocked(){
		new Thread(){
			public void run(){
				f();
			}
		}.start();
	}
	@Override
	public void run() {
		System.out.println(" Trying to call f()...");
		f();
		System.out.println("  Exiting SynchronizedBlocked.run()...");
	}
	
}

