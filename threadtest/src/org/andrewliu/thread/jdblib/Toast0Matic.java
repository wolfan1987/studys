package org.andrewliu.thread.jdblib;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * ��BlockingQueue��ʵ�֣���һ̨����������������һ��������˾��һ������˾�{���ͣ���һ���ڒ{�����ͺ��ٸ���˾Ϳ������
 * @author de
 *
 */
public class Toast0Matic {

	public static void main(String[] args) throws InterruptedException {
		ToastQueue dryQueue = new ToastQueue(),butteredQueue= new ToastQueue(),finishedQueue = new ToastQueue();
		ExecutorService exec = Executors.newCachedThreadPool();
		exec.execute(new Toaster(dryQueue));//����˾
		exec.execute(new Butterer(dryQueue,butteredQueue));//Ϊ������������˾������
		exec.execute(new Jammer(butteredQueue,finishedQueue));//Ϊ���˻��͵���˾�{����
		exec.execute(new Eater(finishedQueue));//����˾
		TimeUnit.SECONDS.sleep(5);
		exec.shutdownNow();
	}
	
}


class  Toast{
	public enum Status {DRY,BUTTERED,JAMMED}
	private Status status = Status.DRY;
	private final int id;
	public Toast(int idn) {
		id = idn;
	}
	public void butter(){
		status = Status.BUTTERED;
	}
	public void jam(){
		status = Status.JAMMED;
	}
	public Status getStatus(){
		return status;
	}
	public int getId(){
		return id;
	}
	public String toString(){
		return "Toast"+id+" :"+status;
	}
}

/**
 * ��˾����
 * @author de
 *
 */
class ToastQueue extends LinkedBlockingQueue<Toast>{}

/**
 * ������˾���߳�
 * @author de
 *
 */
class Toaster implements Runnable{
	private ToastQueue toastQueue;
	private int count = 0;
	private Random rand = new Random(47);
	public Toaster(ToastQueue tq){
		toastQueue = tq;
	}
	@Override
	public void run() {
		try{
			while(!Thread.interrupted()){
				TimeUnit.MILLISECONDS.sleep(100+rand.nextInt(500));
				Toast t = new Toast(count++);
				System.out.println(t);
				toastQueue.put(t);//��Ҫ��������˾�������:
			}
		}catch(InterruptedException e){
			System.out.println("Toaster interrupted!");
		}
		System.out.println("Toaster off!");
	}
	
}



class  Butterer implements Runnable{

	private ToastQueue dryQueue,butteredQueue;
	public Butterer(ToastQueue dry,ToastQueue buttered){
		dryQueue = dry;
		butteredQueue = buttered;
	}
	@Override
	public void run() {
		try{
			while(!Thread.interrupted()){
				Toast t = dryQueue.take();//����˾�Ӷ������ó���
				t.butter();//մ����
				System.out.println(t);
				butteredQueue.put(t);//����մ�˻��͵���˾
			}
		}catch (InterruptedException e){
			System.out.println("Butterer interrupted!");
		}
		System.out.println("Butterer off!");
	}
	
}

class Jammer implements Runnable{
	private ToastQueue butteredQueue,finishedQueue;
	public Jammer(ToastQueue buttered,ToastQueue finished){
		butteredQueue = buttered;
		finishedQueue = finished;
	}
	@Override
	public void run() {
		try{
			while(!Thread.interrupted()){
				Toast t = butteredQueue.take();//��մ�˻��͵Ķ������ó���˾
				t.jam();//մ����
				System.out.println(t);
				finishedQueue.put(t);//����մ�˹�������˾
			}
		}catch(InterruptedException e){
			System.out.println("Jammer interrupted");
		}
		System.out.println("jammer off!");
	}
	
}

/**
 * ����˾����
 * @author de
 *
 */
class  Eater implements Runnable{
	private ToastQueue finishedQueue;  //�������������˾
	private int counter=0;
	public Eater(ToastQueue finished){
		finishedQueue = finished;
	}
	
	public void run(){
		try{
			while(!Thread.interrupted()){
				Toast t = finishedQueue.take();//�ó���˾
				if(t.getId() != counter++ || t.getStatus()!=Toast.Status.JAMMED){
					System.out.println(">>>> Eror:"+t);
					System.exit(1);
				}else{
					System.out.println("Chomp!"+t);
				}
			}
		}catch(InterruptedException e){
			System.out.println("Eater interrupted!");
		}
		
		System.out.println("Eater off!");
	}
}


