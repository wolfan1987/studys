package org.andrewliu.thread.cppartten;

import java.util.ArrayList;
import java.util.List;

/**
 * �����������߳�ʹ�õ�Stack
 * @author de
 *
 */
public class MyStack {

	private List<String>  list = new ArrayList<String>();
	
	public synchronized void push(){
		try{
			while(list.size() == 1){  //��if��ʽ��ֻ�ʺ�һ������һ������ʱʹ�ã���while������ʺ�N�����ߺ�N������
				this.wait();
			}
			list.add("anyString="+Math.random());
			this.notify();
			System.out.println("push="+list.size());
		}catch(InterruptedException ex){
			ex.printStackTrace();
		}
	}
	
	public  synchronized String pop(){
		String returnValue = "";
		try{
			while(list.size() == 0){   //��if��ʽ��ֻ�ʺ�һ������һ������ʱʹ�ã���while������ʺ�N�����ߺ�N������
				System.out.println("pop �����е�:"+Thread.currentThread().getName()+"�߳��� wait ״̬");
				this.wait();
			}
			returnValue = ""+list.get(0);
			list.remove(0);
			this.notify();
			System.out.println("pop="+list.size());
		}catch(InterruptedException ex){
			ex.printStackTrace();
		}
		return returnValue;
	}
	
}
