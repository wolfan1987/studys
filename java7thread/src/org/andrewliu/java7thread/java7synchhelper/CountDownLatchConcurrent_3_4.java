package org.andrewliu.java7thread.java7synchhelper;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * 演示: CountDownLatch同步并发类
 * 它是一个同步辅助类，在完成一组正在其他线程中执行的操作之前，它允许线程一直等待，这个类使用一个整数进行初始化，这个整数就是要
 * 等待完成的操作的数目，当一个线程要等待某些操作先执行完时，需要调用await()方法，这个方法让线程进入休眠直到等待的所有操作都
 * 完成。当某一个操作完成后，它将调用countDown()方法将CuntDownLatch类的内部计数器减1,当计数器变成0的时候，CountDownLatch类
 * 将唤醒所有调用await()方法而进入休眠的线程.他不是用来保护共享资源或者临界区的，而是用来同步执行多个任务的一个或者多个线程。
 * CountDownLatch只准许进入一次，当其内部计数器为0时，再调用其方法将不再起作用，要想能同步需新的CountDownLatch对象.
 * @author de
 *
 */
public class CountDownLatchConcurrent_3_4 {

	public static void main(String[] args) {
		//会议室
		Videoconference conference = new Videoconference(10);
		Thread threadConference = new Thread(conference);
		//会议室线程启动
		threadConference.start();
		
		for(int i = 0; i < 10; i++){
			//参与人员开始进入
			Participant p = new Participant(conference,"Participant"+i);
			Thread t = new Thread(p);
			t.start();
		}
	}
}

class Videoconference implements Runnable{
	private final CountDownLatch controller;
	
	public Videoconference(int number){
		//number指定并发操作个数
		controller = new CountDownLatch(number);
	}
	
	public void arrive(String name){
		System.out.printf("%s has arrived.",name);
		controller.countDown();
		System.out.printf("VideoConference: Waiting for %d participants.\n", controller.getCount());  //打印总共有多少个并发操作任务
	}
	
	@Override
	public void run() {
		System.out.printf("VideoConference: Initialization: %d participants.\n",controller.getCount());
		try{
			controller.await();
			System.out.printf("VideoConference: All the  participants have come\n");
			System.out.printf("VideoConference: Let's start....\n");
		}catch(InterruptedException e){
			e.printStackTrace();
		}
	}
	
}

class Participant implements Runnable{
	private Videoconference conference;
	private String name;
	public Participant(Videoconference conference,String name){
		this.conference = conference;
		this.name = name;
	}
	
	@Override
	public void run() {
		long duration = (long)(Math.random()*10);
		try{
			TimeUnit.SECONDS.sleep(duration);
		}catch(InterruptedException e){
			e.printStackTrace();
		}
		
		conference.arrive(name);
	}
}



