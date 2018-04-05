package org.andrewliu.java7thread.java7base;

import java.util.ArrayDeque;
import java.util.Date;
import java.util.Deque;
import java.util.concurrent.TimeUnit;


/**
 * 守护线程：
 * 守护线程的优先级很低，通常，当同一个应用程序里没有其他的线程运行的时候，守护线程才运行，当守护线程是程序中唯一的线程时，
 * 守护线程执行结束后，JVM也就结束了这个应用程序。它被用来做为同一程序中普通线程的服务提供者。它们通常无限循环，以等待服务请求或者执行线程
 * 的任务，它们不能做重要的工作，因为不知道的守护线程什么时候能得到CPU进钟，并且在没有其他线程运行的时候，守护线程随时可能结束。（如：垃圾回收线程）
 * Thread的setDaemon(true)方法只能在start()方法调用之前设置，线程开始之后，不能再修改其状态
 * Thread的isDaemo()方法可以用来测试当前线程是否为守护线程，true表示是。
 * @author de
 *
 */
public class WriterTask_1_8 implements Runnable{

	private Deque<Event> deque;
	public WriterTask_1_8(Deque<Event> deque){
		this.deque = deque;
	}
	
	@Override
	public void run() {
		for ( int i = 1; i < 100; i++){
			Event event = new Event();
			event.setDate(new Date());
			event.setEvent(String.format("Thread %s has generated an event",Thread.currentThread().getId()));
			deque.addFirst(event);
			try {
				TimeUnit.SECONDS.sleep(1); //每隔1秒添加一个Event到双端队列中
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	
	public static void main(String[] args) {
		Deque<Event>  deque = new ArrayDeque<Event>();
		WriterTask_1_8  writer = new WriterTask_1_8(deque);
		for(int i = 0; i < 3; i++){
			Thread thread = new Thread(writer);//启动3个生产Even的线程
			thread.start();
		}
		CleanerTask cleaner = new CleanerTask(deque); //启动一个为守护线程的消费线程
		System.out.println("CleanerTask.isDaemon="+cleaner.isDaemon());
		cleaner.start();
	}

}

/**
 * 不断删除队列中元素
 * @author de
 *
 */
class CleanerTask extends Thread{
	private Deque<Event> deque;
	public CleanerTask(Deque<Event> deque){
		this.deque = deque;
		setDaemon(true);
	}
	
	@Override
	public void run(){
		while(true){
			Date date = new Date();
			clean(date);
		}
	}
	private void clean(Date date){
		long difference;
		boolean delete;
		if(deque.size() == 0){
			return ;
		}
		delete = false;
		do{
			Event e = deque.getLast();//得到最后一个Event
			difference = date.getTime() - e.getDate().getTime();
			if(difference > 10000){
				System.out.printf("cleaner : %s\n", e.getEvent());
				deque.removeLast();
				delete = true;
			}
		}while(difference > 10000);
		
		if(delete){
			System.out.printf("Cleaner: Size of the queue: %d\n", deque.size());
		}
	}
	
}

class Event{
	private String event;
	private Date date;
	public String getEvent() {
		return event;
	}
	public void setEvent(String event) {
		this.event = event;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	
}
