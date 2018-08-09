package org.andrewliu.thread.test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * ��jdk5/6���Դ���Executor���߳�ִ����������Thread����
 * @author de
 *
 */
public class CachedThreadPool {

	public static void main(String[] args) {
		//ExecutorService�Ǿ��з����������ڵ�Executor�磺�������������ر�
		//newcachedThreadPool ����Ϊÿ������(ʵ����Runnable�ӿ�)����һ���̣߳��������������ʱ�ٴ����̣߳��ִ����ģ� �����������߳�
		ExecutorService execCache = Executors.newCachedThreadPool();
		//��newCachedThreadPool��ͬ������ֻ�ܴ���ָ�������������߳��磺5��,������ȴ����̣߳�Ȼ��ȴ�����һ�������ģ������������߳�,�����������͵ȴ�
		ExecutorService execFixed = Executors.newFixedThreadPool(5);
		//ֻʵ����һ���̣߳�����ж��������ӵ��߳��У�������ᰴ��ӵ�˳������ŶӵȺ�
		ExecutorService execSingled = Executors.newSingleThreadExecutor();
		for( int i = 0; i < 5; i++){
			execCache.execute(new FirstThreadTest());
		}
		//��������ִ������˳�����
		execCache.shutdown();
		
		
	}
}
