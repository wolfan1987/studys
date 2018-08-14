package org.andrewliu.thread.cppartten;

public class MProducer {
	private  String lock;
	public MProducer(String lock){
		super();
		this.lock = lock;
	}
	
	public void  setValue(){
		try{
			synchronized(lock){
				while(!ValueObject.value.equals("")){
					System.out.println("生产者:"+Thread.currentThread().getName()+"Waiting 了  AAA ");
					lock.wait();
				}
				System.out.println("生产者:"+Thread.currentThread().getName()+"Ruannable 了 ");
				String value = System.currentTimeMillis() + "_" + System.nanoTime();
				System.out.println("set 的值是"+value);
				ValueObject.value = value;
				lock.notifyAll();   //当有多个生产者时，用notify，会只唤醒某个线程，不会全部唤醒，其它在等待消费的线程将假死,要用notifyAll
			}
		}catch(InterruptedException ex){
			ex.printStackTrace();
		}
	}

}
