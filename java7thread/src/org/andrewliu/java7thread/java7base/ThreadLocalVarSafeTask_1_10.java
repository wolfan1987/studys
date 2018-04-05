package org.andrewliu.java7thread.java7base;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * ���̼߳�û����Ҫ����Ķ�������ʱ��Ϊ�˸ɾ����ָ��Եı����������ֲ߳̾�������������̵߳ı�����������Ҳ�ȽϺá�
 * 
 * �ֲ߳̾�����Ҳ�ṩ��remove()����������ɾ����ǰ�߳��Ѵ洢��ֵ��java����API������InheritableThreadLocal�࣬
 * ���һ���߳��Ǵ������߳��д����ģ�����ཫ�ṩ�̳е�ֵ�����һ���߳�A���ֲ߳̾���������ֵ��������������ĳ���߳�Bʱ��
 * �߳�B���ֲ߳̾����������߳�A��һ���ġ����Ը���childValue()�������������������ʼ�����߳����ֲ߳̾������е�ֵ����ʹ��
 * ���߳����ֲ߳̾������е�ֵ��Ϊ���������
 * @author de
 *
 */
public class ThreadLocalVarSafeTask_1_10 implements Runnable{

	/**
	 * ����һ���ֲ߳̾���������������ΪDate�ͣ�����д��initalValue������Ϊ��Щ���������ʼֵ(�ڵ�һ����get()����������ֵʱ��Ϊ��������)
	 * ���ַ�ʽ����ı���������������������̶߳����и��Ե�ֵ�������ᱻ���̹߳������ֱ������Լ���get()��set()��������ȡ���޸�
	 * �Լ���ֵ.
	 */
	private static ThreadLocal<Date>  startDate = new ThreadLocal<Date>(){
		protected Date initialValue(){
			return new Date();
		}
	};

	@Override
	public void run() {
		System.out.printf("Sttarting Thread: %s : %s\n", Thread.currentThread().getId(),startDate.get());
		
		try {
			TimeUnit.SECONDS.sleep((int)Math.rint(Math.random()*10));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		System.out.printf("Thread Finished: %s : %s\n", Thread.currentThread().getId(),startDate.get());
	}
	
	public static void main(String[] args) {
		ThreadLocalVarSafeTask_1_10  task = new ThreadLocalVarSafeTask_1_10();
		for(int i = 0; i < 10; i ++){
			Thread thread = new Thread(task);
			thread.start();
			try{
				TimeUnit.SECONDS.sleep(2);
			}catch(InterruptedException e){
				e.printStackTrace();
			}
		}
		
	}
}
