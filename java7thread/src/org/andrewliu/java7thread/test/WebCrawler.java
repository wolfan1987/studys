package org.andrewliu.java7thread.test;

import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 使用TrackingExecutor来保存未完成的任务以备后续执行
 * @author de
 *
 */
public abstract class WebCrawler {

	
	private volatile TrackingExecutor exec;
	private final Set<URL> urlsToCrawl  =  new HashSet<URL>();
	
	public synchronized void start(){
		exec = new TrackingExecutor(Executors.newCachedThreadPool());
		for( URL url : urlsToCrawl){
			submitCrawlTask(url);
		}
		urlsToCrawl.clear();
	}
	
	public synchronized void stop() throws InterruptedException{
		try{
			saveUncrawled(exec.shutdownNow());
			if(exec.awaitTermination(1000, TimeUnit.MILLISECONDS)){
				saveUncrawled(exec.getCancelledTasks());
			}
		}finally{
			exec = null;
		}
	}
	
	protected abstract List<URL> processPage(URL url);
	private void saveUncrawled(List<Runnable> uncrawled){
		for(Runnable task : uncrawled){
			urlsToCrawl.add(((CrawlTask)task).getPage());
		}
	}
	
	private void submitCrawlTask(URL u){
		exec.execute(new CrawlTask(u));
	}
	
	private class CrawlTask implements Runnable {
		private final URL url ;
		public CrawlTask(URL url){
			this.url = url;
		}
		public void run(){
			for ( URL link : processPage(url)){
				if(Thread.currentThread().isInterrupted()){
					return ;
				}
				submitCrawlTask(link);
			}
		}
		
		public URL getPage(){
			return url;
		}
	}
	
}
