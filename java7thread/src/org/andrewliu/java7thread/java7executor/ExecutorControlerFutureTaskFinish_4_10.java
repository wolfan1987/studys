package org.andrewliu.java7thread.java7executor;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;


/**
 * ��ִ�����п���FutureTask����ɣ�������������ȡ������ʱ������һ��������������
 * ����ͨ��ʵ��FutureTask�ӿڣ���дdone()������ʵ�ֵġ����磺����������������꣬��һ���ʼ���������done()���������
 * Future������isDone()֮�󣬾ͻ����done()����.
 * @author de
 *
 */
public class ExecutorControlerFutureTaskFinish_4_10 {

	public static void main(String[] args) {
		//�õ�ִ����
		ExecutorService executor = (ExecutorService) Executors.newCachedThreadPool();
		ResultTask resultTask[] = new  ResultTask[5];
		for( int i = 0; i < 5; i++){
			//ʵ����Task
			ExecutableTask executableTask = new ExecutableTask("Task"+i);
			//ʵ�������ƽ����ResultTask���������ʵ��ִ�е��������,ResultTask���Կ���ExecutableTask��ִ��.
			resultTask[i] = new ResultTask(executableTask);
			executor.submit(resultTask[i]);//����ִ����ȥִ�п��Կ��ƽ����ResultTask
		}
		
		try{
			TimeUnit.SECONDS.sleep(5); //��main�߳�˯5��,����������ִ��5��
		}catch(InterruptedException el){
			el.printStackTrace();
		}
		
		for ( int i = 0; i < resultTask.length; i++){
			//ȡ����������
			resultTask[i].cancel(true);  //
		}
		
		//ȡ������ʱ�������������û��ȡ������������ǵĽ��
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
 * ʵ���Լ����������࣬����չ����ȡ������������Ϊ
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