package org.andrewliu.thread.cppartten;

/**
 * ��Stack����������
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
