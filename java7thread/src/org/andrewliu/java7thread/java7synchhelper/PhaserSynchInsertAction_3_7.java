package org.andrewliu.java7thread.java7synchhelper;

import java.util.Date;
import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;

/**
 * Phaser: �׶�ͬ�����м����׶�(�׶��л���
 * onAdvance()��������phaser�׶θı�Ľ��޻ᱻ�Զ�ִ�С�����Ҫ����int�Ͳ�����
 * ��ǰ�׶����Լ�ע��Ĳ����������������ص���booleanֵ��������ص���false��ʾphaser�ڼ���ִ�У�����true��ʾphaser�Ѿ����ִ�в���������ֹ̬��
 * onAdvance()����Ĭ��ʵ���ǣ����ע��Ĳ�����������0�ͷ���true������ͷ���false.����ͨ������Phaser�า�Ǵ˷�������ʵ�ִ�һ���׶ε���һ���׶ι���
 * ��ʱ��ִ��һЩ����������׶Σ�
 * @author de
 *
 */
public class PhaserSynchInsertAction_3_7 {

	public static void main(String[] args) {
		MyPhaser phaser = new MyPhaser();
		Student students[] = new Student[5];
		for ( int i = 0; i < students.length; i++){
			students[i] = new Student(phaser);//��ѧ����׶ι���
			phaser.register(); //������ѧ����׶�ע��
		}
		
		Thread threads[] = new Thread[students.length];
		for ( int i = 0; i < students.length; i++){   //����ѧ�����������߳̿�ʼ����
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
		case 0:  //���ǽ׶�0ʱ����ʱ��ʾѧ������
			return studentsArrived();
		case 1: //�׶�1ʱ����ɵ�һ���ֿ���
			return finishFirstExercise();
		case 2: //�׶�2�أ���ɵڶ����ֿ���
			return finishSecondExercise();
		case 3: //�׶�3�أ���������
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
		phaser.arriveAndAwaitAdvance();  //������ѧ������׶�0
		System.out.printf("%s: Is going to do the first exercise. %s\n", Thread.currentThread().getName(),new Date());
		doExercise1();  //����һ������
		System.out.printf("%s: Has done the first exercise. %s\n", Thread.currentThread().getName(),new Date());
		phaser.arriveAndAwaitAdvance(); //������ѧ�������꣬����׶�1
		
		System.out.printf("%s: Is going to do the second exercise. %s\n", Thread.currentThread().getName(),new Date());
		doExercise2();  //���ڶ�������
		System.out.printf("%s: Has done the second exercise. %s\n", Thread.currentThread().getName(),new Date());
		phaser.arriveAndAwaitAdvance(); //������ѧ��������,����׶�2
		
		
		System.out.printf("%s: Is going to do the third exercise. %s\n", Thread.currentThread().getName(),new Date());
		doExercise3();  //������������,Ȼ����ɿ��ԣ�
		System.out.printf("%s: Has finished the exam. %s\n", Thread.currentThread().getName(),new Date());
		phaser.arriveAndAwaitAdvance(); //������ѧ��������,����׶�3
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
