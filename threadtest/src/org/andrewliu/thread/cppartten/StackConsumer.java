package org.andrewliu.thread.cppartten;

/**
 * 从Stack中消费数据
 * @author de
 *
 */
public class StackConsumer {
	
	private MyStack myStack;
	public StackConsumer(MyStack myStack){
		super();
		this.myStack = myStack;
	}
	
	public void popService(){
		System.out.println("pop="+myStack.pop());
	}
	

}
