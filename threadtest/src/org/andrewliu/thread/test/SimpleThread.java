package org.andrewliu.thread.test;

/**
 * ����̳�Thread����ʵ���̰߳�װ����
 * �ڹ������������̲߳���ȫ����Ϊ��һ��������ܻ��ڹ���������֮ǰ��ʼִ�У�����ζ�Ÿ������ܹ����ʴ��ڲ��ȶ�״̬�Ķ���,��������
 * ѡ��Executor���ǲ�����ʽ�Ĵ���Thread�������һ��ԭ��
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
