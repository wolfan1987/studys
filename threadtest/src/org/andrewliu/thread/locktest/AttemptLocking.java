package org.andrewliu.thread.locktest;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * ReentrantLock �����Ի�ȡ������
 * @author de
 *
 */
public class AttemptLocking {

	private ReentrantLock lock = new ReentrantLock();
	public void untimed(){
		boolean captured = lock.tryLock();//���Ի�ȡ��
		try{
			System.out.println(" tryLock():"+captured);
		}finally{
			if(captured){//��������ȡ��������ȡ�������ͷ���
				lock.unlock();
			}
		}
	}
	
	public void timed(){
		boolean captured = false;
		try{
			captured = lock.tryLock(2, TimeUnit.SECONDS);//���Ի�ȡ����ֻ����2����
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
