package org.andrewliu.java7thread.java7base;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * ��ʾThread.join()��ʹ��
 * join()��������ʾ����ǰ�߳�A�ݹҲ����У��ȴ������߳�ִ������ֹ����ǰ�߳���ִ��
 * �磺��ǰ�߳�ΪA,��������߳�B��C��������߳�A�е����߳�B.join()��C.join()���������߳�A�ݹң��߳�B\C����ִ�У�ִ������ֹ���߳�A�����ִ�С�
 * ��͵ȴ���ֹ��
 * @author de
 *
 */
public class DataSourcesLoader_1_7  implements Runnable{

	@Override
	public void run() {
		System.out.printf("Beginning data sources loading : %s\n", new Date());
		try {
			TimeUnit.SECONDS.sleep(4);//���߳���˯4��
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.printf("Data sources loading has finished: %s\n", new Date());
	}
	
	
	public static void main(String[] args) {
		DataSourcesLoader_1_7 dsLoader = new DataSourcesLoader_1_7();
		Thread thread1 = new Thread(dsLoader,"DataSourceThread");
		NetworkConnectionsLoader ncLoader = new NetworkConnectionsLoader();
		Thread thread2 = new Thread(ncLoader,"NetWorkConnectionsLoader");
		thread1.start();
		thread2.start();
		
		
		try {
			//�ȴ�thread1��thread2ִ����ϣ�main�Ż����ִ��
			thread1.join();//join(1000ms)/join(1000ms,naoTime);
			thread2.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		System.out.println("Main : configuration has been loaded: %s\n "+new Date());
	}
	
	

}

class NetworkConnectionsLoader implements Runnable{
	@Override
	public void run() {
		System.out.printf("Beginning netWork Connections loading : %s\n", new Date());
		try {
			TimeUnit.SECONDS.sleep(6);//���߳���˯6��
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.printf("netWork Connections loading has finished: %s\n", new Date());
	}
}
