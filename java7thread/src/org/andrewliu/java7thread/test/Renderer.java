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
 * ������ɷ���(CompletionService)���������������ҳ����Ⱦ�������ܣ�����������ʱ���Լ������Ӧ�ԡ�
 * Ϊÿһ��ͼ������ض�����һ���������񣬲����̳߳���ִ�����ǣ��Ӷ������е����ع���ת��Ϊ���еĹ��̣��⽫����
 * ��������ͼ�����ʱ�䡣����ͨ����CompletinService�л�ȡ����Լ�ʹÿ��ͼƬ��������ɺ�������ʾ��������ʹ�û�
 * ���һ�����Ӷ�̬�͸���Ӧ�����û����档
 * CompletionService��Executor��BlockingQueue�Ĺ���������һ�𡣿��Խ�Callable�����ύ������ִ�У�Ȼ��ʹ�������ڶ���
 * ������take��poll�ȷ������������ɵĽ��������Щ����������ʱ������װΪFuture��ExecutorCompletioinServiceʵ����
 * CompletionService���������㲿��ί�и�һ��Executor.
 * ExecutorCompletionService�ڹ��캯���д���һ��BlockingQueue�����������ɵĽ�������������ʱ������ FutureTask�е�done������
 * ���ύĳ������ʱ�����������Ȱ�װΪһ��QueueingFuture������FutureTaskr��һ�����࣬Ȼ���ٸ�д�����done�����������������BlockingQueue�У�
 * take��poll����ί�и���BlockingQueue����Щ�������ڵó����֮ǰ������
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
		//��װһ��CompletoinService������executorִ������
		CompletionService<ImageData>  completionService = new ExecutorCompletionService<ImageData>(executor);
		for(final ImageInfo imageInfo : info){
			completionService.submit(new Callable<ImageData>(){  //����ͼ�񣬲��ύ����ʱ�ύ�Ľ�����ŵ����õ�completionQueue�����У��Է������take
				   public  ImageData call(){
					   return imageInfo.downLoadImage();
				   }
			});
			
			renderText(source);
			
			try{
				for(int t = 0 , n = info.size(); t < n; t++){
					Future<ImageData> f = completionService.take();//��CompletionService��CompletionQueue�������ó����ؽ��
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
