package org.andrewliu.java7thread.test;

import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

/**
 * 使用ConcurrentHashMap+FutureTask+同步机制来初始化缓存
 * 同步机制可以用ConcurrentHashMap的putIfAbsend方法来代替（添加之前先检查)
 * @author de
 *
 */
public class Memoizer<A,V>  implements Computable<A,V> {
	//私有+不可变引用的并发安全的对象
	private final ConcurrentMap<A,Future<V>> cache = new ConcurrentHashMap<A,Future<V>>();
	//计算器
	private final Computable<A,V> c;
	public Memoizer(Computable<A,V> c){
		this.c = c;
	}
	
	//计算某个参数的值
	@Override
	public V compute(final A arg) throws InterruptedException {
		while(true){
			//先从缓存中得到这个参数的计算结果，如果淌有就计算，不然就直接从Future中返回此arg的值
			Future<V> f = cache.get(arg);
			if ( f == null){
				//定义一个计算装饰
				Callable<V> eval = new Callable<V>(){

					@Override
					public V call() throws Exception {
						return c.compute(arg);
					}
					
				};
				//将FutureTask与Callable关联计算
				FutureTask<V> ft = new FutureTask<V>(eval);
				//将与当前参数有关的FutureTask以arg为key，存入缓存(如果以前没有的话)
				f = cache.putIfAbsent(arg, ft);
				if(f == null){
					//将要返回的Futhue指向最新的FutureTask(ft),并运行ft.run方法(进行结果计算)，以便调用f.get()时，可以得到返回值 
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
