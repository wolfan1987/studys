package org.andrewliu.thread.cppartten;

/**
 * 测试有条件的生产和消费
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
				System.out.println("开始  wait time="+System.currentTimeMillis());
				lock.wait();
				System.out.println("结束  wait time="+System.currentTimeMillis());
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
						System.out.println("发出通知，开始消费了。。。。。");
					}
					System.out.println(" 添加了 "+(i+1)+"个元素");
					Thread.sleep(1000);
				}
			}
		}catch(InterruptedException  e){
			e.printStackTrace();
		}
		
	}
}