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
 * ʵ��Callable�ӿڣ���Ϊ�˴���Runnable�ӿڣ��˽ӿ���ͨ�����ͷ��ض��߳��еĽ������Future<T>��װ���
 * 
 * Callable�ӿ��е�call������ֻ����ExecutorService.submit()��������
 * @author de
 *
 */
public class TaskWithResult  implements Callable<String>{

	private  int id;
	public TaskWithResult(int id){
		this.id = id;
	}
	/**
	 * �൱��Runnable�е�run����
	 */
	@Override
	public String call() throws Exception {
		return "result of TaskWithResult "+ id;
	}
	
	
	
	public static void main(String[] args) {
		ExecutorService  exec = Executors.newCachedThreadPool();
		//�������淵�ؽ����List
		List<Future<String>>  results = new ArrayList<Future<String>>();
		for ( int i = 0; i < 10; i++){
			//submit���������Future<String> ����,������isDone()��������ѯFuture�Ƿ��Ѿ����
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
				//������Future�е�get()������ȡ��������Future������û��ɣ�����get����������������ָ������ʱ���磺Future.get(10000ms),����
				//ʱ�������û�еõ��������ֹͣ����;
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
