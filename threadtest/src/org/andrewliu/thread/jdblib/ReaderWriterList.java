package org.andrewliu.thread.jdblib;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReaderWriterList<T> {
   private ArrayList<T> lockedList;
   private ReentrantReadWriteLock lock = new ReentrantReadWriteLock(true);  //读写锁
   public ReaderWriterList(int size, T initialValue){
	   lockedList = new ArrayList<T>(Collections.nCopies(size, initialValue));  //初始化ArrayList大小
	   System.out.println("ockedList.size="+lockedList.size());
   }
   
   public T set (int index ,T element){
	   Lock wlock = lock.writeLock(); //得到写锁
	   wlock.lock();//开始锁定
	   try{
		   return lockedList.set(index, element); //在指定位置写入(更新)元素
	   }finally{
		   wlock.unlock();//释放锁
	   }
   }
   
   public T get(int index){
	   Lock rlock = lock.readLock();  //得到读乐
	   rlock.lock();//开始锁定
	   try{
		   if(lock.getReadLockCount()>1){  //测试当前总共有多少个读锁
			   System.out.println(lock.getReadLockCount());
		   }
		   return lockedList.get(index);  //返回指定索引的值
	   }finally{
		   rlock.unlock();
	   }
   }
   
   public static void main(String[] args) {
	   new ReaderWriterListTest(30,1);
   }
}


class ReaderWriterListTest{
	ExecutorService exec = Executors.newCachedThreadPool();
	private final static int SIZE = 100;
	private static Random rand = new Random(47);
	private ReaderWriterList<Integer>  list = new ReaderWriterList<Integer>(SIZE,0);  //读写List初始化大小=100,初始值=0
	private class Writer implements Runnable{
		@Override
		public void run() {
			try{
				for(int i = 0; i < 20; i++){
					list.set(i, rand.nextInt());//设置指定位置的值
					TimeUnit.MILLISECONDS.sleep(100);//每100毫秒写一次
				}
			}catch(InterruptedException e){
				System.out.println(e.getMessage());
			}
			
			System.out.println(" Write finished, shutting down");
			exec.shutdownNow();  //强制关闭所写线程
		}
		
	}
	
	
	private class Reader implements Runnable{
		
		@Override
		public void run() {
			 try{
				 while(!Thread.interrupted()){
					 for(int i = 0; i < SIZE; i++){
						 list.get(i);//读取
						 TimeUnit.MILLISECONDS.sleep(1);//每一毫秒读一次
					 }
				 }
			 }catch(InterruptedException e){
				 System.out.println(e.getMessage());
			 }
		}
		
	}
	
	public ReaderWriterListTest(int readers,int writers){
		for(int i = 0; i < readers; i++){
			exec.execute(new Reader());
		}
		for(int i = 0; i < writers; i++){
			exec.execute(new Writer());
		}
	}
	
}