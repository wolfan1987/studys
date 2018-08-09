package org.andrewliu.thread.test;

import java.util.concurrent.TimeUnit;

/**
 * 后台线程测试，后台线程产生的线程只能是后台线程，
 * 当非后台线程关闭，则后台线程也会关闭，即：非存后存，非死后死
 * 当非后台线程结束时，后台线程如果还没执行完，则后台线程的finally方法是不会执行的
 * @author de
 *
 */
public class SimpleDaemons  implements Runnable{

	@Override
	public void run() {
		while(true){
			try {
				TimeUnit.MILLISECONDS.sleep(200);
				System.out.println(Thread.currentThread() + "  "+this);
			} catch (InterruptedException e) {
				System.out.println("sleep() interrupted....");
				e.printStackTrace();
			}
			
		}
	}
	
	public static void main(String[] args) throws InterruptedException {
		for(int i = 0; i < 10; i++){
			Thread daemon = new Thread(new SimpleDaemons());
			daemon.setDaemon(true);//将线程设为后台线程
			daemon.start();
		}
		System.out.println("All daemons started...");
		TimeUnit.MILLISECONDS.sleep(300);//启动所有后台线程后main线程休眠300秒,以便后台线程执行完
		System.out.println("All thread  endding...");
	}

}
