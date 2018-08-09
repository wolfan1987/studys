package org.andrewliu.thread.locktest;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 装饰性花园示例，演示线程的取消和等待
 * @author de
 *
 */
public class OranamentalGarden {

	public static void main(String[] args) throws InterruptedException {
		ExecutorService exec = Executors.newCachedThreadPool();
		for( int i = 0; i < 5; i++){//启动每个门的计算线程
			exec.execute(new Entrance(i));
		}
		
		TimeUnit.SECONDS.sleep(3);//统计3秒
		Entrance.cancel();//停止计算
		exec.shutdown();//终止线程
		if(!exec.awaitTermination(250, TimeUnit.MILLISECONDS)){//等待所有线程终止,如果在250少之前全部结束则返回true，否则返回false，表示不是所有任务都已经结束
			System.out.println("Some tasks were not terminated!");
		}
		System.out.println("Total : "+ Entrance.getTotalCount());//还可以访问静态方法与变量，统计在线程中的值
		System.out.println("Sum of Entrances : "+ Entrance.sumEntrances());
	}
	
	
	
}

class Count{
	private int count = 0;
	private Random rand = new Random(47);
	public synchronized int increment(){
		int temp = count;
		if(rand.nextBoolean()){
			Thread.yield();
		}
		
		return (count=++temp);
	}
	
	public synchronized int value(){
		return count;
	}
}


class Entrance implements Runnable{
	private static Count count = new Count();
	private static List<Entrance>  entrances = new ArrayList<Entrance>();
	private int number = 0;
	private final int id ;
	private static volatile boolean canceled = false;
	public static void cancel(){
		canceled = true;
	}
	public Entrance(int id){
		this.id = id;
		entrances.add(this);
	}
	@Override
	public void run() {
		while(!canceled){
			synchronized(this){
				++number;
			}
			System.out.println(this+"Total : "+ count.increment());
			
			try {
				TimeUnit.MILLISECONDS.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		System.out.println("Stopping "+this);
		
	}
	
	public synchronized int getValue(){
		return number;
	}
	public String toString(){
		return "Entrance " + id + ":  "+getValue();
	}
	
	
	public static int getTotalCount(){
		return count.value();
	}
	
	public static int sumEntrances(){
		int sum = 0;
		for(Entrance entrance : entrances){
			sum+=entrance.getValue();
		}
		return sum;
	}
}
