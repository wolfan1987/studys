package org.andrewliu.thread.cppartten;

public class MConsumer {
	private  String lock;
	public MConsumer(String lock){
		super();
		this.lock = lock;
	}
	
	public void  getValue(){
		try{
			synchronized(lock){
				while(ValueObject.value.equals("")){
					System.out.println("消费者:"+Thread.currentThread().getName()+"Waiting 了  BBB ");
					lock.wait();
				}
				System.out.println("消费者:"+Thread.currentThread().getName()+"Ruannable 了 ");
				ValueObject.value = "";
				lock.notifyAll();   //当有多个消费者时，用notify，会只唤醒某个线程，不会全部唤醒，其它在等待消费的线程将假死,要用notifyAll
			}
		}catch(InterruptedException ex){
			ex.printStackTrace();
		}
	}
}
