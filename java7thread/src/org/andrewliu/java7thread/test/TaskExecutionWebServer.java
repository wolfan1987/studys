package org.andrewliu.java7thread.test;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class TaskExecutionWebServer {
	
	private static final int NTHREADS = 100;
	private static final Executor exec = Executors.newFixedThreadPool(NTHREADS);
	
	public static void main(String[] args) throws IOException {
		ServerSocket socket = new ServerSocket(80);
		while(true){
			final Socket connection = socket.accept();
			Runnable task = new Runnable(){
				public void run(){
					System.out.println(connection);
				}
			};
			exec.execute(task);
		}
	}

}

/**
 * Ϊÿ����������һ�����̵߳�Executor
 * @author de
 *
 */
class ThreadPerTaskExecutor implements Executor{
	public void execute(Runnable r){
		new Thread(r).start();
	}
}


/**
 * �ڵ����߳�����ͬ����ʽִ�����������Executor
 * @author de
 *
 */
class  WithinThreadExecutor implements Executor{
	public void execute(Runnable r){
		r.run();
	}
}