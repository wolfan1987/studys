package org.andrewliu.thread.test;

import java.util.concurrent.ThreadFactory;

/**
 * 定制自己的ThreadFactory工厂，主要是用自己的线程工厂来产生线程，可将实现了concurrent包中的ThreadFactory的线程工厂
 * 用于Executor.newXXXPool( new MySelfThreadFactory())中，以设置工厂产生线程的后台、优先级、名称
 * @author de
 *
 */
public class DaemonThreadFactory  implements ThreadFactory{

	@Override
	public Thread newThread(Runnable r) {
		Thread t = new Thread(r);
		t.setPriority(Thread.NORM_PRIORITY);
		t.setDaemon(true);
		t.setName("my factory thread--"+ Math.random());
		return t;
	}

}
