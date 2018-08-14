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
					System.out.println("������:"+Thread.currentThread().getName()+"Waiting ��  BBB ");
					lock.wait();
				}
				System.out.println("������:"+Thread.currentThread().getName()+"Ruannable �� ");
				ValueObject.value = "";
				lock.notifyAll();   //���ж��������ʱ����notify����ֻ����ĳ���̣߳�����ȫ�����ѣ������ڵȴ����ѵ��߳̽�����,Ҫ��notifyAll
			}
		}catch(InterruptedException ex){
			ex.printStackTrace();
		}
	}
}
