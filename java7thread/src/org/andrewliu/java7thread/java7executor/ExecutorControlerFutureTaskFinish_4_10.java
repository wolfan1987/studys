package org.andrewliu.java7thread.java7executor;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;


/**
 * 在执行器中控制FutureTask的完成，即在完成任务或取消任务时，进行一行其它他操作。
 * 这是通过实现FutureTask接口，重写done()方法来实现的。比如：如果想在任务运行完，发一封邮件，可以在done()方法中完成
 * Future在设置isDone()之后，就会调用done()方法.
 * @author de
 *
 */
public class ExecutorControlerFutureTaskFinish_4_10 {

	public static void main(String[] args) {
		//得到执行器
		ExecutorService executor = (ExecutorService) Executors.newCachedThreadPool();
		ResultTask resultTask[] = new  ResultTask[5];
		for( int i = 0; i < 5; i++){
			//实例化Task
			ExecutableTask executableTask = new ExecutableTask("Task"+i);
			//实例化控制结果的ResultTask，并将其和实际执行的任务关联,ResultTask可以控制ExecutableTask的执行.
			resultTask[i] = new ResultTask(executableTask);
			executor.submit(resultTask[i]);//交由执行器去执行可以控制结果的ResultTask
		}
		
		try{
			TimeUnit.SECONDS.sleep(5); //让main线程睡5秒,即先让任务执行5秒
		}catch(InterruptedException el){
			el.printStackTrace();
		}
		
		for ( int i = 0; i < resultTask.length; i++){
			//取消所有任务
			resultTask[i].cancel(true);  //
		}
		
		//取消任务时，如果还有任务没被取消，则输出他们的结果
		for ( int i = 0; i < resultTask.length; i++){
			try{
				if(!resultTask[i].isCancelled()){
					System.out.printf("%s\n",resultTask[i].get());
				}
			}catch(InterruptedException | ExecutionException e){
				e.printStackTrace();
			}
		}
		
		executor.shutdown();
	}
}


class ExecutableTask implements Callable<String>{
	
	private String name;

	public String getName() {
		return name;
	}

	public ExecutableTask(String name){
		this.name = name;
	}
	@Override
	public String call() throws Exception {
		try{
			long duration = (long)(Math.random()*10);
			System.out.printf("%s: Waiting %d seconds for results.\n", this.name,duration);
			TimeUnit.SECONDS.sleep(duration);
		}catch(InterruptedException e){
			e.printStackTrace();
		}
		return "Hello,World. I'm "+ name;
	}

}

/**
 * 实现自己的任务结果类，以扩展任务取消或结束后的行为
 * @author de
 *
 */
class ResultTask extends FutureTask<String>{

	private String name;
	public ResultTask(Callable<String> callable) {
		super(callable);
		this.name = ((ExecutableTask)callable).getName();
	}
	
	@Override
	protected void done(){
		if(isCancelled()){
			System.out.printf("%s: Has been canceled\n",name);
		}else{
			System.out.printf("%s: Has finished\n", name);
		}
	}
	
}