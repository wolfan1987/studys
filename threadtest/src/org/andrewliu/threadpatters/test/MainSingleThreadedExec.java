package org.andrewliu.threadpatters.test;

import java.util.Date;

public class MainSingleThreadedExec extends Thread{

	public static void main(String[] args) {
		new MainSingleThreadedExec().start();
		new MainSingleThreadedExec().start();
	}
	
	 public void run() {
	        System.out.println(Thread.currentThread().getName() + ":" + MySystem2.getInstance().getDate());
	 }
	
}


class MySystem2 {
	private static MySystem2 instance = null;
	private Date date = new Date();

	private MySystem2() {
	}

	public Date getDate() {
		return date;
	}

	public   static  synchronized MySystem2 getInstance() {

		if (instance == null) {
            instance = new MySystem2();
        }
        return instance;
	}
}
