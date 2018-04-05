package org.andrewliu.java7thread.java7base;

/**
 * 测试线程的两种实现方法，实现Runnable接口与继承Thread类
 * @author de
 *
 */
public class Calculator_1_2  implements Runnable{  //实现接口，实现run方法

	private int number;
	public Calculator_1_2(int number){
		this.number = number;
	}
	
	@Override
	public void run() {
		for ( int i=1;i<=10;i++){
			System.out.printf("runnable---- %s:%d * %d= %d\n",Thread.currentThread().getName(),number,i,i*number);
		}
	}
	
	public static void main(String[] args) {
		//线程信息的获取
		Thread[] threads = new Thread[10];
		//线程的状态信息
		Thread.State[] status = new Thread.State[10];
		for(int i = 1;i <=10; i++){
			if(i%2==0){
				Calculator_1_2 calculator = new Calculator_1_2(i);  //将Runnable的实现包装到Thread中（此时才是创建了一个线程），再用Thread的start方法才能启动, 
				Thread thread = new Thread(calculator);
				thread.start();
			}else{
				MyThread thread = new MyThread(i);  //直接调用Thread的start()方法就可以启动
				thread.start();
			}
		}
	}

}


class MyThread extends Thread{  //继承Thread，重写run方法
	private int number;
	public MyThread(int number){
		this.number = number;
	}
	
	public void run(){
		for ( int i=1;i<=10;i++){
			System.out.printf("thread--- %s:%d * %d= %d\n",Thread.currentThread().getName(),number,i,i*number);
		}
	}
}