package org.andrewliu.thread.locktest;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 线程本地存储测试
 * @author de
 *
 */
public class ThreadLocalVariableHolder {

	/**
	 * ThreadLocal 对象通常当作静态域存储，在创建ThreadLocal时，你只能通过get()和set()方法来访问该对象的内容，get()方法将返回
	 * 与其线程相关联的对象的副本，而set会将参数插入到为其线程存储的对象中，并返回存储中原有的对象，ThreadLocal保证不会出理竞争条件,
	 * 他会为每个线程分配自己的存储。
	 */
	private static ThreadLocal<Integer>  value = new ThreadLocal<Integer>(){
		private Random rand = new Random(47);
		protected synchronized Integer initialValue(){
			return rand.nextInt(10000);
		}
	};
	
	
	public static void increment(){
		value.set(value.get()+1);
		
	}
	
	public static int get(){
		return value.get();
	}
	
	public static void main(String[] args) throws InterruptedException {
		ExecutorService exec = Executors.newCachedThreadPool();
		for(int i = 0; i < 5; i++){
			exec.execute(new Accessor(i));
		}
		TimeUnit.SECONDS.sleep(3);
		exec.shutdown();
	}
}


class Accessor implements Runnable{
	private final int id;
	public Accessor(int idn){
		id = idn;
	}
	@Override
	public void run() {
		while(!Thread.currentThread().isInterrupted()){
			ThreadLocalVariableHolder.increment();
			System.out.println(this);
			Thread.yield();
		}
	}
	
	public String toString(){
		return "#" + id + ":"+ ThreadLocalVariableHolder.get();
	}
	
}