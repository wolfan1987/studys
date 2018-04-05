package org.andrewliu.java7thread.test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * ʹ��Futurer�ö��߳�����ͼ��
 * FutureTaskʵ����Runnable���ɴ˿��Խ����ύ��Executor��ִ�У�����ֱ�ӵ�������run����.
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
		
		//ִ����һ��ǰ������ȵ�����ͼƬ�������Ż�ִ��
		Future<List<String>>  future = executor.submit(task);  //�˴�������һ������ΪList<string>��Future,�Ա�����ô�Future�õ�ʵ�ʵ�ͼƬ
		//renderText(source);
		
		try{
			List<String> imageData = future.get();
			for(String data : imageData){
				//rederImage(data);
			}
		}catch(InterruptedException e){
			Thread.currentThread().interrupt();//���������̵߳��ж�״̬
			//ͬ�ڲ���Ҫ��������ȡ������
			future.cancel(true);
		}catch(ExecutionException e){
			System.out.println(e.getCause());
		}
	}
	
}
