package org.andrewliu.thread.test;

import java.util.concurrent.ExecutorService;

/**
 * ��daemon���ɹ�
 * @author de
 *
 */
public class MyDaemonThreadPoolExecutorTest {

	public static void main(String[] args) {
		ExecutorService exec =  new DaemonThreadPoolExecutor();
		exec.execute(new SimpleDaemons());
		exec.shutdown();
	}
}
