package org.andrewliu.java7thread.java7synchhelper;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;

/**
 * Phaser：并发阶段任务的运行
 * 它允许执行并发多阶段任务，当我们有并发任务并且需要分解成几步执行时，这种机制就非常适用.Phaser类机制
 * 是在每一步结束的位置对线程进行同步，当所有线程都完成了这一步，才允许执行下一步。
 * 其必须对Phaser类中参与同步操作的任务数进行初始化，不同的是，可以动态地增加或者减少任务数。
 * Phaser对象有两种状态：
 * 1、活跃态(Active)，当存在参与同步的线程的时候，Phaser就是活跃的，并在每个阶段结束的时候进行同步。
 * 2、线上态(Termination):当所有参与同步的线程都取消注册的时候，Phaser就处于这个状态。此时同步方法arriveAndAwaitAdvance()会立即
 * 返回，而且不会作任何同步操作。
 * 方法说明：
 * arrive(): 通知phaser对象一个参与者已经完成了当前阶段，但是不等待其它参与者都完成当前阶段。
 * awaitAdvance(int phase)：如果传入的阶段数与当前阶段一致，这个方法会将当前线程置于休眠，直到这个阶段的所有参与者都运行完成，
 * 如果传入的阶段参数与当前阶段不一致，此方法将立即返回 。
 * awaitAdvanceInerruptibly(int phaser):此方法与awaitAdvance(int phase)一样，但其在方法中休眠的线程中断，将抛出InterruptedException.
 * 将参与都注册到phaser中:
 * 1、register()，这个方法将一个新的参与者注册到Phaser中，这个新的参与者将被当成没有执行完本阶段的线程。
 * 2、bulkRegister(int Parties):这个方法将指定数目的参与者注册到Phaser中，所有这些新的参与者都将被当成没有执行完本阶段的线程。
 * Phaser中只有arriveAndDeregister()方法来减少注册者的数目。
 * 可以用forceTermination()方法来强制phaser进入线上状态。
 * @author de
 *
 */
public class PhaserSynch_3_6 {

	public static void main(String[] args) {
		 //phaser关联三个线程，每个线程将有3个阶段，当一个线程的某一阶段执行完成，它将
		//调用phaser的arriverAndAwaitAdvance()（继续下一阶段） 或 arriveAndDeregistter()(不进入下一阶段)方法来表明是否继续线程的下一个阶段
		//每当调用phaser的arriverAndAwaitAdvance()一次，phaser的计数将减1，表示当前已有一个线程完成了一个阶段。且当前线程将进入休眠阶段,等待
		//进入下一阶段时被唤醒.
		Phaser phaser = new Phaser(3);//3个阶段
		FileSearch system = new FileSearch("C:\\Windows","log",phaser); 
		FileSearch apps = new FileSearch("C:\\","log",phaser);
		FileSearch documents = new FileSearch("C:\\","log",phaser);
		Thread systemThread = new Thread(system,"System"); //构造控制system目录的线程
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
	//阶段同步控制器
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
					directoryProcess(list[i]);//递归下一个目录
				}else{
					fileProcess(list[i]);//处理文件
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
			//如果某线程在第二个等待阶段就不满足条件，将退出，则告诉phaser此线程将不进入下一个阶段，撤销此线程的注册
			phaser.arriveAndDeregister();  //通知Phaser对象当前线程已经结束这个阶段，并且将不再参与接下来的操作
			return false;
		}else{
			System.out.printf("%s: Phase %d: %d results.\n",Thread.currentThread().getName(),phaser.getPhase(),results.size());
			//第二个等待阶段
			phaser.arriveAndAwaitAdvance();//通知Phaser对象当前线程已经完成了当前阶段，需要被阻塞直到其它线程也都完成当前阶段
			return true;
		}
	}
	
	private void showInfo(){
		for ( int i = 0; i < results.size(); i++){
			File file = new File(results.get(i));
			System.out.printf("%s: %s\n", Thread.currentThread().getName(),file.getAbsolutePath());
		}
		//第三个等待阶段（等所有线程都打印完信息，再往下执行)
		phaser.arriveAndAwaitAdvance();///阻塞等待同一阶段的任务完成
	}


	@Override
	public void run() {
		//第一个等待阶段
		phaser.arriveAndAwaitAdvance();//开始阶段，控制phaser中的线程全部进入后再执行下面的方法。
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
		//将所有线程都撤销注册
		phaser.arriveAndDeregister();  //撤销线程的注册，并将当前线程的完成信息打印到控制台.
		System.out.printf("%s: Work completed.\n", Thread.currentThread().getName());
		
	}
	
}