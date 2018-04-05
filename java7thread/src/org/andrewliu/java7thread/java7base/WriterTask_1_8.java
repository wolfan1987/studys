package org.andrewliu.java7thread.java7base;

import java.util.ArrayDeque;
import java.util.Date;
import java.util.Deque;
import java.util.concurrent.TimeUnit;


/**
 * �ػ��̣߳�
 * �ػ��̵߳����ȼ��ܵͣ�ͨ������ͬһ��Ӧ�ó�����û���������߳����е�ʱ���ػ��̲߳����У����ػ��߳��ǳ�����Ψһ���߳�ʱ��
 * �ػ��߳�ִ�н�����JVMҲ�ͽ��������Ӧ�ó�������������Ϊͬһ��������ͨ�̵߳ķ����ṩ�ߡ�����ͨ������ѭ�����Եȴ������������ִ���߳�
 * ���������ǲ�������Ҫ�Ĺ�������Ϊ��֪�����ػ��߳�ʲôʱ���ܵõ�CPU���ӣ�������û�������߳����е�ʱ���ػ��߳���ʱ���ܽ��������磺���������̣߳�
 * Thread��setDaemon(true)����ֻ����start()��������֮ǰ���ã��߳̿�ʼ֮�󣬲������޸���״̬
 * Thread��isDaemo()���������������Ե�ǰ�߳��Ƿ�Ϊ�ػ��̣߳�true��ʾ�ǡ�
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
				TimeUnit.SECONDS.sleep(1); //ÿ��1�����һ��Event��˫�˶�����
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	
	public static void main(String[] args) {
		Deque<Event>  deque = new ArrayDeque<Event>();
		WriterTask_1_8  writer = new WriterTask_1_8(deque);
		for(int i = 0; i < 3; i++){
			Thread thread = new Thread(writer);//����3������Even���߳�
			thread.start();
		}
		CleanerTask cleaner = new CleanerTask(deque); //����һ��Ϊ�ػ��̵߳������߳�
		System.out.println("CleanerTask.isDaemon="+cleaner.isDaemon());
		cleaner.start();
	}

}

/**
 * ����ɾ��������Ԫ��
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
			Event e = deque.getLast();//�õ����һ��Event
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
