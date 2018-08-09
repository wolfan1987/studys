package org.andrewliu.thread.test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 线程优先级测试
 * 虽然jdk可设置10个级别的优先级，但不建议更改优先级，要改也只是设置以下三个值
 * MAX_PRIORITY   NORM_PRIOITY  MIN_PRIORITY,高的会先执行
 * 线程的优先级，由线程调度器根据优先级别来调整
 * @author de
 *
 */
public class SimplePriorities  implements Runnable{

	private int countDown = 5;
	//volatile 关键字保证变量d 不被编译器优化
	private  volatile double  d;
	private int priority;
	public SimplePriorities(int priority){
		this.priority = priority;
	}
	public String toString(){
		return  Thread.currentThread() +"  :   "+countDown;
	}
	@Override
	public void run() {
		//更改当前线程的优先级
		Thread.currentThread().setPriority(priority);
		while(true){
			for(int i = 1; i < 100000; i++){
				//进行高代价的数学运算
				d+=(Math.PI+Math.E)/(double)i;
				if(i % 1000 == 0){
					Thread.yield();
					System.out.println(this);
					if(--countDown == 0) return;
				}
			}
		}
	}
	
	public static void main(String[] args) {
		ExecutorService exec = Executors.newCachedThreadPool();
		for ( int i = 0; i < 5; i++){
			exec.execute(new SimplePriorities(Thread.MIN_PRIORITY));//优先级最低
		}
		//优先级最高
		exec.execute(new SimplePriorities(Thread.MAX_PRIORITY));
		exec.shutdown();//所有线和执行完后关闭
	}

}
