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
				System.out.println("get ��ֵ��"+ValueObject.value);
				ValueObject.value = "";
				lock.notify();  //�����ͬһ��������lock����notify���У���notifyAll�ỽ�����������߳�
			}
		}catch(InterruptedException ex){
			ex.printStackTrace();
		}
	}

}
