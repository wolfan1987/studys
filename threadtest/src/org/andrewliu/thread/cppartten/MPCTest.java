package org.andrewliu.thread.cppartten;

/**
 * 多生产者，多消费者测试
 * @author de
 *
 */
public class MPCTest {

	public static void main(String[] args) {
		String lock = new String();
		MProducer p = new MProducer(lock);
		MConsumer c = new MConsumer(lock);
		
		MPTest1[]  mps = new  MPTest1[2];
		MCTest1[]  cps = new  MCTest1[2];
		
		for( int i = 0; i < 2 ; i++){
			mps[i] = new MPTest1(p);
			mps[i].setName("生产者"+(i+1));
			cps[i] = new MCTest1(c);
			cps[i].setName("消费者"+(i+1));
			mps[i].start();
			cps[i].start();
		}
		
	}
}


class MPTest1 extends  Thread{
	private  MProducer  p;
	
	public MPTest1(MProducer p){
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

class MCTest1 extends  Thread{
	private  MConsumer  c;
	
	public MCTest1(MConsumer c){
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