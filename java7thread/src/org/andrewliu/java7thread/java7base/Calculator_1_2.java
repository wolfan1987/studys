package org.andrewliu.java7thread.java7base;

/**
 * �����̵߳�����ʵ�ַ�����ʵ��Runnable�ӿ���̳�Thread��
 * @author de
 *
 */
public class Calculator_1_2  implements Runnable{  //ʵ�ֽӿڣ�ʵ��run����

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
		//�߳���Ϣ�Ļ�ȡ
		Thread[] threads = new Thread[10];
		//�̵߳�״̬��Ϣ
		Thread.State[] status = new Thread.State[10];
		for(int i = 1;i <=10; i++){
			if(i%2==0){
				Calculator_1_2 calculator = new Calculator_1_2(i);  //��Runnable��ʵ�ְ�װ��Thread�У���ʱ���Ǵ�����һ���̣߳�������Thread��start������������, 
				Thread thread = new Thread(calculator);
				thread.start();
			}else{
				MyThread thread = new MyThread(i);  //ֱ�ӵ���Thread��start()�����Ϳ�������
				thread.start();
			}
		}
	}

}


class MyThread extends Thread{  //�̳�Thread����дrun����
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