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


//延迟队列演示
public class DelayQueueDemo  {
	public static void main(String[] args) {
		Random rand = new Random(47);
		ExecutorService exec = Executors.newCachedThreadPool();
		DelayQueue<DelayedTask> queue = new DelayQueue<DelayedTask>();
		for(int i = 0 ; i < 20; i++){
			queue.put(new DelayedTask(rand.nextInt(5000)));//添加非最长时间任务
		}
		queue.add(new DelayedTask.EndSentinel(5000, exec));//添加到期时间最长任务，将其优先执行
		exec.execute(new DelayedTaskConsumer(queue));
	}
}

//让任务实现Runnable和delayed接口
class DelayedTask implements Runnable,Delayed{

	private static int counter = 0;
	private final int id = counter++;
	private final int delta;
	private final long trigger;
	protected static List<DelayedTask> sequence = new ArrayList<DelayedTask>();
	public DelayedTask(int delayInMilliseconds){
		delta = delayInMilliseconds;//延长时间
		trigger = System.nanoTime()+NANOSECONDS.convert(delta,MILLISECONDS);//触发时间
		sequence.add(this);//将此任务加入list
	}
	@Override  //任务中要做的事
	public void run() {
		System.out.println(this+"---HaHa!");
	}
    
	@Override   //实现时间比较方法
	public int compareTo(Delayed o) {
		DelayedTask that = (DelayedTask)o;
		if(trigger < that.trigger) return -1;
		if(trigger > that.trigger) return 1;
		return 0;
	}

	@Override   //实现得到延长时间方法
	public long getDelay(TimeUnit unit) {
		return unit.convert(trigger-System.nanoTime(), NANOSECONDS);
	}
	
	
	public String toString(){
		return String .format("[%1%-4d]",delta)+" Task "+id;
	}
	
	public String summary(){
		return "("+id+" : "+ delta + ")";
	}
	//延长时间最长的任务，单独实现,继承了DelayedTask类
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
				q.take().run();//直接调用任务的run方法
				}
			} catch (InterruptedException e) {
				System.out.println(e.getMessage());
			}
			
			System.out.println(" Finished DelayedTaskConsumer!");
	}
	
}
