package org.andrewliu.java7thread.test;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


/**
 * ͨ��newTaskFor���Ǳ�׼��ȡ��������װ��һ��������(��ȡ��I/O�����ȡ���е��߳�)
 * �̳�ThreadPoolExecutor���Ա㵱��ʵ��callable��ʵ����CancellableTaskʱ������CancellableTask<T>���͵Ķ���
 * @author de
 *
 */
public class CancellingExecutor extends ThreadPoolExecutor{

	public CancellingExecutor(int corePoolSize, int maximumPoolSize,
			long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
		super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
	}
	
	protected <T>  RunnableFuture<T>   newTaskFor(Callable<T> callable){
		if(callable instanceof CancellableTask){
			return ((CancellableTask<T>) callable).newTask();
		}else{
			 return super.newTaskFor(callable);
		}
	}
	
}
