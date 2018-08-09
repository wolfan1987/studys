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
   private ReentrantReadWriteLock lock = new ReentrantReadWriteLock(true);  //��д��
   public ReaderWriterList(int size, T initialValue){
	   lockedList = new ArrayList<T>(Collections.nCopies(size, initialValue));  //��ʼ��ArrayList��С
	   System.out.println("ockedList.size="+lockedList.size());
   }
   
   public T set (int index ,T element){
	   Lock wlock = lock.writeLock(); //�õ�д��
	   wlock.lock();//��ʼ����
	   try{
		   return lockedList.set(index, element); //��ָ��λ��д��(����)Ԫ��
	   }finally{
		   wlock.unlock();//�ͷ���
	   }
   }
   
   public T get(int index){
	   Lock rlock = lock.readLock();  //�õ�����
	   rlock.lock();//��ʼ����
	   try{
		   if(lock.getReadLockCount()>1){  //���Ե�ǰ�ܹ��ж��ٸ�����
			   System.out.println(lock.getReadLockCount());
		   }
		   return lockedList.get(index);  //����ָ��������ֵ
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
	private ReaderWriterList<Integer>  list = new ReaderWriterList<Integer>(SIZE,0);  //��дList��ʼ����С=100,��ʼֵ=0
	private class Writer implements Runnable{
		@Override
		public void run() {
			try{
				for(int i = 0; i < 20; i++){
					list.set(i, rand.nextInt());//����ָ��λ�õ�ֵ
					TimeUnit.MILLISECONDS.sleep(100);//ÿ100����дһ��
				}
			}catch(InterruptedException e){
				System.out.println(e.getMessage());
			}
			
			System.out.println(" Write finished, shutting down");
			exec.shutdownNow();  //ǿ�ƹر���д�߳�
		}
		
	}
	
	
	private class Reader implements Runnable{
		
		@Override
		public void run() {
			 try{
				 while(!Thread.interrupted()){
					 for(int i = 0; i < SIZE; i++){
						 list.get(i);//��ȡ
						 TimeUnit.MILLISECONDS.sleep(1);//ÿһ�����һ��
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