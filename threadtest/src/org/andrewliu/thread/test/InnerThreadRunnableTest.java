package org.andrewliu.thread.test;

import java.util.concurrent.TimeUnit;

/**
 * 通过使用内部类来将线程代码隐藏在类中将会很有用，以下为四种实现
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
			//在方法内创建线程
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
	//以内部类+继承Thread实现,在构造器中启动
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
	 * 在构造函数中初始化内部类
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
		//构造器+内部类+直接实例化Thread重写run方法
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
	//肉部类+实现Runnable接口
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
		//将匿名内部类任务包装到Thread中实现
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

