package org.andrewliu.thread.cppartten;

/**
 * 往Stack中生产数据
 * @author de
 *
 */
public class StackProducer {
	
	private MyStack  myStack;
	public StackProducer(MyStack myStack){
		super();
		this.myStack = myStack;
	}
	
	public void pushService(){
		myStack.push();
	}

}
