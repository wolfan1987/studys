package org.andrewliu.thread.test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * �߳����ȼ�����
 * ��Ȼjdk������10����������ȼ�����������������ȼ���Ҫ��Ҳֻ��������������ֵ
 * MAX_PRIORITY   NORM_PRIOITY  MIN_PRIORITY,�ߵĻ���ִ��
 * �̵߳����ȼ������̵߳������������ȼ���������
 * @author de
 *
 */
public class SimplePriorities  implements Runnable{

	private int countDown = 5;
	//volatile �ؼ��ֱ�֤����d �����������Ż�
	private  volatile double  d;
	private int priority;
	public SimplePriorities(int priority){
		this.priority = priority;
	}
	public String toString(){
		return  Thread.currentThread() +"  :   "+countDown;
	}
	@Override
	public void run() {
		//���ĵ�ǰ�̵߳����ȼ�
		Thread.currentThread().setPriority(priority);
		while(true){
			for(int i = 1; i < 100000; i++){
				//���иߴ��۵���ѧ����
				d+=(Math.PI+Math.E)/(double)i;
				if(i % 1000 == 0){
					Thread.yield();
					System.out.println(this);
					if(--countDown == 0) return;
				}
			}
		}
	}
	
	public static void main(String[] args) {
		ExecutorService exec = Executors.newCachedThreadPool();
		for ( int i = 0; i < 5; i++){
			exec.execute(new SimplePriorities(Thread.MIN_PRIORITY));//���ȼ����
		}
		//���ȼ����
		exec.execute(new SimplePriorities(Thread.MAX_PRIORITY));
		exec.shutdown();//�����ߺ�ִ�����ر�
	}

}
