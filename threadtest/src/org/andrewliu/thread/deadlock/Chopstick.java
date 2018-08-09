package org.andrewliu.thread.deadlock;

/**
 * ��ѧ�ҳԷ��õĿ���
 * @author de
 *
 */
public class Chopstick {

	private boolean taken = false;
	/**
	 * ������ӣ����������ӵ�taken=true����ʾ��������
	 * @throws InterruptedException
	 */
	public synchronized void take() throws InterruptedException{
		while(taken){
			wait();
		}
		taken = true;
	}
	
	/**
	 * ���¿��ӣ����˿��ӵ�taken��Ϊfalse�������������߳̿����������
	 */
	public synchronized void drop(){
		taken = false;
		notifyAll();
	}
}
