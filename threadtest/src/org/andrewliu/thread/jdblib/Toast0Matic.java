package org.andrewliu.thread.jdblib;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * 用BlockingQueue来实现：有一台机器具有三个任务：一个制作吐司，一个给吐司抺黄油，另一个在抺过黄油后再给吐司涂果酱。
 * @author de
 *
 */
public class Toast0Matic {

	public static void main(String[] args) throws InterruptedException {
		ToastQueue dryQueue = new ToastQueue(),butteredQueue= new ToastQueue(),finishedQueue = new ToastQueue();
		ExecutorService exec = Executors.newCachedThreadPool();
		exec.execute(new Toaster(dryQueue));//产吐司
		exec.execute(new Butterer(dryQueue,butteredQueue));//为生产出来的吐司擦黄油
		exec.execute(new Jammer(butteredQueue,finishedQueue));//为擦了黄油的吐司抺果酱
		exec.execute(new Eater(finishedQueue));//吃吐司
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
 * 吐司队列
 * @author de
 *
 */
class ToastQueue extends LinkedBlockingQueue<Toast>{}

/**
 * 制作吐司的线程
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
				toastQueue.put(t);//将要制作的吐司放入队列:
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
				Toast t = dryQueue.take();//将吐司从队列里拿出来
				t.butter();//沾黄油
				System.out.println(t);
				butteredQueue.put(t);//保存沾了黄油的吐司
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
				Toast t = butteredQueue.take();//从沾了黄油的队列中拿出吐司
				t.jam();//沾果酱
				System.out.println(t);
				finishedQueue.put(t);//保存沾了果酱的吐司
			}
		}catch(InterruptedException e){
			System.out.println("Jammer interrupted");
		}
		System.out.println("jammer off!");
	}
	
}

/**
 * 吃吐司对象
 * @author de
 *
 */
class  Eater implements Runnable{
	private ToastQueue finishedQueue;  //已完成制作的吐司
	private int counter=0;
	public Eater(ToastQueue finished){
		finishedQueue = finished;
	}
	
	public void run(){
		try{
			while(!Thread.interrupted()){
				Toast t = finishedQueue.take();//拿出吐司
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


