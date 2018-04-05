package org.andrewliu.java7thread.test;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.FutureTask;
import java.util.concurrent.RunnableFuture;

public  abstract class SocketUsingTask<T> implements CancellableTask<T> {

	private Socket socket;
	protected synchronized void setSocket(Socket s){
		this.socket = s;
	}
	
	@Override
	public void cancel() {
		try{
			if (socket != null){
				socket.close();
			} 
		}catch (IOException ignored){
			
		}
	}

	@Override
	public RunnableFuture<T> newTask() {
		return new FutureTask<T>(this){
			public boolean cancel(boolean mayInterruptIfRunning){
				try{
					SocketUsingTask.this.cancel();
				}finally{
				  	return super.cancel(mayInterruptIfRunning);
				}
			}
		};
	}
	

}
