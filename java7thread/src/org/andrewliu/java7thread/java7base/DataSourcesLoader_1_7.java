package org.andrewliu.java7thread.java7base;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * 演示Thread.join()的使用
 * join()方法，表示将当前线程A暂挂不运行，等待其他线程执行完终止，当前线程再执行
 * 如：当前线程为A,有另外的线程B、C。如果在线程A中调用线程B.join()和C.join()方法，则线程A暂挂，线程B\C优先执行，执行完终止后，线程A会继续执行。
 * 这就等待终止。
 * @author de
 *
 */
public class DataSourcesLoader_1_7  implements Runnable{

	@Override
	public void run() {
		System.out.printf("Beginning data sources loading : %s\n", new Date());
		try {
			TimeUnit.SECONDS.sleep(4);//让线程先睡4秒
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.printf("Data sources loading has finished: %s\n", new Date());
	}
	
	
	public static void main(String[] args) {
		DataSourcesLoader_1_7 dsLoader = new DataSourcesLoader_1_7();
		Thread thread1 = new Thread(dsLoader,"DataSourceThread");
		NetworkConnectionsLoader ncLoader = new NetworkConnectionsLoader();
		Thread thread2 = new Thread(ncLoader,"NetWorkConnectionsLoader");
		thread1.start();
		thread2.start();
		
		
		try {
			//等待thread1和thread2执行完毕，main才会继续执行
			thread1.join();//join(1000ms)/join(1000ms,naoTime);
			thread2.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		System.out.println("Main : configuration has been loaded: %s\n "+new Date());
	}
	
	

}

class NetworkConnectionsLoader implements Runnable{
	@Override
	public void run() {
		System.out.printf("Beginning netWork Connections loading : %s\n", new Date());
		try {
			TimeUnit.SECONDS.sleep(6);//让线程先睡6秒
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.printf("netWork Connections loading has finished: %s\n", new Date());
	}
}
