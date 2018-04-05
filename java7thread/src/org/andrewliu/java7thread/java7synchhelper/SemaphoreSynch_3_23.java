package org.andrewliu.java7thread.java7synchhelper;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * jdk�Դ�ͬ�������ࣺ Semaphore(�ź���)
 * ����������ʵ�ֶ�������Ϣ�����������ź�����һ�ֱȽ�������ź���������������Ψһ�Ĺ�����Դ�ķ��ʡ�
 * ��������ڲ�������ֻ��0��1����ֵ��
 * Semaphore��������������:
 * acquireUninterruptibly():������acquire()���������ź������ڲ����������0��ʱ���ź����������߳�ֱ���䱻�ͷţ��߳�
 * �ڱ����������ʱ���У����ܻᱻ���жϣ��Ӷ�����acquire()�����׳�InterruptedException �쳣����acquireUninteruptibly()
 * ����������߳��жϲ��Ҳ����׳��κ��쳣��
 * tryAcquire()����ͼ�����Ϣ��������ܻ�þͷ���ture�����ܾͷ���false,�Ӷ��ܿ��̵߳������͵ȴ��ź������ͷš��ɸ��ݴ�״̬������ش���
 * @author de
 *
 */
public class SemaphoreSynch_3_23 {

	public static void main(String[] args) {
//		/**������ӡ��**/
//		PrintQueue printQueue = new PrintQueue();//ֻ��һ��PrintQueue������߳�ͬ��ʱ������Ϣ�����ƣ�ֻ����һ���߳�ʹ��
//		Thread thread[] = new Thread[10];
//		for ( int i = 0; i < 10; i++){
//			thread[i] = new Thread(new Job(printQueue),"Thread"+i);
//		}
//		
//		for ( int i = 0; i < 10; i++){
//			thread[i].start();
//		}
		
		/**�����ӡ��**/
		PrintQueues printQueues = new PrintQueues();//��3��PrintQueue������߳�ͬ��ʱ������3��ʱҪ�Ŷӵȴ�
		Thread threads[] = new Thread[10];
		for ( int i = 0; i < 10; i++){
			threads[i] = new Thread(new Jobs(printQueues),"Threads"+i);
		}
		
		for ( int i = 0; i < 10; i++){
			threads[i].start();
		}
		
	}
}

class PrintQueue{
	private  final Semaphore  semaphore;
	
	public PrintQueue(){   
		//���ܶ��ٸ��̣߳�ͬһʱ��ֻ����һ���߳�ʹ��PrintQueue
		semaphore = new Semaphore(1);  
		//semaphore = new Semaphore(1,boolean fair); fair=trueʱΪ��ƽģʽ��fair=falseʱΪ����ƽģʽ��Ĭ�ϣ�
	}
	
	public void printJob (Object document){
		try{
			semaphore.acquire(); //���һ���źţ����ͷ�ǰ�������̲߳��ܻ����Ϣ��
			long duration = (long)(Math.random()*10);
			System.out.printf("%s: PrintQueue: Printing a Job during %d seconds\n",Thread.currentThread().getName(),duration);
			Thread.sleep(duration);
		}catch(InterruptedException e){
			e.printStackTrace();
		}finally{
			semaphore.release();//�ͷ���Ϣ���������߳̿����ٽ���
		}
	}
}

/**
 * �����ӡ�������ǿ����Ŷӵȴ���ӡ
 * @author de
 *
 */
class PrintQueues{
     private  final Semaphore  semaphore;
     //������еĴ�ӡ��
	 private  boolean freePrinters[];
	 //������freePrinters�ķ���
	 private Lock lockPrinters;
	public PrintQueues(){   
		//���ܶ��ٸ��̣߳�ͬһʱ��ֻ����һ���߳�ʹ��PrintQueue
		semaphore = new Semaphore(3);  
		freePrinters = new boolean[3];
		for(int i = 0; i < 3; i++){
			freePrinters[i] = true;
		}
		lockPrinters = new ReentrantLock();
	}
	
	public void printJob (Object document){
		try{
			semaphore.acquire(); //���һ���źţ����ͷ�ǰ�������̲߳��ܻ����Ϣ��
			//ͨ��˽�к���getPrinter()��÷����ӡ�����Ĵ�ӡ�����
			int assignedPrinter = getPrinter();
			long duration = (long)(Math.random()*10);
			System.out.printf("%s: PrintQueue: Printing a Job during %d seconds\n",Thread.currentThread().getName(),assignedPrinter,duration);
			//Thread.sleep(duration);
			TimeUnit.SECONDS.sleep(duration);
			//��ĳһ��ӡ����Ϊ���У�ͬʱ�ͷ���Ϣ��
			freePrinters[assignedPrinter] = true;
		}catch(InterruptedException e){
			e.printStackTrace();
		}finally{
			semaphore.release();//�ͷ���Ϣ���������߳̿����ٽ���
		}
	}
	/**
	 * �õ����д�ӡ����� 
	 * @return
	 */
	private int getPrinter(){
		int ret = -1;
		try{
			//��������ס��freePrinters�ķ��ʣ���ȷ�����ᱻ�����߳����ߴ�ӡ��
			lockPrinters.lock();
			for( int i = 0; i < freePrinters.length; i++){
				if(freePrinters[i]){//����ҵ����е�
					ret=i;  //���浱ǰ��ӡ��������
					freePrinters[i] = false; //���ô�ӡ����Ϊ������
					break;
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			//�ͷ���
			lockPrinters.unlock();
		}
		
		return ret;
	}
	
}




class Job implements Runnable{
	private PrintQueue printQueue;
	public Job(PrintQueue printQueue){
		this.printQueue = printQueue;
	}
	@Override
	public void run() {
		System.out.printf("%s: Going to print a job \n", Thread.currentThread().getName());
		printQueue.printJob(new Object());
		System.out.printf("%s: The document has been printed\n",Thread.currentThread().getName());
	}
	
}

class Jobs implements Runnable{
	private PrintQueues printQueues;
	public Jobs(PrintQueues printQueues){
		this.printQueues = printQueues;
	}
	@Override
	public void run() {
		System.out.printf("%s: Going to print a job \n", Thread.currentThread().getName());
		printQueues.printJob(new Object());
		System.out.printf("%s: The document has been printed\n",Thread.currentThread().getName());
	}
	
}
