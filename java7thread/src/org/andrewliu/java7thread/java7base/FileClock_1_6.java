package org.andrewliu.java7thread.java7base;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class FileClock_1_6  implements Runnable{

	
	@Override
	public void run() {
		for( int i = 0;i < 10; i++){
			System.out.printf("%s\n", new Date());
			try {
				//����һ����,�������������У���CPU�Ѳ�����ʱ������߳�ִ��,����������ж����̣߳����׳�InterruptedException
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {
				System.out.printf("Fhe FileClock has been interrupted");
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args)  {
		FileClock_1_6 clock = new FileClock_1_6();
		Thread thread = new Thread(clock);
		thread.start();
		try {
			Thread.sleep(3600*5);   //����ǰ���߳�����5����,��clockִ��5����
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		thread.interrupt();//�ж�clock
	}

}
