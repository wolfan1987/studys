package org.andrewliu.thread.test2;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * java ���߳�---δ������쳣
 * �������run�������׳����쳣����thorws�������ͻ��˴���
 * Thread�������������쳣�������Ľӿ��磺
 * Thread.UncaughtExceptionHandler
 * �������ÿ��Thread�����϶�����һ���쳣������
 * Thread.UncaughtExceptonHandler.uncaughtException() �������߳���δ�����쳣���ٽ�����ʱ������
 * @author de
 *
 */
public class NavieExceptionHandling {

	public static void main(String[] args) {
		//�޷������쳣����
		try{
			ExecutorService exec2 = Executors.newCachedThreadPool();
			exec2.execute(new ExceptionThread());
		}catch(RuntimeException ue){//���ᵽ����ִ��
			System.out.println("Exception has been handled!");
		}
		
		//�Զ����쳣�����������̹߳�����ָ���߳��쳣�������Ĳ���
		ExecutorService exec = Executors.newCachedThreadPool(new HandlerThreadFactory());
		exec.execute(new ExceptionThread2());
		
		//����Thread��û�������쳣������������µ�Ĭ���쳣������
		Thread.setDefaultUncaughtExceptionHandler(new MyUncaughtExceptionHandler());
		//��ʱ�����µ��ã�ExcetionThread�е��쳣���Եõ�����
		ExecutorService exec1 = Executors.newCachedThreadPool();
		exec1.execute(new ExceptionThread());
	}
}

class ExceptionThread2 implements Runnable{
	public void run(){
		Thread t =  Thread.currentThread();
		System.out.println("run() by "+t);
		//�õ���ǰ�̵߳��쳣������
		System.out.println("eh = "+ t.getUncaughtExceptionHandler());
		throw new RuntimeException();
	}
}

/**
 * �����Լ����߳��쳣������
 * @author de
 *
 */
class MyUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler{

	@Override
	public void uncaughtException(Thread t, Throwable e) {
		System.out.println("thread.name="+ t.getName() + " caught exception = "+e.getMessage());
		e.printStackTrace();
	}
	
}

/**
 * ���������߳��쳣���������̹߳���
 * @author de
 *
 */
class HandlerThreadFactory implements ThreadFactory{

	@Override
	public Thread newThread(Runnable r) {
		System.out.println(this+" creating new Thread...");
		Thread t = new Thread(r);
		System.out.println("created "+ t);
		t.setUncaughtExceptionHandler(new MyUncaughtExceptionHandler());//�����̵߳��쳣������
		System.out.println("eh = "+ t.getUncaughtExceptionHandler());
		return t;
	}
	
	
	
	
}