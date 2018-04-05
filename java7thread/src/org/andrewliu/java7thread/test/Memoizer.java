package org.andrewliu.java7thread.test;

import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

/**
 * ʹ��ConcurrentHashMap+FutureTask+ͬ����������ʼ������
 * ͬ�����ƿ�����ConcurrentHashMap��putIfAbsend���������棨���֮ǰ�ȼ��)
 * @author de
 *
 */
public class Memoizer<A,V>  implements Computable<A,V> {
	//˽��+���ɱ����õĲ�����ȫ�Ķ���
	private final ConcurrentMap<A,Future<V>> cache = new ConcurrentHashMap<A,Future<V>>();
	//������
	private final Computable<A,V> c;
	public Memoizer(Computable<A,V> c){
		this.c = c;
	}
	
	//����ĳ��������ֵ
	@Override
	public V compute(final A arg) throws InterruptedException {
		while(true){
			//�ȴӻ����еõ���������ļ�������������оͼ��㣬��Ȼ��ֱ�Ӵ�Future�з��ش�arg��ֵ
			Future<V> f = cache.get(arg);
			if ( f == null){
				//����һ������װ��
				Callable<V> eval = new Callable<V>(){

					@Override
					public V call() throws Exception {
						return c.compute(arg);
					}
					
				};
				//��FutureTask��Callable��������
				FutureTask<V> ft = new FutureTask<V>(eval);
				//���뵱ǰ�����йص�FutureTask��argΪkey�����뻺��(�����ǰû�еĻ�)
				f = cache.putIfAbsent(arg, ft);
				if(f == null){
					//��Ҫ���ص�Futhueָ�����µ�FutureTask(ft),������ft.run����(���н������)���Ա����f.get()ʱ�����Եõ�����ֵ 
					f = ft; ft.run();
				}
			}
			try{
				return f.get();
			}catch ( CancellationException e){
				cache.remove(arg,f);
			}catch ( ExecutionException e){
				System.out.println(e.getCause());
			}
		}
	}
}
