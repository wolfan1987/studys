package org.andrewliu.thread.cppartten;

/**
 * 测试一个生产者与一个消费者操作栈
 * @author de
 *
 */
public class StackPCTest {

	public static void main(String[] args) {
		MyStack myStack = new MyStack();
		StackProducer  sp = new StackProducer(myStack);
		StackConsumer sc = new StackConsumer(myStack);
		
		StackPThread spt = new StackPThread(sp);
		StackCThread sct = new StackCThread(sc);
		
		spt.start();
		sct.start();
		
	}
	
}


class  StackPThread extends  Thread{
	private  StackProducer p ;
	
	public  StackPThread(StackProducer p){
		super();
		this.p = p;
	}
	
	@Override
	public void run(){
		while(true){
			p.pushService();
		}
	}
}

class StackCThread extends Thread{
	private  StackConsumer c ;
	
	public  StackCThread(StackConsumer c){
		super();
		this.c = c;
	}
	
	@Override
	public void run(){
		while(true){
			c.popService();
		}
	}
	
}