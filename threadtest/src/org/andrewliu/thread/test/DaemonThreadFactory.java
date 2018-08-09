package org.andrewliu.thread.test;

import java.util.concurrent.ThreadFactory;

/**
 * �����Լ���ThreadFactory��������Ҫ�����Լ����̹߳����������̣߳��ɽ�ʵ����concurrent���е�ThreadFactory���̹߳���
 * ����Executor.newXXXPool( new MySelfThreadFactory())�У������ù��������̵߳ĺ�̨�����ȼ�������
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
