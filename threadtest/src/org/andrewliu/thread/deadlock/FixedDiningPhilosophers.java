package org.andrewliu.thread.deadlock;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 5个哲学家用5根筷子吃饭，用多线程来等待与协调
 * @author de
 *
 */
public class FixedDiningPhilosophers {

	public static void main(String[] args) throws InterruptedException {
		int ponder = 5;
		int size = 5;
		ExecutorService exec = Executors.newCachedThreadPool();
		Chopstick[] sticks = new Chopstick[size];
		for(int i = 0; i < size; i++){
			sticks[i] = new Chopstick();
		}
		for(int i = 0; i <size; i++)
		if(i<(size-1)){//当i!=4时，全先右后左
			exec.execute(new Philosopher(sticks[i],sticks[i+1],i,ponder));
		}else{//当是最后一个人(5)时，先左后右
			//打破死循环（拿筷子如果全部先右后左的话，会死锁，打破一个哲学家的拿筷子方式（先左后右）就不会产生死锁。
			exec.execute(new Philosopher(sticks[0],sticks[i],i,ponder));
		}
		
		TimeUnit.SECONDS.sleep(5);
		exec.shutdownNow();
	}
}
