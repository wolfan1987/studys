package org.andrewliu.thread.test;

import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 后台线程执行器,暂时不知道如何使用
 * @author de
 *
 */
public class DaemonThreadPoolExecutor    extends ThreadPoolExecutor{

	public DaemonThreadPoolExecutor() {
		super(0, Integer.MAX_VALUE, 60L, TimeUnit.SECONDS, new SynchronousQueue<Runnable>(),
				new DaemonThreadFactory());
	}

}
