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
		//����һ����СΪ25��Fat�Ķ����
		final Pool<Fat> pool = new Pool<Fat>(Fat.class,SIZE);
		ExecutorService exec = Executors.newCachedThreadPool();
		for(int i = 0; i < SIZE; i++){
			exec.execute(new CheckoutTask<Fat>(pool));
		}
		System.out.println("All CheckoutTasks created!");
		List<Fat>  list = new ArrayList<Fat>();
		for(int i = 0; i < SIZE; i++){
			Fat f = pool.checkOut();  //ǩ������
			System.out.println(i+ " : main() thread checked out");
			f.operation();
			list.add(f);
		}
		
		Future<?>  blocked = exec.submit(new Runnable(){  //���ϵ�ǩ������
			public void run(){
				 try {
					pool.checkOut();
				} catch (InterruptedException e) {
					System.out.println("checkout() Interrupted!");
				}
			}
		});
		
		TimeUnit.SECONDS.sleep(2);
		blocked.cancel(true);  //2����ж�ǩ��
		System.out.println(" Checking in objects in "+ list);
		for(Fat f : list){  //��ʼ�����ж���ǩ��
			pool.checkIn(f);
		}
		for(Fat f : list){
			pool.checkIn(f);  //�ظ���ǩ�룬�����Ե�
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
			//ǩ������
			 T item = pool.checkOut();
			 System.out.println(this + "checked out "+item);
			 //��1�����ǩ��
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