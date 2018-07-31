package org.andrewliu.jvm.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * �߳���ѭ�����߳����ȴ���ʾ
 * @author zjhtadmin
 *
 */
public class ThreadWaitTest {

	/**
	 * �߳���ѭ����ʾ
	 */
	public static void createBusyThread(){
		Thread thread = new Thread(new Runnable(){

			@Override
			public void run() {
				while(true)  //��15��
					;
			}
			
		},"testBusyThread");
		thread.start();
	}
	
	/**
	 * �߳����ȴ���ʾ
	 * @param lock
	 */
	public static void createLockThread(final Object lock){
		Thread thread = new Thread(new Runnable(){

			@Override
			public void run() {
				synchronized(lock){
					try{
						lock.wait();
					}catch(InterruptedException e){
						e.printStackTrace();
					}
				}
				
			}
			
		},"testLockThread");
		thread.start();
	}
	
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		br.readLine();
		createBusyThread();
		br.readLine();
		Object obj = new Object();
		createLockThread(obj);
	}
	
}
