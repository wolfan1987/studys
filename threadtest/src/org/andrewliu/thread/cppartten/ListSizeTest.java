package org.andrewliu.thread.cppartten;

/**
 * ����������������������
 * @author de
 *
 */
public class ListSizeTest {
	
	public static void main(String[] args) {
		Object lock = new Object();
		ListSizeTest1 t1 = new ListSizeTest1(lock);
		t1.start();
		try {
			Thread.sleep(50);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ListSizeTest2 t2 = new  ListSizeTest2(lock);
		t2.start();
	}

}


class  ListSizeTest1 extends Thread{
	private  Object lock;
	public  ListSizeTest1(Object lock){
		super();
		this.lock = lock;
	}
	
	@Override
	public void run(){
		try {
			
		synchronized(lock){
			if(DataList.size() != 5){
				System.out.println("��ʼ  wait time="+System.currentTimeMillis());
				lock.wait();
				System.out.println("����  wait time="+System.currentTimeMillis());
			}
		}
		} catch (InterruptedException e) {
		
			e.printStackTrace();
		}
	}
}

class  ListSizeTest2  extends  Thread{
	private  Object lock;
	public  ListSizeTest2(Object lock){
		super();
		this.lock = lock;
	}
	
	@Override
	public void run(){
		try{
			synchronized(lock){
				for(int i=0 ; i < 10 ; i++){
					DataList.addData();
					if(DataList.size() == 5){
						lock.notify();
						System.out.println("����֪ͨ����ʼ�����ˡ���������");
					}
					System.out.println(" ����� "+(i+1)+"��Ԫ��");
					Thread.sleep(1000);
				}
			}
		}catch(InterruptedException  e){
			e.printStackTrace();
		}
		
	}
}