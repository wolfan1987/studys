package org.andrewliu.thread.cppartten;

public class Consumer {
	
	private  String lock;
	public Consumer(String lock){
		super();
		this.lock = lock;
	}
	
	public void  getValue(){
		try{
			synchronized(lock){
				if(ValueObject.value.equals("")){
					lock.wait();
				}
				System.out.println("get 的值是"+ValueObject.value);
				ValueObject.value = "";
				lock.notify();  //如果是同一个锁对象lock，用notify就行，用notifyAll会唤醒其他所有线程
			}
		}catch(InterruptedException ex){
			ex.printStackTrace();
		}
	}

}
