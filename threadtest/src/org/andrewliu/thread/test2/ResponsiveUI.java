package org.andrewliu.thread.test2;

import java.io.IOException;

public class ResponsiveUI  extends Thread{
	private static volatile double d = 1;
	public  ResponsiveUI(){
		setDaemon(true);
		start();
	}
	/**
	 * һֱ�����У�d����б仯��ִ�м���
	 */
	public void run(){
		while(true){
			d = d + (Math.PI + Math.E)/d;
		}
	}
	
	public static void main(String[] args) throws IOException {
		new ResponsiveUI();
	    System.in.read();
	    //�������run�ڼ��㣬Ȼ��������
	    System.out.println(d);
	}

}
