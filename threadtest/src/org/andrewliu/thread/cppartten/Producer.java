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
				System.out.println("set ��ֵ��"+value);
				ValueObject.value = value;
				lock.notify();   //�����ͬһ��������lock����notify���У���notifyAll�ỽ�����������߳�
			}
		}catch(InterruptedException ex){
			ex.printStackTrace();
		}
	}

}
