package org.andrewliu.thread.test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * 实现Callable接口，是为了代替Runnable接口，此接口能通过泛型返回多线程中的结果，用Future<T>包装结果
 * 
 * Callable接口中的call方法，只能用ExecutorService.submit()方法调用
 * @author de
 *
 */
public class TaskWithResult  implements Callable<String>{

	private  int id;
	public TaskWithResult(int id){
		this.id = id;
	}
	/**
	 * 相当于Runnable中的run方法
	 */
	@Override
	public String call() throws Exception {
		return "result of TaskWithResult "+ id;
	}
	
	
	
	public static void main(String[] args) {
		ExecutorService  exec = Executors.newCachedThreadPool();
		//声明保存返回结果的List
		List<Future<String>>  results = new ArrayList<Future<String>>();
		for ( int i = 0; i < 10; i++){
			//submit方法会产生Future<String> 对象,可以用isDone()方法来查询Future是否已经完成
			Future<String>  rsfs = exec.submit(new TaskWithResult(i));
			if(rsfs.isDone()){
				try {
					System.out.println("for.rsfs.isdone"+rsfs.get(1000,TimeUnit.MILLISECONDS));
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (TimeoutException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else{
				System.out.println("rs.isnotdone="+rsfs.isDone());
			}
			results.add(rsfs);
		}
		for(Future<String>  fs : results){
			try {
				//可以用Future中的get()方法获取结果，如果Future方法还没完成，调用get方法会阻塞，或者指定阻塞时长如：Future.get(10000ms),到了
				//时间如果还没有得到结果，将停止阻塞;
				if(fs.isDone()){
					System.out.println("fs.isdone=="+fs.get());
				}
				System.out.println("outer.output="+fs.get());
			} catch (InterruptedException e) {
				System.out.println(e);
				e.printStackTrace();
				return;
			} catch (ExecutionException e) {
				System.out.println(e);
				e.printStackTrace();
			}finally {
				exec.shutdown();
			}
		}
	}

}
