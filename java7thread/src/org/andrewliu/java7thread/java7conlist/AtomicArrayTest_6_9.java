package org.andrewliu.java7thread.java7conlist;

import java.util.concurrent.atomic.AtomicIntegerArray;


/**
 * 针对要同步、防死锁、数据共享访问问题，为了提供更优的性能：Java 于是引于了比较和交换操作(Compare-and-Swap Operation)，这个操作使用
 * 以下三步修改变量的值。
 * 1、取得变量值，即变量的值；
 * 2、在一个临时变量中修改变量值，即变量的新值。
 * 3、如果上面获得的变量旧值与当前变量值相等，就用新值替换旧值。如果已有其他线程修改了这个值，上面获得的变量的旧值就可能不当前变量值不同。
 * 采用比较和交换机制不需要使用同步机制，可以避免死锁并性能更好。
 * 在java原子变量(Atomic Variable)中实现了这处机制，这些变量提供了实现比较和交换操作的compareAndSet()方法。
 *  
 * 原子数组（Atomic Array)：java提供了对原子数组对类型为Integer 或 long数字数组的原子操作。
 * AtomicIntegerArray类与AtomicLongArray类的方法相同：
 * AtomicIntegerArray.getAndIncrement(i); //将第i个元素值加1
 * AtomicLongArray.getAndDecrement(i);//将第i个元素值减1 
 *其他方法：
 * get(int i); 返回数组中同参数指定位置的值。
 *set(int I,int newValue):设置由参数指定位置的新值。
 * @author de
 * 
 */
public class AtomicArrayTest_6_9 {

	public static void main(String[] args) {
		final int THREADS = 100;
		AtomicIntegerArray vector = new AtomicIntegerArray(1000);
		Incrementer incrementer = new Incrementer(vector);
		Decrementer decrementer = new Decrementer(vector);
		
		Thread threadIncrementer[] = new Thread[THREADS];
		Thread threadDecrementer[] = new Thread[THREADS];
		
		for ( int i = 0 ; i < THREADS; i++){
			threadIncrementer[i] = new Thread(incrementer);
			threadDecrementer[i] = new Thread(decrementer);
			threadIncrementer[i].start();
			threadDecrementer[i].start();
		}
		
		for(int i = 0; i<100;i++){
			try{
				threadIncrementer[i].join();
				threadDecrementer[i].join();
			}catch(InterruptedException e){
				e.printStackTrace();
			}
		}
		
		for ( int i = 0; i < vector.length(); i++){
			if(vector.get(i)!=0){  //用get(i)，访问元素i的值
				System.out.printf("Vector["+i+"] :"+vector.get(i));
			}
		
		}
		
		System.out.println("Main: end of the example!");
	}
	
}


class Incrementer implements Runnable{
	private AtomicIntegerArray vector;

	public Incrementer(AtomicIntegerArray vector) {
		super();
		this.vector = vector;
	}

	@Override
	public void run() {
		for ( int i = 0; i < vector.length();i++){
			vector.getAndIncrement(i);//将第i个元素加1
		}
	}
}

class Decrementer implements Runnable{
	private AtomicIntegerArray vector;

	public Decrementer(AtomicIntegerArray vector) {
		super();
		this.vector = vector;
	}

	@Override
	public void run() {
		for ( int i = 0; i < vector.length(); i++){
			vector.getAndDecrement(i); //将第i个元素头1
		}
	}
}