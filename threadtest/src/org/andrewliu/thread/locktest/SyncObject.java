package org.andrewliu.thread.locktest;

public class SyncObject {
	public static void main(String[] args) {
		final DualSynch ds = new DualSynch();
		new Thread() {
			public void run() {
				ds.f();
			}
		}.start();
		ds.g();
	}

}

/**
 * f()方法获得的锁是当前对象this，g()方法的锁获取的是syncObject的锁，所以A和B任务调用这两个方法时不会受有阻塞，可以同时访问。
 * @author de
 *
 */
class DualSynch {

	private Object syncObject = new Object();

	public synchronized void f() {
		for (int i = 0; i < 5; i++) {
			System.out.println("f()");
			Thread.yield();
		}
	}

	public void g() {
		synchronized (syncObject) {
			for (int i = 0; i < 5; i++) {
				System.out.println("g()");
				Thread.yield();
			}
		}
	}
}