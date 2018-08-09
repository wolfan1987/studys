package org.andrewliu.thread.test2;

import java.io.IOException;

public class ResponsiveUI  extends Thread{
	private static volatile double d = 1;
	public  ResponsiveUI(){
		setDaemon(true);
		start();
	}
	/**
	 * 一直在运行，d如果有变化就执行计算
	 */
	public void run(){
		while(true){
			d = d + (Math.PI + Math.E)/d;
		}
	}
	
	public static void main(String[] args) throws IOException {
		new ResponsiveUI();
	    System.in.read();
	    //输入完后，run在计算，然后输出结果
	    System.out.println(d);
	}

}
