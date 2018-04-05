package org.andrewliu.java7thread.test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * 利用完成服务(CompletionService)从两个方面提高了页面渲染器的性能：缩短总运行时间以及提高响应性。
 * 为每一幅图像的下载都创建一个独立任务，并在线程池中执行它们，从而将串行的下载过程转换为并行的过程，这将减少
 * 下载所有图像的总时间。此外通过从CompletinService中获取结果以及使每张图片在下载完成后立刻显示出来，能使用户
 * 获得一个更加动态和高响应怀的用户界面。
 * CompletionService将Executor和BlockingQueue的功能整合在一起。可以将Callable任务提交给它来执行，然后使用类似于队列
 * 操作的take和poll等方法来获得已完成的结果，而这些结果会在完成时将被封装为Future。ExecutorCompletioinService实现了
 * CompletionService，并将计算部分委托给一个Executor.
 * ExecutorCompletionService在构造函数中创建一个BlockingQueue来保存计算完成的结果。当计算完成时，调用 FutureTask中的done方法。
 * 当提交某个任务时，该任务将首先包装为一个QueueingFuture，这是FutureTaskr的一个子类，然后再改写子类的done方法，并将结果放入BlockingQueue中，
 * take和poll方法委托给了BlockingQueue，这些方法会在得出结果之前阻塞。
 * @author de
 *
 */
public class Renderer {

	private final ExecutorService executor;
	Renderer(ExecutorService executor){
		this.executor = executor;
	}
	
	public void renderPage(CharSequence source){
		List<ImageInfo> info = scanForImageInfo(source);
		//包装一个CompletoinService，并由executor执行任务
		CompletionService<ImageData>  completionService = new ExecutorCompletionService<ImageData>(executor);
		for(final ImageInfo imageInfo : info){
			completionService.submit(new Callable<ImageData>(){  //下载图像，并提交。这时提交的结果将放到内置的completionQueue队列中，以方便后面take
				   public  ImageData call(){
					   return imageInfo.downLoadImage();
				   }
			});
			
			renderText(source);
			
			try{
				for(int t = 0 , n = info.size(); t < n; t++){
					Future<ImageData> f = completionService.take();//从CompletionService的CompletionQueue队列中拿出下载结果
					ImageData imageData  = f.get();
					renderImage(imageData);
				}
			}catch(InterruptedException e){
				Thread.currentThread().interrupt();
			}catch(ExecutionException e){
				System.out.println(e.getCause());
			}
		}
	}
	
	public List<ImageInfo>  scanForImageInfo(CharSequence source){
	     return  new ArrayList<ImageInfo>();	
	}
	
	public  void  renderText(CharSequence source){
		
	}
	
	public  void renderImage(ImageData imageData){
		
	}
}
