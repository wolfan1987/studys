package org.andrewliu.java7thread.java7executor;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * 通过CompletionService类，实现发送任务到执行器和结果获取分开处理，
 * CompletionService类有一个方法用来发送任务给执行器，还有一个方法为下一个已经执行结束的任务获取Future对象.
 * 其也是通过Executor来执行任务，这可以共享CompletionService对象，并发送任务到执行器，然后其他对象可以处理任务的结果,
 * 但其只能获取已执行结束的任务的Future对象,且这些对象只能被用来获取任务的结果。
 * CompletionService灰可以执行Callable或Runnable类型的任务，但Runnable对象不能产生结果。
 * 其有两种方法来获取任务已经完成的Future对象：
 * poll():无参数的poll()方法用于检查队列中是否有Future对象，若为空，则返回 null，否则返回队列中第一个元素，并移除此元素.
 * take():此方法也无参数，同样检查队列中是否有Future对象，若队列为空，它将阻塞线程直到队列中有可用的元素。如果队列中有元素，
 *       它将返回 队列中的第一个元素，并移除这个元素。
 * @author de
 *
 */
public class CompletionServiceSplitTaskAndResult_4_11 {

	public static void main(String[] args) {
		ExecutorService executor = Executors.newCachedThreadPool();
		//定义一个完成服务，并将执行器与其关联
		CompletionService<String>  service = new ExecutorCompletionService<>(executor);
		//请求生成报表，通过service报报表生成器(ReportGenerator)传给ReportProcess,再由ReportProcess获取生成结果
		ReportRequest faceRequest = new ReportRequest("Face", service);
		ReportRequest onlineReqquest = new ReportRequest("Online",service);
		Thread faceThread = new Thread(faceRequest);
		Thread onlineThread = new Thread(onlineReqquest);
		
		//处理报表线程
		ReportProcessor processor = new ReportProcessor(service);
		Thread senderThread = new Thread(processor);
		
		System.out.printf("Main: Starting the Threads\n");
		faceThread.start();
		onlineThread.start();
		senderThread.start();
		try{
			System.out.printf("Main: Waiting for the report generators.\n");
			//请请求生成报表工作先做完
			faceThread.join();
			onlineThread.join();
		} catch (InterruptedException e){
			e.printStackTrace();
		}
		
		System.out.printf("Main: Shutting down the executor.\n");
		executor.shutdown();
		try{
			//等待所有任务都执行完
			executor.awaitTermination(1,TimeUnit.DAYS);
		}catch(InterruptedException e){
			e.printStackTrace();
		}
		//提示ReportProcess可以获取Future中的结果了
		processor.setEnd(true);
		System.out.println("Main: Ends");
		
	}
}

/**
 * 报表生成类
 * @author de
 *
 */
class ReportGenerator implements Callable<String>{
	private String sender;
	private String title;
	public ReportGenerator(String sender, String title) {
		super();
		this.sender = sender;
		this.title = title;
	}
	@Override
	public String call() throws Exception {
		try{
			long duration = (long)(Math.random()*10);
			System.out.printf("%s_%s: ReportGernerator : Generating a report during %d seconds\n",this.sender,this.title,duration);
			TimeUnit.SECONDS.sleep(duration);
		}catch(InterruptedException e){
			e.printStackTrace();
		}
		String ret=sender+": "+title;
		return ret;
	}
	
}

/**
 * 封装和发送生成报表请求类
 * @author de
 *
 */
class ReportRequest implements Runnable{
	private String name;
	//将报表生成器给service，让其执行报表生成器的call方法。
	private CompletionService<String> service;
	
	public ReportRequest(String name, CompletionService<String> service) {
		super();
		this.name = name;
		this.service = service;
	}

	@Override
	public void run() {
		ReportGenerator reportGenerator = new ReportGenerator(name, "Report");//不断的生成报表
		service.submit(reportGenerator);//将报表生成器发送给CompletionService,以生成报表和产生结果
	}
	
}

class ReportProcessor implements Runnable{
	//在ReportQuest中提交来的生成报表工作完成后，由service从中报得结果
	private CompletionService<String> service;
	private boolean end;
	
	public ReportProcessor(CompletionService<String> service){
		this.service = service;
		end = false;
	}

	@Override
	public void run() {
		while(!end){
			try{
				//从Service各得到执行完任务的Future
				Future<String> result = service.poll(20,TimeUnit.SECONDS);
				if(result != null){
					String report = result.get();  //得到报表生成结果
					System.out.printf("ReportReceiver: Report Received: %s\n", report);
				}
			}catch(InterruptedException | ExecutionException e){
				e.printStackTrace();
			}
		}
		System.out.printf("ReportSender: End\n"	);
	}

	public void setEnd(boolean end) {
		this.end = end;
	}
	
}