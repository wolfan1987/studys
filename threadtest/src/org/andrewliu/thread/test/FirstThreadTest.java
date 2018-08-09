package org.andrewliu.thread.test;

import java.util.concurrent.TimeUnit;

/**
 * ��Runnable �ӿڶ���һ������
 * @author de
 *
 */
public class FirstThreadTest  implements Runnable{

	protected int countDown = 10;
	private  static int taskCount = 0;
	private final  int  id = taskCount++;
	public FirstThreadTest(){
	}
	
	public  FirstThreadTest(int countDown){
		this.countDown = countDown;
	}
	public  String status(){
		return "#"+id+"("+(countDown > 0 ? countDown : "FirstThreadTest")+"),";
	}
	@Override
	public void run() {
		
		while(countDown-- > 0){
			System.out.println(status());
			//��ִ����һ��������̵߳��������㿴��Ҫ���ҽ��е��Ȳ��������ҿ����ó�ʱ���ñ���ִ��һ���,�������������ҾͲ���
			//����̵߳����ȼ���ͬ��ͬ���Ὣ����һ���߳�����
			//Thread.yield();
			try {
				//��ֻ˯1000����,����jdb1.5֮ǰ���÷�
				//Thread.sleep(1000);
				//jdb1.5/6�б�ʾ�߳����ߵķ�ʽ
				TimeUnit.MILLISECONDS.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	public static  void main(String[] args){
		//ֱ�ӵ���run������ִ������
		FirstThreadTest  test = new FirstThreadTest();
		test.run();
		//��Thread���װ����(��Ҫһ��ʵ����Runnable�ӿڵĶ���)������Thread��������
		Thread  thread = new Thread(new FirstThreadTest());
		System.out.println("��Thread.start ����run ��ʼ������������������");
		thread.start();//start ���������run����
		System.out.println(" Waiting  for LiftOff!");
		
		//����߳�֮�佻��ִ�в鿴,��Ϊ��������Thread.yield()�������ڵ���ʱ�����߳�֮����ò����Ӷ���ӡ�������ִ������
		for ( int i = 0 ; i < 5; i++){
			System.out.println("��forѭ���е���Thread.start ����run ��ʼ������������������i===="+i);
			new Thread(new FirstThreadTest()).start();
		}
		
		
	}
	
	
}
