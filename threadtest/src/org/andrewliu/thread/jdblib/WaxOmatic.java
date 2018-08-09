package org.andrewliu.thread.jdblib;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * �������׹����
 * @author de
 *
 */
public class WaxOmatic  {

	public static void main(String[] args) throws InterruptedException {
		Car car = new Car();
		ExecutorService exec = Executors.newCachedThreadPool();
		exec.execute(new WaxOff(car));//�ô����߳���waxOn=falseʱ������,һֱ��while��ѭ���ж�waxOn�Ƿ�Ϊfalse����Ϊtureʱ�������ִ��
		exec.execute(new WaxOn(car));//�׹��߳̿�ʼ�׹�,�׹��꽫waxOn��Ϊtrue��ͬʱ���������̸߳ɻ���Լ����𣬵������̴߳����꣬��waxOn��Ϊfalse
		TimeUnit.SECONDS.sleep(5);
		exec.shutdownNow();
	}
}
class Car{
	private Lock lock = new ReentrantLock();
	private Condition condition = lock.newCondition();
	private boolean waxOn = false;
	public void waxed(){
		lock.lock();  //�õ���ǰ�������
		try{
			waxOn = true;  //waxOn=true��ʾ���Դ����ˣ�
			condition.signalAll();//Ȼ���Ѵ����߳�
		}finally{
			lock.unlock();
		}
	}
	
	public void buffed(){
		lock.lock();//�õ���ǰ�������
		try{
		waxOn = false;  //waxOn = false����ʾ������ϣ������׹���
		condition.signalAll();//�����׹��̼߳���ִ��
		}finally{
			lock.unlock();
		}
	}
	
	public void waitForWaxing() throws InterruptedException{
		lock.lock();   
		try{
			while(waxOn == false){  //��waxOn = falseʱ����ʱ���׹⣬�����߳�Ҫһֱ�ȴ���ֱ���׹��̻߳��Ѵ����̲߳���waxOn = true
				condition.await();
			}
		}finally{
			lock.unlock();
		}
	}
	public void waitForBuffing() throws InterruptedException{
		lock.lock();
		try{
			while(waxOn == true){  //��waxOn = trueʱ����ʱ�ڴ������׹��߳�Ҫһֱ�ȴ���ֱ�������̻߳����׹��̲߳���waxOn = false
				condition.await();
			}
		}finally{
			lock.unlock();
		}
	}
}

class WaxOn implements Runnable{
	private Car car;//����һ�����׹�ʹ���
	public WaxOn(Car c){
		car = c;
	}
	public void run(){
		try{
			while(!Thread.interrupted()){  //���߳�״̬�����ж�״̬
				System.out.println("Wax on !");  //��ʼ�׹�
				TimeUnit.MILLISECONDS.sleep(200);//�׹�200��������
				car.waxed();//����waxOn״̬Ϊtrue,�����Ѵ����߳�
				car.waitForBuffing();//�ȴ�waxOn=false,�ټ���ִ���߳�
			}
		}catch(InterruptedException e){
			System.out.println("Exiting via interrupt");
		}
		
		System.out.println("Ending Wax On task!");
	}
}

class WaxOff implements Runnable{
	private  Car car;
	public WaxOff(Car c ){
		car = c;
	}
	
	public void run(){
		try{
			while(!Thread.interrupted()){ //���̲߳����ж�״̬
				car.waitForWaxing();  //��Ϊ�����׹⣬����Ҫ�ȹ����Լ������׹���������waxOn����Ϊtrue��ʾ���Դ�����
				System.out.println("Wax Off!");//������ʼ
				TimeUnit.MILLISECONDS.sleep(200);//����ʱ��200��������
				car.buffed();//�����꣬����waxOn״̬Ϊfalse���������׹��̸߳ɻ�
			}
		}catch(InterruptedException e){
			System.out.println("Exiting via interrupt");
		}
		System.out.println(" Ending wax Off task!");
	}
}

