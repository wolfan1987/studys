package org.andrewliu.java7thread.java7synchhelper;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;

/**
 * Phaser�������׶����������
 * ������ִ�в�����׶����񣬵������в�����������Ҫ�ֽ�ɼ���ִ��ʱ�����ֻ��ƾͷǳ�����.Phaser�����
 * ����ÿһ��������λ�ö��߳̽���ͬ�����������̶߳��������һ����������ִ����һ����
 * ������Phaser���в���ͬ�����������������г�ʼ������ͬ���ǣ����Զ�̬�����ӻ��߼�����������
 * Phaser����������״̬��
 * 1����Ծ̬(Active)�������ڲ���ͬ�����̵߳�ʱ��Phaser���ǻ�Ծ�ģ�����ÿ���׶ν�����ʱ�����ͬ����
 * 2������̬(Termination):�����в���ͬ�����̶߳�ȡ��ע���ʱ��Phaser�ʹ������״̬����ʱͬ������arriveAndAwaitAdvance()������
 * ���أ����Ҳ������κ�ͬ��������
 * ����˵����
 * arrive(): ֪ͨphaser����һ���������Ѿ�����˵�ǰ�׶Σ����ǲ��ȴ����������߶���ɵ�ǰ�׶Ρ�
 * awaitAdvance(int phase)���������Ľ׶����뵱ǰ�׶�һ�£���������Ὣ��ǰ�߳��������ߣ�ֱ������׶ε����в����߶�������ɣ�
 * �������Ľ׶β����뵱ǰ�׶β�һ�£��˷������������� ��
 * awaitAdvanceInerruptibly(int phaser):�˷�����awaitAdvance(int phase)һ���������ڷ��������ߵ��߳��жϣ����׳�InterruptedException.
 * �����붼ע�ᵽphaser��:
 * 1��register()�����������һ���µĲ�����ע�ᵽPhaser�У�����µĲ����߽�������û��ִ���걾�׶ε��̡߳�
 * 2��bulkRegister(int Parties):���������ָ����Ŀ�Ĳ�����ע�ᵽPhaser�У�������Щ�µĲ����߶���������û��ִ���걾�׶ε��̡߳�
 * Phaser��ֻ��arriveAndDeregister()����������ע���ߵ���Ŀ��
 * ������forceTermination()������ǿ��phaser��������״̬��
 * @author de
 *
 */
public class PhaserSynch_3_6 {

	public static void main(String[] args) {
		 //phaser���������̣߳�ÿ���߳̽���3���׶Σ���һ���̵߳�ĳһ�׶�ִ����ɣ�����
		//����phaser��arriverAndAwaitAdvance()��������һ�׶Σ� �� arriveAndDeregistter()(��������һ�׶�)�����������Ƿ�����̵߳���һ���׶�
		//ÿ������phaser��arriverAndAwaitAdvance()һ�Σ�phaser�ļ�������1����ʾ��ǰ����һ���߳������һ���׶Ρ��ҵ�ǰ�߳̽��������߽׶�,�ȴ�
		//������һ�׶�ʱ������.
		Phaser phaser = new Phaser(3);//3���׶�
		FileSearch system = new FileSearch("C:\\Windows","log",phaser); 
		FileSearch apps = new FileSearch("C:\\","log",phaser);
		FileSearch documents = new FileSearch("C:\\","log",phaser);
		Thread systemThread = new Thread(system,"System"); //�������systemĿ¼���߳�
		systemThread.start();
		Thread appsThread = new Thread(apps,"Apps");
		appsThread.start();
		Thread documentsThread = new Thread(documents,"Documents");
		documentsThread.start();
		
		try{
			systemThread.join();
			appsThread.join();
			documentsThread.join();
		}catch(InterruptedException e){
			e.printStackTrace();
		}
		
		System.out.printf("Terminated: "+phaser.isTerminated());
	}
	
}


class FileSearch implements Runnable{

	private String initPath;
	private String end;
	private List<String>  results;
	//�׶�ͬ��������
	private Phaser phaser;
	
	public FileSearch(String initPath, String end, 
			Phaser phaser) {
		this.initPath = initPath;
		this.end = end;
		this.phaser = phaser;
		results = new ArrayList<>();
	}


	private void directoryProcess(File file){
		File list[] = file.listFiles();
		if (list != null){
			for ( int i = 0; i < list.length; i++){
				if(list[i].isDirectory()){
					directoryProcess(list[i]);//�ݹ���һ��Ŀ¼
				}else{
					fileProcess(list[i]);//�����ļ�
				}
			}
		}
	}
	
	private void fileProcess(File file){
		if(file.getName().endsWith(end)){
			results.add(file.getAbsolutePath());
		}
	}
	
	private void filterResults(){
		List<String>  newResults = new ArrayList<>();
		long actualDate = new Date().getTime();
		
		for ( int i = 0; i < results.size(); i++){
			File file = new File(results.get(i));
			long fileDate = file.lastModified();
			if(actualDate - fileDate < TimeUnit.MILLISECONDS.convert(1,TimeUnit.DAYS)){
				newResults.add(results.get(i));
			}
		}
		results = newResults;
	}
	
	private boolean checkResults(){
		if(results.isEmpty()){
			System.out.printf("%s: Phase %d: o results.\n", Thread.currentThread().getName(),phaser.getPhase());
			System.out.printf("%s: Phase %d: End.\n", Thread.currentThread().getName(),phaser.getPhase());
			//���ĳ�߳��ڵڶ����ȴ��׶ξͲ��������������˳��������phaser���߳̽���������һ���׶Σ��������̵߳�ע��
			phaser.arriveAndDeregister();  //֪ͨPhaser����ǰ�߳��Ѿ���������׶Σ����ҽ����ٲ���������Ĳ���
			return false;
		}else{
			System.out.printf("%s: Phase %d: %d results.\n",Thread.currentThread().getName(),phaser.getPhase(),results.size());
			//�ڶ����ȴ��׶�
			phaser.arriveAndAwaitAdvance();//֪ͨPhaser����ǰ�߳��Ѿ�����˵�ǰ�׶Σ���Ҫ������ֱ�������߳�Ҳ����ɵ�ǰ�׶�
			return true;
		}
	}
	
	private void showInfo(){
		for ( int i = 0; i < results.size(); i++){
			File file = new File(results.get(i));
			System.out.printf("%s: %s\n", Thread.currentThread().getName(),file.getAbsolutePath());
		}
		//�������ȴ��׶Σ��������̶߳���ӡ����Ϣ��������ִ��)
		phaser.arriveAndAwaitAdvance();///�����ȴ�ͬһ�׶ε��������
	}


	@Override
	public void run() {
		//��һ���ȴ��׶�
		phaser.arriveAndAwaitAdvance();//��ʼ�׶Σ�����phaser�е��߳�ȫ���������ִ������ķ�����
		System.out.printf("%s: Starting.\n", Thread.currentThread().getName());
		File file = new File(initPath);
		if(file.isDirectory()){
			directoryProcess(file);
		}
		if(!checkResults()){
			return;
		}
		filterResults();
		if(!checkResults()){
			return;
		}
		
		showInfo();
		//�������̶߳�����ע��
		phaser.arriveAndDeregister();  //�����̵߳�ע�ᣬ������ǰ�̵߳������Ϣ��ӡ������̨.
		System.out.printf("%s: Work completed.\n", Thread.currentThread().getName());
		
	}
	
}