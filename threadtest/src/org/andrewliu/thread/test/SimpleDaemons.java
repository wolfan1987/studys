package org.andrewliu.thread.test;

import java.util.concurrent.TimeUnit;

/**
 * ��̨�̲߳��ԣ���̨�̲߳������߳�ֻ���Ǻ�̨�̣߳�
 * ���Ǻ�̨�̹߳رգ����̨�߳�Ҳ��رգ������Ǵ��棬��������
 * ���Ǻ�̨�߳̽���ʱ����̨�߳������ûִ���꣬���̨�̵߳�finally�����ǲ���ִ�е�
 * @author de
 *
 */
public class SimpleDaemons  implements Runnable{

	@Override
	public void run() {
		while(true){
			try {
				TimeUnit.MILLISECONDS.sleep(200);
				System.out.println(Thread.currentThread() + "  "+this);
			} catch (InterruptedException e) {
				System.out.println("sleep() interrupted....");
				e.printStackTrace();
			}
			
		}
	}
	
	public static void main(String[] args) throws InterruptedException {
		for(int i = 0; i < 10; i++){
			Thread daemon = new Thread(new SimpleDaemons());
			daemon.setDaemon(true);//���߳���Ϊ��̨�߳�
			daemon.start();
		}
		System.out.println("All daemons started...");
		TimeUnit.MILLISECONDS.sleep(300);//�������к�̨�̺߳�main�߳�����300��,�Ա��̨�߳�ִ����
		System.out.println("All thread  endding...");
	}

}
