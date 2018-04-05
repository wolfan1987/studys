package org.andrewliu.java7thread.test;

import java.util.concurrent.Callable;
import java.util.concurrent.RunnableFuture;

/**
 * 在ThreadPoolExecutor的新增功能中，当把一个Callable提交给ExecutorService时，submit方法会返回一个Future，可以通过这个Future来取消任务，
 * newTaskFor是一个工厂方法，它将创建Future来代表任务，newTaskFor还能返回 一个RunnableFuture接口，该接口扩展了Future和Runnable（并由FutureTask)
 * 实现.通过定制表示任务的Future可以改变Future.cancel的行为.
 * @author de
 *
 * @param <T>
 *继承Callable接口，并重写cancel和newTask方法，实现该接口可以将非标准的取消操作封装在一个任务中
 */
public interface CancellableTask<T>  extends Callable<T>{
	
	void cancel();
	RunnableFuture<T> newTask();

}
