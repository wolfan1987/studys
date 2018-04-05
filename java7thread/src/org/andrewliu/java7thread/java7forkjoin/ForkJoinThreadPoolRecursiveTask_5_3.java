package org.andrewliu.java7thread.java7forkjoin;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.TimeUnit;

/**
 * 用Fork/Join框架实现执行任务并返回结果。
 * RecursiveTask类继承ForkJoinTask类，并实现了Executor Framework的Future接口。使得返回结果成为可能.
 * 
 * 如果任务需要解决的问题大于预先定义的大小，那么就要将这个问题拆分成多个子任务，并使用Fork/Join框架来执行这些子任务。执行完成
 * 后，原始任务获取到所有这些子任务产生的结果，合并这些结果，返回最终的结果。当原始任务在线程池中执行结束后，将高效地获取到整个问题
 * 的最终结果。
 * @author de
 *
 */
public class ForkJoinThreadPoolRecursiveTask_5_3 {

	
	
	public static void main(String[] args) {
		DocumentMock mock = new DocumentMock();
		//构造一个100行，每行1000个词，并记录the单词在文档中出现的次数
		String[][]  document = mock.generateDocument(100, 1000, "the");
		//构建一个在document中查找the单词的任务，从每0行到100行
		DocumentTask task = new DocumentTask(document,0,100,"the");
		ForkJoinPool pool = new ForkJoinPool();
		//执行DocumentTask
		pool.execute(task);
		do{
			System.out.printf("********************************\n");
			System.out.printf("Main: Parallelism: %d\n", pool.getParallelism());
			System.out.printf("Main: Active Threads: %d\n", pool.getActiveThreadCount());
			System.out.printf("Main: Task Count: %d\n", pool.getQueuedTaskCount());
			System.out.printf("Main: Steal Count: %d\n", pool.getStealCount());
			System.out.printf("********************************\n");
			try{
				TimeUnit.SECONDS.sleep(1);
			}catch(InterruptedException e){
				e.printStackTrace();
			}
		}while(!task.isDone());
		//通知停止所有任务
		pool.shutdown();
		try{
			//等待所有任务执行完成
			pool.awaitTermination(1, TimeUnit.DAYS);
		}catch(InterruptedException e){
			e.printStackTrace();
		}
		try{
			System.out.printf("Main: The word appears %d in the document", task.get());
		}catch(InterruptedException | ExecutionException e){
			e.printStackTrace();
		}
	}
}

class DocumentMock{
	private  String words[] = {"the","hello","goodby","packt","java","thread","pool","random","class","main"};
	/**
	 * 构造一篇文档
	 * @param numLines
	 * @param numWords
	 * @param word
	 * @return
	 */
	public String[][] generateDocument(int numLines,int numWords,String word){
		int counter = 0;
		String  document[][] = new String[numLines][numWords];
		Random random = new Random();
		for (int i = 0 ; i < numLines; i++){
			for(int j = 0; j < numWords; j++){
				int index = random.nextInt(words.length);
				document[i][j] = words[index];
				if(document[i][j].equals(word)){
					counter++;
				}
			}
		}
		System.out.println("DocumentMock: The word appers"+counter+" times in the document");
		return document;
	}
}

/**
 * 分析整个文件中的字符
 * @author de
 *
 */
class DocumentTask extends RecursiveTask<Integer>{

	private static final long serialVersionUID = 9467171433843959L;
	
	private String document[][];
	private int start,end;
	private String word;
	
	public DocumentTask(String[][] document, int start, int end, String word) {
		super();
		this.document = document;
		this.start = start;
		this.end = end;
		this.word = word;
	}

	@Override
	protected Integer compute() {
		int result = 0;
		if(end-start<10){
			//10行之内，直接通过LineTask处理
			result = processLines(document,start,end,word);
		}else{  //否则分到两个DocumentTask中去处理
			int mid = (start+end)/2;
			DocumentTask task1 = new DocumentTask(document,start,mid,word);
			DocumentTask task2 = new DocumentTask(document,mid,end,word);
			//执行任务
			invokeAll(task1,task2);
			
			try{
				//将执行任务的结果合并
				result = groupResults(task1.get(),task2.get());
			}catch(InterruptedException | ExecutionException e){
				e.printStackTrace();
			}
		}
		return  result;
	}
	
	/**
	 * 处理一行中的字符
	 * @param document
	 * @param start
	 * @param end
	 * @param word
	 * @return
	 */
	private Integer processLines(String[][] document,int start ,int end,String word){
		List<LineTask>  tasks = new ArrayList<LineTask>();
		for ( int i = start; i < end; i++){
			LineTask task = new LineTask(document[i],0,document[i].length,word);
			tasks.add(task);
		}
		//一行就一个任务处理，不再分解
		invokeAll(tasks);
		int result = 0;
		for ( int i = 0; i < tasks.size(); i++){
			LineTask task = tasks.get(i);
			try{
				//得到处理的结果
				result = result+task.get();
			}catch(InterruptedException | ExecutionException e){
				e.printStackTrace();
			}
		}
		return new Integer(result);
	}
	
	/**
	 * 合并两个任务的结果
	 * @param number1
	 * @param number2
	 * @return
	 */
	private Integer groupResults(Integer number1,Integer number2){
		Integer result;
		result = number1+number2;
		return result;
	}
	
}

/**
 * 处理一行的任务
 * @author de
 *
 */
class   LineTask extends RecursiveTask<Integer>{

	private static final long serialVersionUID = 5275594774222164201L;

	private String line[];
	private int start,end;
	private String word;
	
	public LineTask(String[] line, int start, int end, String word) {
		super();
		this.line = line;
		this.start = start;
		this.end = end;
		this.word = word;
	}

	@Override
	protected Integer compute() {
		Integer result = null;
		//当一行的大小《100时，直接统计
		if(end - start < 100){
			result = count(line,start,end,word);
		}else{ //否则分解到两个任务中去统计
			int mid = (start+end)/2;
			LineTask task1 = new LineTask(line,start,mid,word);
			LineTask task2 = new LineTask(line,mid,end,word);
			invokeAll(task1,task2);
			try{
				//将两个结果合并
				result = groupResults(task1.get(),task2.get());
			}catch(InterruptedException | ExecutionException e){
				e.printStackTrace();
			}
		}
		return result;
	}
	 /**
	  * 统计字符数
	  * @param line
	  * @param start
	  * @param end
	  * @param word
	  * @return
	  */
	private Integer count(String[] line,int start,int end,String word){

		int counter;
		counter = 0;
		for ( int i = start; i < end; i++){
			if(line[i].equals(word)){
				counter++;
			}
		}
		try{
			Thread.sleep(10);
		}catch (InterruptedException e){
			e.printStackTrace();
		}
		return counter;
	}
	
	private Integer groupResults(Integer number1,Integer number2){
		Integer result;
		result = number1+number2;
		return result;
	}
}