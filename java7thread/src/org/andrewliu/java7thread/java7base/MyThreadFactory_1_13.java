package org.andrewliu.java7thread.java7base;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * ʵ���Լ����̹߳����࣬���������̶߳���
 * @author de
 *
 */
public class MyThreadFactory_1_13  implements ThreadFactory {

	/**
	 * ���幤������
	 */
	
	private int counter;//��¼�߳�����
	private String name; //�߳�����
	private List<String> stats; //�߳�״̬
	//...............�����Զ�������,�磺�߳�����������
	
	public MyThreadFactory_1_13(String name){
		counter = 0;
		this.name = name;
		stats = new ArrayList<String>();
	}
	@Override
	public Thread newThread(Runnable r) {
		Thread t  = new Thread(r,name+"-Thread_"+counter);
		counter++;
		stats.add(String.format("Created thread %d with name %s on %s\n", t.getId(),t.getName(),new Date()));
		return t;
	}
	
	public String getStats(){
		StringBuffer buffer = new StringBuffer();
		Iterator<String> it = stats.iterator();
		while(it.hasNext()){
			buffer.append(it.next());
			buffer.append("\n");
		}
		return buffer.toString();
	}
	
	
	public static void main(String[] args) {
		MyThreadFactory_1_13  factory = new MyThreadFactory_1_13("MyThreadFactory");
		TestThreadFactorTask task = new TestThreadFactorTask();
		Thread thread ;
	    System.out.printf("Starting the Threads\n");
	    for(int i = 0; i < 10; i++){
	    	thread = factory.newThread(task);//�����̲߳����߳����������
	    	thread.start();
	    }
	    
	    System.out.printf("Factory status:\n");
	    System.out.printf("%s\n", factory.getStats());
	}

}

class TestThreadFactorTask implements Runnable{

	@Override
	public void run() {
		try {
			TimeUnit.SECONDS.sleep(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
}
