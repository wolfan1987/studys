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
					System.out.println("������:"+Thread.currentThread().getName()+"Waiting ��  AAA ");
					lock.wait();
				}
				System.out.println("������:"+Thread.currentThread().getName()+"Ruannable �� ");
				String value = System.currentTimeMillis() + "_" + System.nanoTime();
				System.out.println("set ��ֵ��"+value);
				ValueObject.value = value;
				lock.notifyAll();   //���ж��������ʱ����notify����ֻ����ĳ���̣߳�����ȫ�����ѣ������ڵȴ����ѵ��߳̽�����,Ҫ��notifyAll
			}
		}catch(InterruptedException ex){
			ex.printStackTrace();
		}
	}

}
