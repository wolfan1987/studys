package org.andrewliu.java7thread.java7synchhelper;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * ��ʾ: CountDownLatchͬ��������
 * ����һ��ͬ�������࣬�����һ�����������߳���ִ�еĲ���֮ǰ���������߳�һֱ�ȴ��������ʹ��һ���������г�ʼ���������������Ҫ
 * �ȴ���ɵĲ�������Ŀ����һ���߳�Ҫ�ȴ�ĳЩ������ִ����ʱ����Ҫ����await()����������������߳̽�������ֱ���ȴ������в�����
 * ��ɡ���ĳһ��������ɺ���������countDown()������CuntDownLatch����ڲ���������1,�����������0��ʱ��CountDownLatch��
 * ���������е���await()�������������ߵ��߳�.��������������������Դ�����ٽ����ģ���������ͬ��ִ�ж�������һ�����߶���̡߳�
 * CountDownLatchֻ׼�����һ�Σ������ڲ�������Ϊ0ʱ���ٵ����䷽�������������ã�Ҫ����ͬ�����µ�CountDownLatch����.
 * @author de
 *
 */
public class CountDownLatchConcurrent_3_4 {

	public static void main(String[] args) {
		//������
		Videoconference conference = new Videoconference(10);
		Thread threadConference = new Thread(conference);
		//�������߳�����
		threadConference.start();
		
		for(int i = 0; i < 10; i++){
			//������Ա��ʼ����
			Participant p = new Participant(conference,"Participant"+i);
			Thread t = new Thread(p);
			t.start();
		}
	}
}

class Videoconference implements Runnable{
	private final CountDownLatch controller;
	
	public Videoconference(int number){
		//numberָ��������������
		controller = new CountDownLatch(number);
	}
	
	public void arrive(String name){
		System.out.printf("%s has arrived.",name);
		controller.countDown();
		System.out.printf("VideoConference: Waiting for %d participants.\n", controller.getCount());  //��ӡ�ܹ��ж��ٸ�������������
	}
	
	@Override
	public void run() {
		System.out.printf("VideoConference: Initialization: %d participants.\n",controller.getCount());
		try{
			controller.await();
			System.out.printf("VideoConference: All the  participants have come\n");
			System.out.printf("VideoConference: Let's start....\n");
		}catch(InterruptedException e){
			e.printStackTrace();
		}
	}
	
}

class Participant implements Runnable{
	private Videoconference conference;
	private String name;
	public Participant(Videoconference conference,String name){
		this.conference = conference;
		this.name = name;
	}
	
	@Override
	public void run() {
		long duration = (long)(Math.random()*10);
		try{
			TimeUnit.SECONDS.sleep(duration);
		}catch(InterruptedException e){
			e.printStackTrace();
		}
		
		conference.arrive(name);
	}
}



