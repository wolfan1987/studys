package org.andrewliu.java7thread.java7synchhelper;

import java.util.Date;
import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;

/**
 * Phaser: 阶段同步的中间插入阶段(阶段切换）
 * onAdvance()方法：在phaser阶段改变的进修会被自动执行。其需要两个int型参数：
 * 当前阶段数以及注册的参与者数量。它返回的是boolean值，如果返回的是false表示phaser在继续执行，返回true表示phaser已经完成执行并进入了终止态。
 * onAdvance()方法默认实现是：如果注册的参与者数量是0就返回true，否则就返回false.可以通过继续Phaser类覆盖此方法，来实现从一个阶段到另一个阶段过渡
 * 的时候执行一些操作（插入阶段）
 * @author de
 *
 */
public class PhaserSynchInsertAction_3_7 {

	public static void main(String[] args) {
		MyPhaser phaser = new MyPhaser();
		Student students[] = new Student[5];
		for ( int i = 0; i < students.length; i++){
			students[i] = new Student(phaser);//将学生与阶段关联
			phaser.register(); //将所有学生与阶段注册
		}
		
		Thread threads[] = new Thread[students.length];
		for ( int i = 0; i < students.length; i++){   //创建学生，并启动线程开始考试
			threads[i] = new Thread(students[i],"Student"+i);
			threads[i].start();
		}
		for( int i = 0; i < threads.length; i++){
			try{
				threads[i].join();
			}catch(InterruptedException e){
				e.printStackTrace();
			}
		}
		
		System.out.printf("Main: Thre phaser has finished: %s.\n", phaser.isTerminated());
	}
	
}

class MyPhaser extends Phaser{
	
	@Override
	protected boolean onAdvance(int phase,int registeredParties){
		switch(phase){
		case 0:  //当是阶段0时，这时表示学生到达
			return studentsArrived();
		case 1: //阶段1时，完成第一部分考试
			return finishFirstExercise();
		case 2: //阶段2地，完成第二部分考试
			return finishSecondExercise();
		case 3: //阶段3地，结束考试
			return finishExam();
			default:
				return true;
		}
	}
	
	private boolean studentsArrived(){
		System.out.printf("Phase : The exam are going to start. The students are ready.\n");
		System.out.printf("Phaser: We have %d students.\n",getRegisteredParties());
		return false;
	}
	
	
	private boolean finishFirstExercise(){
		System.out.printf("Phaser: All the students have finished the first exercise.\n");
		System.out.printf("Phaser: It's time for the second one.\n");
		return false;
	}
	
	private boolean finishSecondExercise(){
		System.out.printf("Phaser: All the students have finished the second exercise.\n");
		System.out.printf("Phaser: It's time for the third one.\n");
		return false;
	}
	
	private boolean  finishExam(){
		System.out.printf("Phaser: All the students have finished the exam.\n");
		System.out.printf("Phaser: Theank you for you time.\n");
		return true;
	}
	
}

class Student implements Runnable{
	private Phaser phaser;
	public Student(Phaser phaser){
		this.phaser = phaser;
	}
	@Override
	public void run() {
		System.out.printf("%s: Has arrived to do the exam.%s\n",Thread.currentThread().getName(),new Date());
		phaser.arriveAndAwaitAdvance();  //等所有学生到达阶段0
		System.out.printf("%s: Is going to do the first exercise. %s\n", Thread.currentThread().getName(),new Date());
		doExercise1();  //做第一部分题
		System.out.printf("%s: Has done the first exercise. %s\n", Thread.currentThread().getName(),new Date());
		phaser.arriveAndAwaitAdvance(); //等所有学生都做完，到达阶段1
		
		System.out.printf("%s: Is going to do the second exercise. %s\n", Thread.currentThread().getName(),new Date());
		doExercise2();  //做第二部分题
		System.out.printf("%s: Has done the second exercise. %s\n", Thread.currentThread().getName(),new Date());
		phaser.arriveAndAwaitAdvance(); //等所有学生都做完,到达阶段2
		
		
		System.out.printf("%s: Is going to do the third exercise. %s\n", Thread.currentThread().getName(),new Date());
		doExercise3();  //做第三部分题,然后完成考试，
		System.out.printf("%s: Has finished the exam. %s\n", Thread.currentThread().getName(),new Date());
		phaser.arriveAndAwaitAdvance(); //等所有学生都做完,到达阶段3
	}
	
	
	
	private void doExercise1(){
		try {
			long duration = (long)(Math.random()*10);
			TimeUnit.SECONDS.sleep(duration);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private void doExercise2(){
		try {
			long duration = (long)(Math.random()*10);
			TimeUnit.SECONDS.sleep(duration);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private void doExercise3(){
		try {
			long duration = (long)(Math.random()*10);
			TimeUnit.SECONDS.sleep(duration);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
}
