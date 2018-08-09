package org.andrewliu.thread.jdblib;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.NANOSECONDS;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


//�ӳٶ�����ʾ
public class DelayQueueDemo  {
	public static void main(String[] args) {
		Random rand = new Random(47);
		ExecutorService exec = Executors.newCachedThreadPool();
		DelayQueue<DelayedTask> queue = new DelayQueue<DelayedTask>();
		for(int i = 0 ; i < 20; i++){
			queue.put(new DelayedTask(rand.nextInt(5000)));//��ӷ��ʱ������
		}
		queue.add(new DelayedTask.EndSentinel(5000, exec));//��ӵ���ʱ������񣬽�������ִ��
		exec.execute(new DelayedTaskConsumer(queue));
	}
}

//������ʵ��Runnable��delayed�ӿ�
class DelayedTask implements Runnable,Delayed{

	private static int counter = 0;
	private final int id = counter++;
	private final int delta;
	private final long trigger;
	protected static List<DelayedTask> sequence = new ArrayList<DelayedTask>();
	public DelayedTask(int delayInMilliseconds){
		delta = delayInMilliseconds;//�ӳ�ʱ��
		trigger = System.nanoTime()+NANOSECONDS.convert(delta,MILLISECONDS);//����ʱ��
		sequence.add(this);//�����������list
	}
	@Override  //������Ҫ������
	public void run() {
		System.out.println(this+"---HaHa!");
	}
    
	@Override   //ʵ��ʱ��ȽϷ���
	public int compareTo(Delayed o) {
		DelayedTask that = (DelayedTask)o;
		if(trigger < that.trigger) return -1;
		if(trigger > that.trigger) return 1;
		return 0;
	}

	@Override   //ʵ�ֵõ��ӳ�ʱ�䷽��
	public long getDelay(TimeUnit unit) {
		return unit.convert(trigger-System.nanoTime(), NANOSECONDS);
	}
	
	
	public String toString(){
		return String .format("[%1%-4d]",delta)+" Task "+id;
	}
	
	public String summary(){
		return "("+id+" : "+ delta + ")";
	}
	//�ӳ�ʱ��������񣬵���ʵ��,�̳���DelayedTask��
	public static class EndSentinel extends DelayedTask{
		private ExecutorService exec;
		public EndSentinel(int delay,ExecutorService e){
			super(delay);
			exec = e;
		}
		public void run(){
			for(DelayedTask pt : sequence){
				System.out.println( pt.summary() + "   ");
			}
			System.out.println();
			System.out.println(this + "Calling shutdownNow()");
			exec.shutdownNow();
		}
	}
}

class DelayedTaskConsumer implements Runnable{
	private DelayQueue<DelayedTask> q;
	public DelayedTaskConsumer(DelayQueue<DelayedTask> q){
		this.q = q;
	}
	
	@Override
	public void run() {
			try {
				while(!Thread.interrupted()){
				q.take().run();//ֱ�ӵ��������run����
				}
			} catch (InterruptedException e) {
				System.out.println(e.getMessage());
			}
			
			System.out.println(" Finished DelayedTaskConsumer!");
	}
	
}
