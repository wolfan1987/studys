package org.andrewliu.thread.jdblib;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class SemaphoreDemo {

	final static int SIZE = 25;
	public static void main(String[] args) throws InterruptedException {
		//构造一个大小为25的Fat的对象迟
		final Pool<Fat> pool = new Pool<Fat>(Fat.class,SIZE);
		ExecutorService exec = Executors.newCachedThreadPool();
		for(int i = 0; i < SIZE; i++){
			exec.execute(new CheckoutTask<Fat>(pool));
		}
		System.out.println("All CheckoutTasks created!");
		List<Fat>  list = new ArrayList<Fat>();
		for(int i = 0; i < SIZE; i++){
			Fat f = pool.checkOut();  //签出对象
			System.out.println(i+ " : main() thread checked out");
			f.operation();
			list.add(f);
		}
		
		Future<?>  blocked = exec.submit(new Runnable(){  //不断的签出对象
			public void run(){
				 try {
					pool.checkOut();
				} catch (InterruptedException e) {
					System.out.println("checkout() Interrupted!");
				}
			}
		});
		
		TimeUnit.SECONDS.sleep(2);
		blocked.cancel(true);  //2秒后中断签出
		System.out.println(" Checking in objects in "+ list);
		for(Fat f : list){  //开始将所有对象签入
			pool.checkIn(f);
		}
		for(Fat f : list){
			pool.checkIn(f);  //重复的签入，将忽略掉
		}
		exec.shutdown();
	}
}


class CheckoutTask<T> implements Runnable{
	private static int counter = 0;
	private final int id = counter++;
	private  Pool<T>  pool;
	public CheckoutTask(Pool<T> pool){
		this.pool = pool;
	}
	@Override
	public void run() {
		try{
			//签出对象
			 T item = pool.checkOut();
			 System.out.println(this + "checked out "+item);
			 //过1秒后，再签入
			 TimeUnit.SECONDS.sleep(1);
			 System.out.println(this+"checking in "+ item);
			 pool.checkIn(item);
		}catch(InterruptedException e){
			System.out.println(e.getMessage());
		}
	}
	
	public String toString(){
		return "checkoutTask" + id + "  ";
	}
	
}