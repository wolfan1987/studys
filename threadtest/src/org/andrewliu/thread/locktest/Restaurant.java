package org.andrewliu.thread.locktest;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * ������--������ʾ��
 * @author de
 *�ʹ�
 */
public class Restaurant {

	ExecutorService exec = Executors.newCachedThreadPool();
	//ʳ��
	Meal meal;
    //��ʦ
	Chef chef = new Chef(this);
	//����Ա
	WaitPerson waitPerson= new WaitPerson(this);
	public Restaurant(){
		//�ʹ��г�ʦ�����Ա���໥����
		exec.execute(chef);
		exec.execute(waitPerson);
	}
	public static void main(String[] args) {
		new Restaurant();
	}
}

//ʳ��
class Meal{
	//������
	private final int orderNum;
	public Meal(int orderNum){
		this.orderNum = orderNum;
	}
	public String toString(){
		return "Meal"+orderNum;
	}
}
//����Ա
class WaitPerson implements Runnable{
	private Restaurant restaurant;
	//�Ͳʹ�����
	public WaitPerson(Restaurant r){
		restaurant = r;
	}
	@Override
	public void run() {
		try{
			//�������Ƿ��ж�
			while(!Thread.interrupted()){
				synchronized(this){ //�����Լ�����ǰ����Ա)��һֱ�鿴�Ƿ���ʳ���ˣ����û�У���wait���ͷ���
					while(restaurant.meal == null){
						wait();
					}
					System.out.println("WaitPerson got"+ restaurant.meal);
				}
				//������ʳ��
				synchronized(restaurant.chef){
					//��ʳ������
						restaurant.meal = null;
					//֪ͨ��ʦ��ʳ��
						restaurant.chef.notifyAll();
					}
				}
		}catch(InterruptedException e){
			System.out.println("WaitPerson interrupted!");
		}
	}
	
}

//��ʦ
class Chef implements Runnable{
	//�����ʹ�
	private Restaurant restaurant;
	private int count = 0;
	public Chef(Restaurant r){
		restaurant = r;
	}
	@Override
	public void run() {
		try{
			//���߳��Ƿ��ж�
			while(!Thread.interrupted()){
				synchronized(this){//�����Լ�
					while(restaurant.meal !=null)//����ʹ�����ʳ���wait
						wait();
				}
				if(++count==10){
					System.out.println("out of food.closing");
				}
				System.out.println("Order up!");
				synchronized(restaurant.waitPerson){  //���۷���Ա����
					restaurant.meal = new Meal(count);  //��ʳ��
					restaurant.waitPerson.notifyAll();//֪ͨ����Ա
				}
				TimeUnit.MILLISECONDS.sleep(100);
			}
		}catch(InterruptedException e){
			System.out.println("Chef interrupted!");
		}
	}
	
}