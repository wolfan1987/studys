package org.andrewliu.thread.cppartten;

public class Producer {
	private  String lock;
	public Producer(String lock){
		super();
		this.lock = lock;
	}
	
	public void  setValue(){
		try{
			synchronized(lock){
				if(!ValueObject.value.equals("")){
					lock.wait();
				}
				String value = System.currentTimeMillis() + "_" + System.nanoTime();
				System.out.println("set 的值是"+value);
				ValueObject.value = value;
				lock.notify();   //如果是同一个锁对象lock，用notify就行，用notifyAll会唤醒其他所有线程
			}
		}catch(InterruptedException ex){
			ex.printStackTrace();
		}
	}

}
