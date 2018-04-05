package org.andrewliu.java7thread.test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * 使用Futurer用多线程下载图像
 * FutureTask实现了Runnable，可此可以将它提交给Executor来执行，或者直接调用它的run方法.
 * @author de
 *
 */
public class FutureRenderer {

	
	private final ExecutorService executor = Executors.newCachedThreadPool();
	
	void renderPage(CharSequence source){
		final List<String>   imageInfos = new ArrayList<String>();  //scanForImageInfo(source);
		Callable<List<String>> task = new Callable<List<String>>(){
			@Override
			public List<String> call() throws Exception {
				List<String>  result = new ArrayList<String>();
				for(String  imageInfo : imageInfos){
				      result.add(imageInfo);
				}
				return result;
			}
		};
		
		//执行这一步前，将会等到所有图片下载完后才会执行
		Future<List<String>>  future = executor.submit(task);  //此处将返回一个类型为List<string>的Future,以便后面用此Future得到实际的图片
		//renderText(source);
		
		try{
			List<String> imageData = future.get();
			for(String data : imageData){
				//rederImage(data);
			}
		}catch(InterruptedException e){
			Thread.currentThread().interrupt();//重新设置线程的中断状态
			//同于不需要结果，因此取消任务
			future.cancel(true);
		}catch(ExecutionException e){
			System.out.println(e.getCause());
		}
	}
	
}
