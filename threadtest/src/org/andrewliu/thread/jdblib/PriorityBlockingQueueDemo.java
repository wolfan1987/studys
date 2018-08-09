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
		PriorityBlockingQueue<Runnable> queue = new PriorityBlockingQueue<Runnable>();//�������ȼ���������
		exec.execute(new PrioritizedTaskProducer(queue,exec));//��������
		exec.execute(new PrioritizedTaskConsumer(queue));//��������
	}
}

/**
 * ���м������е�����
 * @author de
 *
 */
class PrioritizedTask implements Runnable,Comparable<PrioritizedTask>{

	private Random rand = new Random(47);
	private static int counter = 0;
	private final int id = counter++;
	private final int priority;//���ڱȽ������˳��
	protected static List<PrioritizedTask> sequence = new ArrayList<PrioritizedTask>();
	public PrioritizedTask(int priority){
		this.priority = priority;
		sequence.add(this);
	}
	@Override
	public int compareTo(PrioritizedTask o) {  //ʵ�ֱȽϵķ���
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
	
	//һ���̳�PrioritizedTask�Ķ������������ȼ����-1���������ִ��
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
 * ��������������
 * @author de
 *
 */
class PrioritizedTaskProducer implements Runnable{

	private Random rand = new Random(47);
	//������У�����ָ��PriorityBlockingQueue
	private Queue<Runnable> queue;
	private ExecutorService exec;//���ȼ�Ϊ-1����ͣ�������ִ��ʱ�õ�ִ��������ִ���꽫shutdown
	public PrioritizedTaskProducer(Queue<Runnable> q,ExecutorService e){
		queue = q;
		exec = e;
	}
	@Override
	public void run() {
		for(int i = 0; i < 20 ; i++){
			queue.add(new PrioritizedTask(rand.nextInt(10)));////������ȼ�����
			Thread.yield();
		}
		
		try{
			for( int i = 0; i < 10; i++){
				TimeUnit.MILLISECONDS.sleep(250);
				queue.add(new PrioritizedTask(10)); //���ȼ�Ϊ10������
			}
			
			for(int i = 0; i < 10; i++){
				queue.add(new PrioritizedTask(i));//1-10�����ȼ�
			}
			queue.add(new PrioritizedTask.EndSentinel(exec));//-1���ȼ�������
			
		}catch(InterruptedException e){
			System.out.println(e.getMessage());
		}
		System.out.println("Finished PrioritizedTaskProducer!");
	}
	
}

/**
 * ���Ѷ����������
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
			try {//���û���жϣ����ϵĴӶ������ó���������
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
