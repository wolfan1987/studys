package org.andrewliu.threadpatters.test;

import java.util.Date;

public class MainCorrect  extends Thread{

	public static void main(String[] args) {
		new MainCorrect().start();
		new MainCorrect().start();
	}
	
	 public void run() {
	        System.out.println(Thread.currentThread().getName() + ":" + MySystem1.getInstance().getDate());
	 }
	
}

class MySystem1 {
	private static MySystem1 instance = new MySystem1();
	private Date date = new Date();

	private MySystem1() {
	}

	public Date getDate() {
		return date;
	}

	public static MySystem1 getInstance() {

		return instance;
	}
}