package org.andrewliu.java7thread.java7base;


/**
 * �߳��жϲ��ԣ�
 * �߳������̡߳���ͨ�̡߳����ػ��̡߳��ػ��߳�
 * ���������з��ػ��̶߳�ִ����ᣬ�������system.exit();����ʱ������Ż������
 * java�̵߳��жϣ��߳���������ж������Ҽ���ִ�С�
 * Thread.interrupt()----�ж��̣߳����Ƿ��ж�״̬��Ϊtrue
 * Thread.isInterrupted()----�õ���ǰ�̵߳�״̬,����true/false
 * Thread.interrupted()----��̬���������߳��ж�״̬���ظ����ÿɽ��߳�״̬��Ϊtrue��false
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
