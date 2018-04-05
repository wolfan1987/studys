package org.andrewliu.java7thread.java7conlist;

import java.util.concurrent.ThreadLocalRandom;

/**
 * 生成并发随机数: ThreadLocalRandom类： 用于生成伪随机数，它是线程本地变量，每个生成随机改数的线程
 * 都有一个不同的生成器，但是都是在同一个类被管理，对程序员来说是透明的。相比于使用共享的Random对象为所有线程生成随机数，它种机制具有更好的性能。
 * 它可以生成其它类型的值：如：long,float,double,Boolean.
 * @author de
 *
 */
public class ThreadLocalRandomTest_6_7 {

	public static void main(String[] args) {
		Thread threads[] = new Thread[3];
		for(int i = 0; i < 3; i++){
			TaskLocalRandom task = new TaskLocalRandom();
			threads[i] = new Thread(task);
			threads[i].start();
		}
	}
}

class TaskLocalRandom implements Runnable{
	public TaskLocalRandom(){
		//使用current()方法为当前线程初始化随机数生成器,可以返回与当前线程关联的ThreadLocalRandom对象
		ThreadLocalRandom.current();
		
	}

	@Override
	public void run() {
		String name = Thread.currentThread().getName();
		for ( int i = 0; i < 10; i++){
			System.out.printf("%s: %d\n", name,ThreadLocalRandom.current().nextInt(10));//生成随机数
		}
	}
	
	
}
