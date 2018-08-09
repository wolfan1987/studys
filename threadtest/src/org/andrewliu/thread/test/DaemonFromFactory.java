package org.andrewliu.thread.test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 此任务用于测试自己实现的ThreadFactory[DaemonThreadFactory)
 * @author de
 *
 */
public class DaemonFromFactory  implements Runnable{

	@Override
	public void run() {
		try {
			while(true){
			TimeUnit.MILLISECONDS.sleep(200);
			System.out.println(Thread.currentThread()+ "" + this);
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) throws InterruptedException {
		ExecutorService exec = Executors.newCachedThreadPool(new DaemonThreadFactory());//指定线程工厂
		for(int i = 0; i < 10; i++){
			exec.execute(new DaemonFromFactory());
		}
		System.out.println(" all daemons started...");
		TimeUnit.MILLISECONDS.sleep(500);//等后台线程执行完，不然Main线程结束后，后台线程会全挂掉
		System.out.println(" all thread end...");
	}

}
