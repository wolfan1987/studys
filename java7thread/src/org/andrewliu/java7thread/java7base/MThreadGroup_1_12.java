package org.andrewliu.java7thread.java7base;

import java.util.Random;

/**
 * 实现自己的线程组，并实现线程组的未捕获异常处理方法，到时其它属于这个组的线程就可以使用此异常处理器，参见：ExceptonHandler_1_9.java
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
		//打印信息后中断线程组中其它线程
		interrupt();
	}

	public static void main(String[] args) {
		MThreadGroup_1_12 threadGroup = new MThreadGroup_1_12("MyThreadGroup");
		ThreadGroupTask  task = new ThreadGroupTask();
		for (int i = 0 ; i < 2; i++){
			//建立2个线程，运行的任务为task,且他们属于同一个线程组（MyThreadGroup)，这里用于测试线程组异常处理器是否会处理异常
			Thread t = new Thread(threadGroup,task);
			t.start();
		}
	}
	
}
/**
 * 一个将属于MyThreadGroup组的测试任务
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
