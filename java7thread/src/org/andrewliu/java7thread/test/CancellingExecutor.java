package org.andrewliu.java7thread.test;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


/**
 * 通过newTaskFor将非标准的取消操作封装在一个任务中(如取消I/O，或获取锁中的线程)
 * 继承ThreadPoolExecutor，以便当是实现callable的实例是CancellableTask时，返回CancellableTask<T>类型的对象
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
