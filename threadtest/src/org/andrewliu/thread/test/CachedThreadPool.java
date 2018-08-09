package org.andrewliu.thread.test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 用jdk5/6中自带的Executor（线程执行器）管理Thread对象
 * @author de
 *
 */
public class CachedThreadPool {

	public static void main(String[] args) {
		//ExecutorService是具有服务生命周期的Executor如：启动、阻塞、关闭
		//newcachedThreadPool 将会为每个任务(实现了Runnable接口)创建一个线程，这里会在有任务时再创建线程（分次消耗） 将任务分配给线程
		ExecutorService execCache = Executors.newCachedThreadPool();
		//与newCachedThreadPool相同，但其只能创建指定参数个数的线程如：5个,这里会先创建线程，然后等待任务（一次性消耗）将任务分配给线程,有任务阻塞就等待
		ExecutorService execFixed = Executors.newFixedThreadPool(5);
		//只实例化一个线程，如果有多个任务添加到线程中，其任务会按添加的顺序进行排队等候
		ExecutorService execSingled = Executors.newSingleThreadExecutor();
		for( int i = 0; i < 5; i++){
			execCache.execute(new FirstThreadTest());
		}
		//所有任务执行完后退出程序
		execCache.shutdown();
		
		
	}
}
