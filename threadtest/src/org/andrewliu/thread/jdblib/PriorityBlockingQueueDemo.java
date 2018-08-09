package org.andrewliu.thread.jdblib;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;

public class PriorityBlockingQueueDemo  {

	public static void main(String[] args) {
		Random rand = new Random(47);
		ExecutorService exec = Executors.newCachedThreadPool();
		PriorityBlockingQueue<Runnable> queue = new PriorityBlockingQueue<Runnable>();//公共优先级阻塞队列
		exec.execute(new PrioritizedTaskProducer(queue,exec));//生产任务
		exec.execute(new PrioritizedTaskConsumer(queue));//消费任务
	}
}

/**
 * 优行级队列中的任务
 * @author de
 *
 */
class PrioritizedTask implements Runnable,Comparable<PrioritizedTask>{

	private Random rand = new Random(47);
	private static int counter = 0;
	private final int id = counter++;
	private final int priority;//用于比较任务的顺序
	protected static List<PrioritizedTask> sequence = new ArrayList<PrioritizedTask>();
	public PrioritizedTask(int priority){
		this.priority = priority;
		sequence.add(this);
	}
	@Override
	public int compareTo(PrioritizedTask o) {  //实现比较的方法
		return priority < o.priority ? 1 : (priority > o.priority ? -1 : 0);
	}
	

	@Override
	public void run() {
		try{
			TimeUnit.MILLISECONDS.sleep(rand.nextInt(250));
		}catch(InterruptedException e){
			System.out.println(e.getMessage());
		}
		System.out.println(this);
	}
	
	public String toString(){
		return String.format("[%1$-3d]",priority)+ " Task  "+ id;
	}
	
	public String summary(){
		return "("+id+" : "+priority+")";
	}
	
	//一个继承PrioritizedTask的队列任务，其优先级最低-1，将会最后执行
	public static class EndSentinel extends PrioritizedTask{
		private ExecutorService exec;
		public EndSentinel(ExecutorService e) {
			super(-1);
			exec = e;
		}
		
		public void run(){
			int count = 0;
			for(PrioritizedTask pt : sequence){
				System.out.println(pt.summary());
				if(++count % 5 == 0){
					System.out.println();
				}
			}
			System.out.println();
			System.out.println(this + " Calling shutdownNow()");
			exec.shutdownNow();
		}
		
	}
}

/**
 * 队列任务生产者
 * @author de
 *
 */
class PrioritizedTaskProducer implements Runnable{

	private Random rand = new Random(47);
	//抽象队列（用于指向PriorityBlockingQueue
	private Queue<Runnable> queue;
	private ExecutorService exec;//优先级为-1（最低）的任务执行时用的执行器，其执行完将shutdown
	public PrioritizedTaskProducer(Queue<Runnable> q,ExecutorService e){
		queue = q;
		exec = e;
	}
	@Override
	public void run() {
		for(int i = 0; i < 20 ; i++){
			queue.add(new PrioritizedTask(rand.nextInt(10)));////随机优先级任务
			Thread.yield();
		}
		
		try{
			for( int i = 0; i < 10; i++){
				TimeUnit.MILLISECONDS.sleep(250);
				queue.add(new PrioritizedTask(10)); //优先级为10的任务
			}
			
			for(int i = 0; i < 10; i++){
				queue.add(new PrioritizedTask(i));//1-10级优先级
			}
			queue.add(new PrioritizedTask.EndSentinel(exec));//-1优先级的任务
			
		}catch(InterruptedException e){
			System.out.println(e.getMessage());
		}
		System.out.println("Finished PrioritizedTaskProducer!");
	}
	
}

/**
 * 消费队列任务对象
 * @author de
 *
 */
class PrioritizedTaskConsumer implements Runnable{

	private PriorityBlockingQueue<Runnable> q ;
	public PrioritizedTaskConsumer( PriorityBlockingQueue<Runnable> q){
		this.q = q;
	}
	@Override
	public void run() {
			try {//如果没有中断，不断的从队列中拿出任务消费
				while(!Thread.interrupted()){
				  q.take().run();
				}
			} catch (InterruptedException e) {
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
		System.out.println("Finished PrioritizedTaskConsumer!");
	}
	
}
