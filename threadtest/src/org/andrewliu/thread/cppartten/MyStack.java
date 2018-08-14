package org.andrewliu.thread.cppartten;

import java.util.ArrayList;
import java.util.List;

/**
 * 给生产消费线程使用的Stack
 * @author de
 *
 */
public class MyStack {

	private List<String>  list = new ArrayList<String>();
	
	public synchronized void push(){
		try{
			while(list.size() == 1){  //用if形式，只适合一生产者一消费者时使用，用while则可以适合N生产者和N消费者
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
			while(list.size() == 0){   //用if形式，只适合一生产者一消费者时使用，用while则可以适合N生产者和N消费者
				System.out.println("pop 操作中的:"+Thread.currentThread().getName()+"线程是 wait 状态");
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
