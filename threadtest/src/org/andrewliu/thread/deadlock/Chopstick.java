package org.andrewliu.thread.deadlock;

/**
 * 哲学家吃饭用的筷子
 * @author de
 *
 */
public class Chopstick {

	private boolean taken = false;
	/**
	 * 拿起筷子，如果这根筷子的taken=true，表示有人在用
	 * @throws InterruptedException
	 */
	public synchronized void take() throws InterruptedException{
		while(taken){
			wait();
		}
		taken = true;
	}
	
	/**
	 * 放下筷子，将此筷子的taken改为false，并唤醒其它线程可以来拿这根
	 */
	public synchronized void drop(){
		taken = false;
		notifyAll();
	}
}
