package org.andrewliu.thread.deadlock;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 5����ѧ����5�����ӳԷ����ö��߳����ȴ���Э��
 * @author de
 *
 */
public class FixedDiningPhilosophers {

	public static void main(String[] args) throws InterruptedException {
		int ponder = 5;
		int size = 5;
		ExecutorService exec = Executors.newCachedThreadPool();
		Chopstick[] sticks = new Chopstick[size];
		for(int i = 0; i < size; i++){
			sticks[i] = new Chopstick();
		}
		for(int i = 0; i <size; i++)
		if(i<(size-1)){//��i!=4ʱ��ȫ���Һ���
			exec.execute(new Philosopher(sticks[i],sticks[i+1],i,ponder));
		}else{//�������һ����(5)ʱ���������
			//������ѭ�����ÿ������ȫ�����Һ���Ļ���������������һ����ѧ�ҵ��ÿ��ӷ�ʽ��������ң��Ͳ������������
			exec.execute(new Philosopher(sticks[0],sticks[i],i,ponder));
		}
		
		TimeUnit.SECONDS.sleep(5);
		exec.shutdownNow();
	}
}
