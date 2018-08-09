package org.andrewliu.thread.test;

import java.util.concurrent.TimeUnit;

/**
 * ͨ��ʹ���ڲ��������̴߳������������н�������ã�����Ϊ����ʵ��
 * @author de
 *
 */
public class InnerThreadRunnableTest {

	public static void main(String[] args) {
		 new InnerThread1("InnerThread1");
		 new InnerThread2("InnerThread2");
		 new InnerRunnable1("InnerRunnable1");
		 new InnerRunnable2("InnerRunnable2");
		 new ThreadMethod("ThreadMethod").runTask();
	}
}

class ThreadMethod{
	private int countDown = 5;
	private Thread t;
	private String name;
	public ThreadMethod(String name){
		this.name = name;
	}
	public void runTask(){
		if(t == null){
			//�ڷ����ڴ����߳�
			t = new Thread(name){
				public void run(){
					try{
						while(true){
							System.out.println(this);
							if(--countDown == 0) return;
							sleep(10);
						}
					}catch(InterruptedException e){
						System.out.println(" interrupted  ....");
					}
				}
				
				public String toString(){
					return getName()+" : "+countDown;
				}
			};
			t.start();
		}
	}
}

class InnerThread1{
	private int countDown=5;
	private Inner inner;
	//���ڲ���+�̳�Threadʵ��,�ڹ�����������
	private class Inner extends Thread{
		Inner(String name){
			super(name);
			start();
		}
		public void run(){
			try{
				while(true){
					System.out.println(this);
					if(--countDown == 0) return;
					sleep(10);
				}
			}catch(InterruptedException e){
				System.out.println(" interrupted  ....");
			}
		}
		
		public String toString(){
			return getName()+" : "+countDown;
		}
	}
	/**
	 * �ڹ��캯���г�ʼ���ڲ���
	 * @param name
	 */
	public InnerThread1(String name){
		inner = new Inner(name);
	}
}


class InnerThread2{
	private int countDown = 5;
	private Thread t;
	public InnerThread2(String name){
		//������+�ڲ���+ֱ��ʵ����Thread��дrun����
		t = new Thread(name){
			public void run(){
				try{
					while(true){
						System.out.println(this);
						if(--countDown == 0) return;
						sleep(10);
					}
				}catch(InterruptedException e){
					System.out.println(" interrupted  ....");
				}
			}
			
			public String toString(){
				return getName()+" : "+countDown;
			}
		};
		t.start();
	}
}

class InnerRunnable1{
	private int countDown = 5;
	private Inner  inner;
	//�ⲿ��+ʵ��Runnable�ӿ�
	private class Inner implements Runnable{
		Thread t ;
		Inner(String name){
			t = new Thread(this,name);
			t.start();
		}
		public void run(){
			try{
				while(true){
					System.out.println(this);
					if(--countDown == 0) return;
					TimeUnit.MILLISECONDS.sleep(10);
				}
			}catch(InterruptedException e){
				System.out.println(" interrupted  ....");
			}
		}
		
		public String toString(){
			return t.getName()+" : "+countDown;
		}
	}
	
	public InnerRunnable1(String name){
		inner = new Inner(name);
	}
}

class InnerRunnable2{
	private int countDown = 5;
	private Thread t ;
	public InnerRunnable2(String name){
		//�������ڲ��������װ��Thread��ʵ��
		t = new Thread(new Runnable(){
			public void run(){
				try{
					while(true){
						System.out.println(this);
						if(--countDown == 0) return;
						TimeUnit.MILLISECONDS.sleep(10);
					}
				}catch(InterruptedException e){
					System.out.println(" interrupted  ....");
				}
			}
			
			public String toString(){
				return Thread.currentThread().getName()+" : "+countDown;
			}
			
		},name);
		t.start();
	}
}

