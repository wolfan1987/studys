package org.andrewliu.java7thread.test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class TrackingExecutor  extends AbstractExecutorService{

	private final ExecutorService exec ;
	
	
	
	//用同步的Set存入取消（中断）后的任务
	private final Set<Runnable> taskCancelledAtShutdown = Collections.synchronizedSet(new HashSet<Runnable>());
	
	
	public TrackingExecutor(ExecutorService exec){
		this.exec = exec;
	}
	
	public List<Runnable> getCancelledTasks(){
		if(!exec.isTerminated()){
			throw new IllegalStateException();
		}
		return new ArrayList<Runnable>(taskCancelledAtShutdown);
	}
	
	@Override
	public void shutdown() {
	}

	@Override
	public List<Runnable> shutdownNow() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isShutdown() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isTerminated() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean awaitTermination(long timeout, TimeUnit unit)
			throws InterruptedException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void execute(final Runnable runnable) {
		exec.execute(new Runnable(){
			public void run(){
				try{
					runnable.run();
				}finally{
					if(isShutdown() && Thread.currentThread().isInterrupted()){  //如果任务中断或取消了，就将其加入到set中
						taskCancelledAtShutdown.add(runnable);
					}
				}
			}
		});
	}

}
