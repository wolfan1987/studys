package org.andrewliu.java7thread.java7conlist;

import java.util.Date;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * DelayQueue���ӳٶ����̰߳�ȫ�б� ������Դ�Ŵ��м������ڵ�Ԫ�ء������÷��������з��� ����ȡԪ��ʱ��δ����Ԫ�����ڽ���
 * ���ԡ���ЩԪ�ض�����Щ�����ǲ��ɼ��ġ�
 * ��ŵ�delayQueue���е�Ԫ�ر���̳�Delayed�ӿڣ�����ʹ�����Ϊ�ӳٶ���ʹ������DelyQueue�еĶ�������˼������ڣ������������ڵ�ʱ�䡣
 * �ýӿ�ǿ��ִ����������������
 * compareTo(Delayed o ) ���Ƚ��ӳٶ���ʱ��ֵ 
 * getDelay(TimeUnit unit);  ���ؼ������ڵ�ʣ��ʱ�䡣
 * clear():  �Ƴ�����������Ԫ�أ�
 * offer(E e): E��DelayQueue�ķ��Ͳ�������ʾ������������͡���������Ѳ�����Ӧ��Ԫ�ز��뵽�����С�
 * peek():  ���ض����еĵ�һ��Ԫ�أ����������Ƴ���
 * take(): ���ض����еĵ�һ��Ԫ�أ��������Ƴ����������Ϊ�գ��߳̽�������ֱ���������п���Ԫ��.
 * poll(): ���ص�һ��Ԫ�أ����Ƴ�
 * add()�� ����Ԫ��
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
			threads[i].start(); //������ʼ��Ԫ���߳�
		}
		for ( int i = 0; i < threads.length; i++){
			threads[i].join();  //�����Ԫ�ص��߳�ȫ��ִ����
		}
		
		do{
			int counter = 0;
			DelayedEvent event ;
			do{
				event = queue.poll();  //ȡ��Ԫ�أ�ͳ��Ԫ�ظ���.
				if(event!=null) counter++;
			}while(event!=null);
			System.out.printf("At %s you have read %d events\n", new Date(),counter);
			TimeUnit.MILLISECONDS.sleep(500);
		}while(queue.size()>0);
	}
	
}
/**
 * Ҫ��ӵ��ӳٶ����е��¼��������Լ��ṩʱ��Ƚϼ�ʱ�䳤�̼��㷽��
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
 * ���ӳٶ������Ԫ�ص�����
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