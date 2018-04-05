package org.andrewliu.java7thread.java7base;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.Thread.State;

/**
 * �����̵߳�����ʵ�ַ�����ʵ��Runnable�ӿ���̳�Thread��
 * @author de
 *
 */
public class Calculator_1_3  implements Runnable{  //ʵ�ֽӿڣ�ʵ��run����

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
		//���10���̵߳�����
		Thread threads[] = new Thread[10];
		//�̵߳�״̬��Ϣ
		Thread.State status[] = new Thread.State[10]; 
		for(int i = 0;i < 10; i++){
			if((i%2)==0){
				Calculator_1_3 calculator = new Calculator_1_3(i);  //��Runnable��ʵ�ְ�װ��Thread�У���ʱ���Ǵ�����һ���̣߳�������Thread��start������������, 
				threads[i] = new Thread(calculator);
				threads[i].setPriority(Thread.MAX_PRIORITY);//�������̵߳����ȼ�Ϊ��߼�
			}else{
				threads[i] = new MyThread2(i);  //ֱ�ӵ���Thread��start()�����Ϳ�������
				threads[i].setPriority(Thread.MIN_PRIORITY);//�������̵߳����ȼ�Ϊ��ͼ�
			}
			
			threads[i].setName("Thread"+(i+i));//�����̵߳�����
		}
		
		
			FileWriter file;
			PrintWriter pw = null;
			try {
				//���߳���Ϣд���ļ���
				file = new FileWriter("F:\\WEB-INF\\logs\\log.txt");
				 pw = new PrintWriter(file);
				for( int i = 0; i < 10;i++){
					pw.println("Main.Status of Thread"+i+":"+threads[i].getState());
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			for(int i = 0;i < 10; i++){
				threads[i].start();  //��ʼִ����10���߳�
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


class MyThread2 extends Thread{  //�̳�Thread����дrun����
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