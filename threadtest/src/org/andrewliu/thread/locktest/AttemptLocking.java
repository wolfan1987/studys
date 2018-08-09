package org.andrewliu.thread.locktest;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * ReentrantLock 锁尝试获取锁测试
 * @author de
 *
 */
public class AttemptLocking {

	private ReentrantLock lock = new ReentrantLock();
	public void untimed(){
		boolean captured = lock.tryLock();//尝试获取锁
		try{
			System.out.println(" tryLock():"+captured);
		}finally{
			if(captured){//测试是事取得了锁，取得了则释放锁
				lock.unlock();
			}
		}
	}
	
	public void timed(){
		boolean captured = false;
		try{
			captured = lock.tryLock(2, TimeUnit.SECONDS);//尝试获取锁，只尝试2秒钟
		} catch (InterruptedException ex){
		   throw new RuntimeException(ex);	
		}
		
		try{
			System.out.println("tryLock(2,TimeUnit.SECONDS):"+captured);
		}finally{
			if(captured){
				lock.unlock();
			}
		}
	}
	
	public static void main(String[] args) {
		final AttemptLocking al = new AttemptLocking();
		al.untimed();
		al.timed();
		new Thread(){
			{ setDaemon(true);}
			public void run(){
				al.lock.lock();
				System.out.println("acquired....");
			}
		}.start();
		
		Thread.yield();
		al.untimed();
		al.timed();
	}
	
}
