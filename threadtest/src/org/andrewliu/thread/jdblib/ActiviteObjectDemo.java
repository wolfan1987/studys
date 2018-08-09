package org.andrewliu.thread.jdblib;

import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class ActiviteObjectDemo {

	private ExecutorService ex = Executors.newSingleThreadExecutor();//单线程执行器，即：加入到队列中的任务一次只能调用一个，不会同时调用
	private Random rand = new Random(47);
	
	private void pause(int factor){
		try{
			TimeUnit.MILLISECONDS.sleep(100+rand.nextInt(factor));
		}catch(InterruptedException e){
			System.out.println("sleep() interrupted!");
		}
	}
	
	public Future<Integer> calculateInt(final int x,final int y ){  //将业务以发送消息（方法调用）形式来用Callable执行，然后用Future返回结果
		return ex.submit(new Callable<Integer>(){
			public Integer call(){
				System.out.println("Starting "+ x + " + "+ y);
				pause(500);
				return x+y;
			}
		});
	}
	
	public Future<Float> calculateFloat(final float x, final float y){
		return ex.submit(new Callable<Float>(){
			public Float call(){
				System.out.println("starting"+ x + "+ "+ y);
				pause(2000);
				return x+y;
			}
		});
	}
	
	public void shutdown(){
		ex.shutdown();
	}
	
	
	public static void main(String[] args) {
		ActiviteObjectDemo d1 = new ActiviteObjectDemo();
		List<Future<?>>  results = new CopyOnWriteArrayList<Future<?>>();  //保存发送消息执行后的结果
		for(float f = 0.0f; f< 1.0f; f+=0.2f){
			results.add(d1.calculateFloat(f, f));
		}
		for(int i = 0; i < 5; i++){
			results.add(d1.calculateInt(i, i));  //当调用d1.calculate时，ex是从队列中一次一个的执行任务并返回结果,这里是将方法调用排队，
			                                      	//使任务时刻都只能发生一个调用，从而将同步控制在消息级别上发生。
		}
		System.out.println(" All asynch calls made");
		
		while(results.size() > 0){
			for(Future<?> f : results){
				if(f.isDone()){ //如果任务完成了
					try {
						System.out.println(f.get());  //抽取结果
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
					results.remove(f); //抽取完后就将此Future删除掉。
				}
			}
		}
		
		d1.shutdown();
	}
}
