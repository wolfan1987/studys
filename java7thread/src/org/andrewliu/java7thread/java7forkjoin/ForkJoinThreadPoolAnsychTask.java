package org.andrewliu.java7thread.java7forkjoin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.TimeUnit;

/**
 * 用Fork/Join框架对ForkJoinTask时，可以以同步或异步方式运行，当采用同步方式执行时，发送任务给Fork/Join线程池的方法
 * 直到任务执行完成后才会返回结果，而采用异步方式执行时，发送任务给执行器的方法将立即返回结果，但是任务仍能够继续执行.
 * 
 * 当采用同步时，调用 这些方法时，任务被挂起，直到任务被发送到Fork/Join线程池中执行完成。这种方式允许ForkJoinPool类窃取算法来分配一个
 * 新任务给在执行休眠任务的工作者线程。当task调用 join()或get()方法时，ForkJoinPool可以使用工作窃取算法。
 * 当采用异步方式方法（调用一个任务书的fork()方法)时，任务将继续执行，ForkJoinPool类无法使用工作窃取算法来提升性能。
 * 
 * @author de
 *
 *查找文件夹或子文件夹下的文件扩展名，ForkJoinTask类处理文件夹下的内容，而文件夹下的子文件夹，任务将以异步的方式方式发送一个新的任务给ForkJoinPool类
 *对于每个文件夹中的文件，任务将检查任务文件的扩展名，如果符合条件就将其增加到结果列表。
 */
public class ForkJoinThreadPoolAnsychTask {

	public static void main(String[] args) {
		ForkJoinPool pool = new ForkJoinPool();
		//查三个目录的任务
		FolderProcessor system = new FolderProcessor("C:\\Windows", "log");
		FolderProcessor  apps = new FolderProcessor("C:\\Program Files","log");
		FolderProcessor  documents = new FolderProcessor("C:\\Documents And Settings","log");
		//执行任务
		pool.execute(system);
		pool.execute(apps);
		pool.execute(documents);
		
		do{
			
			System.out.printf("**************************\n");
			System.out.printf("Main: Parallelism: %d\n", pool.getParallelism());
			System.out.printf("Main: Active Threads: %d\n", pool.getActiveThreadCount());
			System.out.printf("Main: Task Count: %d\n", pool.getQueuedTaskCount());
			System.out.printf("Main: Steal Count: %d\n", pool.getStealCount());
			System.out.printf("*************************\n");
			try{
				TimeUnit.SECONDS.sleep(1);
			}catch(InterruptedException e){
				e.printStackTrace();
			}
		}while((!system.isDone())||(!apps.isDone())||(!documents.isDone()));
		
		pool.shutdown();
		List<String> results;  
		results = system.join();  //得到最后的结果
		System.out.printf("System: %d files found.\n", results.size());
		results = apps.join();
		System.out.printf("Apps: %d files found.\n", results.size());
		results = documents.join();
		System.out.printf("Documents: %d files found.\n", results.size());
	}
}


class FolderProcessor extends RecursiveTask<List<String>>{

	private static final long serialVersionUID = 8599139283510697952L;

	
	private String path;
	private String extension;
	
	public FolderProcessor(String path, String extension) {
		super();
		this.path = path;
		this.extension = extension;
	}



	@Override
	protected List<String> compute() {
		
		List<String>  list = new ArrayList<>();
		List<FolderProcessor> tasks = new ArrayList<>();
		File file = new File(path);
		File content[] = file.listFiles();
		
		if(content !=null){
			for ( int i = 0; i < content.length; i++){
				if(content[i].isDirectory()){   //如果是目录，就重启一个任务，并以异步方式运行
					FolderProcessor task = new FolderProcessor(content[i].getAbsolutePath(), extension);
					task.fork();  //将任务以异步方式执行,并发送到线程池
					tasks.add(task);
				}else{  //否则表示是个文件，如果是要找的文件扩展名，则将结果加到list中
					if(checkFile(content[i].getName())){
						list.add(content[i].getAbsolutePath());
					}
				}
			}
			
			if(tasks.size()>50){ 
				System.out.printf("%s: %d tasks ran.\n", file.getAbsolutePath(),tasks.size());
			}
			
			//将tasks中的任务作为同步执行，并得到执行结果,在这里改变了list的结果
			addResultsFromTasks(list,tasks);
		}
		
		return  list;
	}
	
	/***
	 * join()方法来等待任务的结束，然后获取它们的结果，也可以使用get()方法以下的两个版本来完成这个目的。
	 * get():  如果ForkJoinTask类执行结束，或者一直等到结束，那么get()方法的这个版本则返回由compute()方法返回的结果.
	 * get(long timeout,TimeUnit unit): 如果任务的结果未准备好，那么get()方法的这个版本将等待指定的时间，如果超过指定的时间了。任务的结果仍
	 *                                   未准备好，那么这个方法将返回null值.
	 *  get()方法与join()方法的主要区别：
	 *  join()方法不能被中断，如果中断调用join()方法的线程，方法将抛出InterruptedException异常.
	 *  如果任务抛出任何运行时异常，那么get()方法将返回ExecutionException异常,但join()方法将返回RuntimeException异常.                                 
	 * @param list
	 * @param tasks
	 */
	private void addResultsFromTasks(List<String> list,List<FolderProcessor> tasks){
		for ( FolderProcessor item : tasks){
			list.addAll(item.join());  //item.join(),等待执行完成后通过compute()方法返回结果
		}
	}
	
	private boolean checkFile(String name){
		return name.endsWith(extension);
	}
	
}