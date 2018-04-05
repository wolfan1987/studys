package org.andrewliu.java7thread.test;

import java.io.PrintWriter;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * ��LogWriter��ӿɿ���ȡ������
 * @author de
 *
 */
public class LogService {

	private final BlockingQueue<String> queue ;
	private final LoggerThread loggerThread;
	private final PrintWriter writer;
	private boolean isShutdown;
	private int reservations;
	
	private final ExecutorService exec =  Executors.newSingleThreadExecutor();
	
	public LogService(PrintWriter writer){
		this.writer = writer;
		this.queue = new LinkedBlockingQueue<String>(500);
		this.loggerThread = new LoggerThread(writer);
	}
	
	public void start(){
		loggerThread.start();
	}
	public void stop() throws InterruptedException{
		synchronized(this){
			isShutdown = true;
		}
		loggerThread.interrupt();
		
		/***�����ǻ���Executorservice�ر�**/
		try{
			exec.shutdown();
			exec.awaitTermination(3000, TimeUnit.MILLISECONDS);
		}finally{
			writer.close();
		}
		/*****end*****/
	}
	
	public void log(String msg) throws InterruptedException{
		synchronized (this){
			if(isShutdown){
				throw new IllegalStateException("eee");
			}
			++reservations;
		}
		queue.put(msg);
		
		/**����Executorserviceд��־***/
//		try{
//			exec.execute((new WriteTask(msg));
//		}catch(RejectedExecutionException ignored){
//			
//		}
	}
	
	private class LoggerThread extends Thread{
		private final PrintWriter writer;
		public LoggerThread(PrintWriter writer){
			this.writer = writer;
		}
		
		public void run(){
			try{
				while(true){
					try{
						synchronized (LogService.this){
							if (isShutdown && reservations == 0){
								break;
							}
							String msg = queue.take();
							synchronized (LogService.this){
								--reservations;
							}
							writer.println(msg);
						}
					}catch(InterruptedException e){
						/***retry**/
					}
				}
			}finally{
				writer.close();
			}
		}
	}
}
