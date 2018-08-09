package org.andrewliu.thread.test;

/**
 * 以类继承Thread类来实现线程包装任务
 * 在构造器中启动线程不安全，因为另一个任务可能会在构造器结束之前开始执行，这意味着该任务能够访问处于不稳定状态的对象,建议优先
 * 选用Executor而非不是显式的创建Thread对象的另一个原因
 * 
 * 
 * @author de
 * 
 */
public class SimpleThread extends Thread {
	private int countDown = 5;
	private static int threadCount = 0;

	public SimpleThread() {
		super(Integer.toString(++threadCount));
		start();
	}
	public String toString(){
		return "#"+getName()+"("+countDown+")";
	}
	public  void run(){
		while(true){
			System.out.println(this);
			if(--countDown==0){
				return;
			}
		}
	}
	
	public static void main(String[] args) {
		for( int  i = 0; i < 5; i++){
			new SimpleThread();
		}
	}
}
