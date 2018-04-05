package org.andrewliu.java7thread.test;

import java.util.concurrent.Callable;
import java.util.concurrent.RunnableFuture;

/**
 * ��ThreadPoolExecutor�����������У�����һ��Callable�ύ��ExecutorServiceʱ��submit�����᷵��һ��Future������ͨ�����Future��ȡ������
 * newTaskFor��һ��������������������Future����������newTaskFor���ܷ��� һ��RunnableFuture�ӿڣ��ýӿ���չ��Future��Runnable������FutureTask)
 * ʵ��.ͨ�����Ʊ�ʾ�����Future���Ըı�Future.cancel����Ϊ.
 * @author de
 *
 * @param <T>
 *�̳�Callable�ӿڣ�����дcancel��newTask������ʵ�ָýӿڿ��Խ��Ǳ�׼��ȡ��������װ��һ��������
 */
public interface CancellableTask<T>  extends Callable<T>{
	
	void cancel();
	RunnableFuture<T> newTask();

}
