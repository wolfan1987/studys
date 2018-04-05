package org.andrewliu.java7thread.test;

import java.io.File;
import java.io.FileFilter;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 通过“毒丸”对象来关闭服务:即：当得到这个对象时，立即停止.如在:FIFO中，到了某个对象，表示队列已经结束
 * @author de
 *
 */
public class IndexingService {
	private static final File root = new File("c:/");
	private static final File POISON = new File("");
	private final IndexerThread consumer = new IndexerThread();
	private final CrawlerThread producer = new CrawlerThread();
	private final BlockingQueue<File> queue = new LinkedBlockingQueue<File>();
	private final FileFilter fileFilter = new FileFilter(){
		@Override
		public boolean accept(File pathname) {
			return false;
		}
		
	};
	
	
	public void start(){
		producer.start();
		consumer.start();
	}
	
	public void stop(){
		producer.interrupt();
	}
	
	public void awaiTermination() throws InterruptedException{
		consumer.join();
	}
	
	class CrawlerThread extends Thread{
		public void run(){
			try{
				
				crawl(root);
			}catch(InterruptedException e){
				
			}finally{
				while(true){
					try{
						queue.put(POISON);
						break;
					}catch(InterruptedException e1){
						//不处理，重新尝试
					}
				}
			}
		}
		
		private void crawl(File root) throws InterruptedException {  
			
		}
	}
	
	class IndexerThread extends Thread{
		public void run(){
			try{
				while(true){
					File file = queue.take();
					if(file == POISON){
						break;
					}else{
						indexFile(file);
					}
				}
			}catch( InterruptedException consumed){
				
			}
		}
		private void indexFile(File root) throws InterruptedException {  
			
		}
	}
}
