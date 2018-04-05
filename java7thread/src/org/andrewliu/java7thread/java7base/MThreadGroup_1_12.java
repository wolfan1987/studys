package org.andrewliu.java7thread.java7base;

import java.util.Random;

/**
 * ʵ���Լ����߳��飬��ʵ���߳����δ�����쳣����������ʱ���������������߳̾Ϳ���ʹ�ô��쳣���������μ���ExceptonHandler_1_9.java
 * @author de
 *
 */
public class MThreadGroup_1_12  extends ThreadGroup{

	public MThreadGroup_1_12(String name) {
		super(name);
	}
	
	@Override
	public void uncaughtException(Thread t,Throwable e){
		System.out.printf("The thread %s has throw an exception\n", t.getId());
		e.printStackTrace(System.out);
		System.out.printf("Terminating the rest of the Threads\n");
		//��ӡ��Ϣ���ж��߳����������߳�
		interrupt();
	}

	public static void main(String[] args) {
		MThreadGroup_1_12 threadGroup = new MThreadGroup_1_12("MyThreadGroup");
		ThreadGroupTask  task = new ThreadGroupTask();
		for (int i = 0 ; i < 2; i++){
			//����2���̣߳����е�����Ϊtask,����������ͬһ���߳��飨MyThreadGroup)���������ڲ����߳����쳣�������Ƿ�ᴦ���쳣
			Thread t = new Thread(threadGroup,task);
			t.start();
		}
	}
	
}
/**
 * һ��������MyThreadGroup��Ĳ�������
 * @author de
 *
 */
class ThreadGroupTask implements Runnable{

	@Override
	public void run() {
		int result ;
		Random random = new Random(Thread.currentThread().getId());
		while(true){
			result = 1000/((int)(random.nextDouble()*1000));
			System.out.printf("%s : %d\n", Thread.currentThread().getId(),result);
			if(Thread.currentThread().isInterrupted()){
				System.out.printf("%d : interrupted\n",Thread.currentThread().getId());
				return;
			}
		}
	}
	
}
