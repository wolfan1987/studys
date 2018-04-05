package org.andrewliu.java7thread.java7base;


/**
 * 线程中断测试：
 * 线程有主线程、普通线程、非守护线程、守护线程
 * 当程序所有非守护线程都执行完结，或调用了system.exit();方法时，程序才会结束。
 * java线程的中断，线程允许忽略中断请求并且继续执行。
 * Thread.interrupt()----中断线程，将是否中断状态设为true
 * Thread.isInterrupted()----得到当前线程的状态,返回true/false
 * Thread.interrupted()----静态方法设置线程中断状态。重复调用可将线程状态改为true或false
 * @author de
 *
 */
public class PrimeGenerator_1_4  extends Thread	{

	@Override
	public void run(){
		long number = 1L;
		while(true){
			if(isPrime(number)){
				System.err.printf("Number %d is Prime",number);
			}
			if(isInterrupted()){
				System.out.printf("The Prime Generator has been Interrupted");
				return;
			}
			number++;
		}
	}
	
	private boolean isPrime(long number){
		if(number <= 2){
			return true;
		}
		for ( long i = 2; i<number; i++){
			if((number%i)==0){
				return false;
			}
		}
		return true;
	}
	
	public static void main(String[] args) {
		Thread task = new PrimeGenerator_1_4();
		task.start();
		try {
			Thread.sleep(5000);
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		task.interrupt();
	}
}
