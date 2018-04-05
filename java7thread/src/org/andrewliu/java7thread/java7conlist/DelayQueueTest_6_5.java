package org.andrewliu.java7thread.java7conlist;

import java.util.Date;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * DelayQueue：延迟队列线程安全列表。 此类可以存放带有激活日期的元素。当调用方法队列中返回 或提取元素时，未来的元素日期将被
 * 忽略。这些元素对于这些方法是不可见的。
 * 存放到delayQueue类中的元素必须继承Delayed接口，它可使对象成为延迟对象，使存入在DelyQueue中的对象具有了激活日期，即到激活日期的时间。
 * 该接口强制执行下列两个方法：
 * compareTo(Delayed o ) ：比较延迟对象时间值 
 * getDelay(TimeUnit unit);  返回激活日期的剩余时间。
 * clear():  移除队列中所有元素；
 * offer(E e): E是DelayQueue的泛型参数，表示传入参数的类型。这个方法把参数对应的元素插入到队列中。
 * peek():  返回队列中的第一个元素，但不将其移除。
 * take(): 返回队列中的第一个元素，并将其移除，如果队列为空，线程将被阻塞直到队列中有可用元素.
 * poll(): 返回第一个元素，并移除
 * add()： 存入元素
 * @author de
 *
 */
public class DelayQueueTest_6_5 {

	public static void main(String[] args) throws InterruptedException {
		DelayQueue<DelayedEvent> queue = new DelayQueue<>();
		Thread threads[] = new Thread[5];
		for(int i = 0; i < threads.length;i++){
			DelayedTask  task = new DelayedTask(i+1, queue);
			threads[i] = new Thread(task);
		}
		for( int i = 0; i < threads.length;i++){
			threads[i].start(); //启动初始化元素线程
		}
		for ( int i = 0; i < threads.length; i++){
			threads[i].join();  //让添加元素的线程全部执行完
		}
		
		do{
			int counter = 0;
			DelayedEvent event ;
			do{
				event = queue.poll();  //取出元素，统计元素个数.
				if(event!=null) counter++;
			}while(event!=null);
			System.out.printf("At %s you have read %d events\n", new Date(),counter);
			TimeUnit.MILLISECONDS.sleep(500);
		}while(queue.size()>0);
	}
	
}
/**
 * 要添加到延迟队列中的事件对象，其自己提供时间比较及时间长短计算方法
 * @author de
 *
 */
class DelayedEvent  implements Delayed{
	private Date startDate;
	
	public DelayedEvent(Date startDate){
		this.startDate = startDate;
	}

	@Override
	public int compareTo(Delayed o) {
		long result = this.getDelay(TimeUnit.NANOSECONDS)-o.getDelay(TimeUnit.NANOSECONDS);
		if(result < 0 ){
			return -1;
		}else if(result >0){
			return 1;
		}else{
			return 0;
		}
	}

	@Override
	public long getDelay(TimeUnit unit) {
		Date now = new Date();
		long diff = startDate.getTime()-now.getTime();
		return unit.convert(diff, TimeUnit.MILLISECONDS);
	}
	
}
/**
 * 往延迟队列添加元素的任务
 * @author de
 *
 */
class DelayedTask implements Runnable{
	private int id;
	private DelayQueue<DelayedEvent> queue;
	
	public DelayedTask(int id, DelayQueue<DelayedEvent> queue) {
		super();
		this.id = id;
		this.queue = queue;
	}

	@Override
	public void run() {
		Date now = new Date();
		Date delay = new Date();
		delay.setTime(now.getTime()+(id*1000));
		System.out.printf("Thread %s: %s\n", id,delay);
		
		for(int i = 0; i < 100; i++){
			DelayedEvent event = new DelayedEvent(delay);
			queue.add(event);
		}
	}
	
	
}