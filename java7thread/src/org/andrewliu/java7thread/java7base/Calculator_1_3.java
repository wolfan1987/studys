package org.andrewliu.java7thread.java7base;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.Thread.State;

/**
 * 测试线程的两种实现方法，实现Runnable接口与继承Thread类
 * @author de
 *
 */
public class Calculator_1_3  implements Runnable{  //实现接口，实现run方法

	private int number;
	public Calculator_1_3(int number){
		this.number = number;
	}
	
	@Override
	public void run() {
		for ( int i=1;i<=10;i++){
			System.out.printf("runnable---- %s:%d * %d= %d\n",Thread.currentThread().getName(),number,i,i*number);
		}
	}
	
	public static void main(String[] args) {
		//存放10个线程的数组
		Thread threads[] = new Thread[10];
		//线程的状态信息
		Thread.State status[] = new Thread.State[10]; 
		for(int i = 0;i < 10; i++){
			if((i%2)==0){
				Calculator_1_3 calculator = new Calculator_1_3(i);  //将Runnable的实现包装到Thread中（此时才是创建了一个线程），再用Thread的start方法才能启动, 
				threads[i] = new Thread(calculator);
				threads[i].setPriority(Thread.MAX_PRIORITY);//设置障线程的优先级为最高级
			}else{
				threads[i] = new MyThread2(i);  //直接调用Thread的start()方法就可以启动
				threads[i].setPriority(Thread.MIN_PRIORITY);//设置障线程的优先级为最低级
			}
			
			threads[i].setName("Thread"+(i+i));//设置线程的名字
		}
		
		
			FileWriter file;
			PrintWriter pw = null;
			try {
				//将线程信息写入文件中
				file = new FileWriter("F:\\WEB-INF\\logs\\log.txt");
				 pw = new PrintWriter(file);
				for( int i = 0; i < 10;i++){
					pw.println("Main.Status of Thread"+i+":"+threads[i].getState());
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			for(int i = 0;i < 10; i++){
				threads[i].start();  //开始执行这10个线程
			}
			
			boolean finish = false;
			while(!finish){
				for(int i = 0; i < 10; i++){
					if(threads[i].getState()!=status[i]){
						writeThreadInfo(pw,threads[i],status[i]);
						status[i] = threads[i].getState();
					}
				}
			}
			
//			finish = true;
//			for ( int i = 0 ; i < 10; i++){
//				
//			}
			
	}
	
	private static void writeThreadInfo(PrintWriter pw,Thread thread,State state){
		pw.printf("Main : Id %d - %s\n", thread.getId(),thread.getName());
		pw.printf("Main : priority : %d\n", thread.getPriority());
		pw.printf("Main : Old state: %s\n", state);
		pw.printf("Main : new State : %s\n", thread.getState());
		pw.println("Main : **************************");
	}

}


class MyThread2 extends Thread{  //继承Thread，重写run方法
	private int number;
	public MyThread2(int number){
		this.number = number;
	}
	
	public void run(){
		for ( int i=1;i<=10;i++){
			System.out.printf("thread--- %s:%d * %d= %d\n",Thread.currentThread().getName(),number,i,i*number);
		}
	}
}