package org.andrewliu.java7thread.java7conlist;

import java.util.concurrent.ThreadLocalRandom;

/**
 * ���ɲ��������: ThreadLocalRandom�ࣺ ��������α������������̱߳��ر�����ÿ����������������߳�
 * ����һ����ͬ�������������Ƕ�����ͬһ���౻�����Գ���Ա��˵��͸���ġ������ʹ�ù����Random����Ϊ�����߳���������������ֻ��ƾ��и��õ����ܡ�
 * �����������������͵�ֵ���磺long,float,double,Boolean.
 * @author de
 *
 */
public class ThreadLocalRandomTest_6_7 {

	public static void main(String[] args) {
		Thread threads[] = new Thread[3];
		for(int i = 0; i < 3; i++){
			TaskLocalRandom task = new TaskLocalRandom();
			threads[i] = new Thread(task);
			threads[i].start();
		}
	}
}

class TaskLocalRandom implements Runnable{
	public TaskLocalRandom(){
		//ʹ��current()����Ϊ��ǰ�̳߳�ʼ�������������,���Է����뵱ǰ�̹߳�����ThreadLocalRandom����
		ThreadLocalRandom.current();
		
	}

	@Override
	public void run() {
		String name = Thread.currentThread().getName();
		for ( int i = 0; i < 10; i++){
			System.out.printf("%s: %d\n", name,ThreadLocalRandom.current().nextInt(10));//���������
		}
	}
	
	
}
