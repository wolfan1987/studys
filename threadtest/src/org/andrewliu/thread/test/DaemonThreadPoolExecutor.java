package org.andrewliu.thread.test;

import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * ��̨�߳�ִ����,��ʱ��֪�����ʹ��
 * @author de
 *
 */
public class DaemonThreadPoolExecutor    extends ThreadPoolExecutor{

	public DaemonThreadPoolExecutor() {
		super(0, Integer.MAX_VALUE, 60L, TimeUnit.SECONDS, new SynchronousQueue<Runnable>(),
				new DaemonThreadFactory());
	}

}
