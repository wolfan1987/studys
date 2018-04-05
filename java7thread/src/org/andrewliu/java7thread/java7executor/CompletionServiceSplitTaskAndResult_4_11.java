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
 * ͨ��CompletionService�࣬ʵ�ַ�������ִ�����ͽ����ȡ�ֿ�����
 * CompletionService����һ�������������������ִ����������һ������Ϊ��һ���Ѿ�ִ�н����������ȡFuture����.
 * ��Ҳ��ͨ��Executor��ִ����������Թ���CompletionService���󣬲���������ִ������Ȼ������������Դ�������Ľ��,
 * ����ֻ�ܻ�ȡ��ִ�н����������Future����,����Щ����ֻ�ܱ�������ȡ����Ľ����
 * CompletionService�ҿ���ִ��Callable��Runnable���͵����񣬵�Runnable�����ܲ��������
 * �������ַ�������ȡ�����Ѿ���ɵ�Future����
 * poll():�޲�����poll()�������ڼ��������Ƿ���Future������Ϊ�գ��򷵻� null�����򷵻ض����е�һ��Ԫ�أ����Ƴ���Ԫ��.
 * take():�˷���Ҳ�޲�����ͬ�����������Ƿ���Future����������Ϊ�գ����������߳�ֱ���������п��õ�Ԫ�ء������������Ԫ�أ�
 *       �������� �����еĵ�һ��Ԫ�أ����Ƴ����Ԫ�ء�
 * @author de
 *
 */
public class CompletionServiceSplitTaskAndResult_4_11 {

	public static void main(String[] args) {
		ExecutorService executor = Executors.newCachedThreadPool();
		//����һ����ɷ��񣬲���ִ�����������
		CompletionService<String>  service = new ExecutorCompletionService<>(executor);
		//�������ɱ���ͨ��service������������(ReportGenerator)����ReportProcess,����ReportProcess��ȡ���ɽ��
		ReportRequest faceRequest = new ReportRequest("Face", service);
		ReportRequest onlineReqquest = new ReportRequest("Online",service);
		Thread faceThread = new Thread(faceRequest);
		Thread onlineThread = new Thread(onlineReqquest);
		
		//�������߳�
		ReportProcessor processor = new ReportProcessor(service);
		Thread senderThread = new Thread(processor);
		
		System.out.printf("Main: Starting the Threads\n");
		faceThread.start();
		onlineThread.start();
		senderThread.start();
		try{
			System.out.printf("Main: Waiting for the report generators.\n");
			//���������ɱ�����������
			faceThread.join();
			onlineThread.join();
		} catch (InterruptedException e){
			e.printStackTrace();
		}
		
		System.out.printf("Main: Shutting down the executor.\n");
		executor.shutdown();
		try{
			//�ȴ���������ִ����
			executor.awaitTermination(1,TimeUnit.DAYS);
		}catch(InterruptedException e){
			e.printStackTrace();
		}
		//��ʾReportProcess���Ի�ȡFuture�еĽ����
		processor.setEnd(true);
		System.out.println("Main: Ends");
		
	}
}

/**
 * ����������
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
 * ��װ�ͷ������ɱ���������
 * @author de
 *
 */
class ReportRequest implements Runnable{
	private String name;
	//��������������service������ִ�б�����������call������
	private CompletionService<String> service;
	
	public ReportRequest(String name, CompletionService<String> service) {
		super();
		this.name = name;
		this.service = service;
	}

	@Override
	public void run() {
		ReportGenerator reportGenerator = new ReportGenerator(name, "Report");//���ϵ����ɱ���
		service.submit(reportGenerator);//���������������͸�CompletionService,�����ɱ���Ͳ������
	}
	
}

class ReportProcessor implements Runnable{
	//��ReportQuest���ύ�������ɱ�������ɺ���service���б��ý��
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
				//��Service���õ�ִ���������Future
				Future<String> result = service.poll(20,TimeUnit.SECONDS);
				if(result != null){
					String report = result.get();  //�õ��������ɽ��
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