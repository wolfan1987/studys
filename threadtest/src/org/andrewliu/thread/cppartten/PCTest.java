package org.andrewliu.thread.cppartten;

/**
 * 生产者消费者测试
 * @author de
 *
 */
public class PCTest {

	
	public static void main(String[] args) {
		String lock = new String();
		Producer p = new Producer(lock);
		Consumer c = new Consumer(lock);
		PTest1  p1 = new  PTest1(p);
		CTest1  c1 = new  CTest1(c);
		p1.start();
		c1.start();
	}

}


class PTest1 extends  Thread{
	private  Producer  p;
	
	public PTest1(Producer p){
		super();
		this.p = p;
	}
	
	@Override
	public void run(){
		while(true){
			p.setValue();
		}
	}
}

class CTest1 extends  Thread{
	private  Consumer  c;
	
	public CTest1(Consumer c){
		super();
		this.c = c;
	}
	
	@Override
	public void run(){
		while(true){
			c.getValue();
		}
	}
}