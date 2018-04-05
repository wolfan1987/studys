package org.andrewliu.java7thread.java7forkjoin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.TimeUnit;

/**
 * ��Fork/Join��ܶ�ForkJoinTaskʱ��������ͬ�����첽��ʽ���У�������ͬ����ʽִ��ʱ�����������Fork/Join�̳߳صķ���
 * ֱ������ִ����ɺ�Ż᷵�ؽ�����������첽��ʽִ��ʱ�����������ִ�����ķ������������ؽ���������������ܹ�����ִ��.
 * 
 * ������ͬ��ʱ������ ��Щ����ʱ�����񱻹���ֱ�����񱻷��͵�Fork/Join�̳߳���ִ����ɡ����ַ�ʽ����ForkJoinPool����ȡ�㷨������һ��
 * ���������ִ����������Ĺ������̡߳���task���� join()��get()����ʱ��ForkJoinPool����ʹ�ù�����ȡ�㷨��
 * �������첽��ʽ����������һ���������fork()����)ʱ�����񽫼���ִ�У�ForkJoinPool���޷�ʹ�ù�����ȡ�㷨���������ܡ�
 * 
 * @author de
 *
 *�����ļ��л����ļ����µ��ļ���չ����ForkJoinTask�ദ���ļ����µ����ݣ����ļ����µ����ļ��У��������첽�ķ�ʽ��ʽ����һ���µ������ForkJoinPool��
 *����ÿ���ļ����е��ļ������񽫼�������ļ�����չ����������������ͽ������ӵ�����б�
 */
public class ForkJoinThreadPoolAnsychTask {

	public static void main(String[] args) {
		ForkJoinPool pool = new ForkJoinPool();
		//������Ŀ¼������
		FolderProcessor system = new FolderProcessor("C:\\Windows", "log");
		FolderProcessor  apps = new FolderProcessor("C:\\Program Files","log");
		FolderProcessor  documents = new FolderProcessor("C:\\Documents And Settings","log");
		//ִ������
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
		results = system.join();  //�õ����Ľ��
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
				if(content[i].isDirectory()){   //�����Ŀ¼��������һ�����񣬲����첽��ʽ����
					FolderProcessor task = new FolderProcessor(content[i].getAbsolutePath(), extension);
					task.fork();  //���������첽��ʽִ��,�����͵��̳߳�
					tasks.add(task);
				}else{  //�����ʾ�Ǹ��ļ��������Ҫ�ҵ��ļ���չ�����򽫽���ӵ�list��
					if(checkFile(content[i].getName())){
						list.add(content[i].getAbsolutePath());
					}
				}
			}
			
			if(tasks.size()>50){ 
				System.out.printf("%s: %d tasks ran.\n", file.getAbsolutePath(),tasks.size());
			}
			
			//��tasks�е�������Ϊͬ��ִ�У����õ�ִ�н��,������ı���list�Ľ��
			addResultsFromTasks(list,tasks);
		}
		
		return  list;
	}
	
	/***
	 * join()�������ȴ�����Ľ�����Ȼ���ȡ���ǵĽ����Ҳ����ʹ��get()�������µ������汾��������Ŀ�ġ�
	 * get():  ���ForkJoinTask��ִ�н���������һֱ�ȵ���������ôget()����������汾�򷵻���compute()�������صĽ��.
	 * get(long timeout,TimeUnit unit): �������Ľ��δ׼���ã���ôget()����������汾���ȴ�ָ����ʱ�䣬�������ָ����ʱ���ˡ�����Ľ����
	 *                                   δ׼���ã���ô�������������nullֵ.
	 *  get()������join()��������Ҫ����
	 *  join()�������ܱ��жϣ�����жϵ���join()�������̣߳��������׳�InterruptedException�쳣.
	 *  ��������׳��κ�����ʱ�쳣����ôget()����������ExecutionException�쳣,��join()����������RuntimeException�쳣.                                 
	 * @param list
	 * @param tasks
	 */
	private void addResultsFromTasks(List<String> list,List<FolderProcessor> tasks){
		for ( FolderProcessor item : tasks){
			list.addAll(item.join());  //item.join(),�ȴ�ִ����ɺ�ͨ��compute()�������ؽ��
		}
	}
	
	private boolean checkFile(String name){
		return name.endsWith(extension);
	}
	
}