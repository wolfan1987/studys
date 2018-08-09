package org.andrewliu.thread.test2;

/**
 * 多线程术语（join)用法: join:加入一个线程，即下在执行的A线程调用B线程的join()方法，此时A挂起，B执行，直到B执行结束，A继续执行
 * 
 * @author de
 * 
 */
public class Joining {
	public static void main(String[] args) {
		Sleeper sleepy = new Sleeper("Sleepy",1500);
		Sleeper grumpy = new Sleeper("Grumpy",1500);
		
		Joiner dopey = new Joiner("Dopey",sleepy);
		Joiner doc = new Joiner("Doc",grumpy);
		grumpy.interrupt();
	}
}

class Sleeper extends Thread {
	private int duration;

	public Sleeper(String name, int sleepTime) {
		super(name);
		duration = sleepTime;
		start();
	}

	public void run() {
		try {
			sleep(duration);
		} catch (InterruptedException e) {
			System.out.println(getName() + " was interrupted.is Interrupted"
					+ isInterrupted());
			return;
		}
		System.out.println(getName() + " has awakened");
	}

}

class Joiner extends Thread {
	private Sleeper sleeper;

	public Joiner(String name, Sleeper sleeper) {
		super(name);
		this.sleeper = sleeper;
		start();
	}

	public void run() {
		try {
			sleeper.join();
		} catch (InterruptedException e) {
			System.out.println(" Interrupted ...");
		}

		System.out.println(getName() + "  join completed");
	}
}