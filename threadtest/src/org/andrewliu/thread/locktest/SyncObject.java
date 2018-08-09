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
 * f()������õ����ǵ�ǰ����this��g()����������ȡ����syncObject����������A��B�����������������ʱ������������������ͬʱ���ʡ�
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