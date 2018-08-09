package org.andrewliu.threadpatters.test;

import java.util.Date;

public class MainDoubleCheckLock extends Thread{

	public static void main(String[] args) {
		new MainDoubleCheckLock().start();
		new MainDoubleCheckLock().start();
	}
	
	 public void run() {
	        System.out.println(Thread.currentThread().getName() + ":" + MySystem3.getInstance().getDate());
	 }
}


class MySystem3 {
	private static MySystem3 instance = null;
	private Date date = new Date();

	private MySystem3() {
	}

	public Date getDate() {
		return date;
	}

	public   static   MySystem3 getInstance() {

		if (instance == null) {
            synchronized (MySystem3.class) {
            	if(instance == null){
            		instance = new MySystem3();
            	}
			}
        }
        return instance;
	}
}