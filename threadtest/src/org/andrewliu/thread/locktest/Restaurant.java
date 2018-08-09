package org.andrewliu.thread.locktest;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 生产者--消费者示例
 * @author de
 *餐窗
 */
public class Restaurant {

	ExecutorService exec = Executors.newCachedThreadPool();
	//食物
	Meal meal;
    //厨师
	Chef chef = new Chef(this);
	//服务员
	WaitPerson waitPerson= new WaitPerson(this);
	public Restaurant(){
		//餐窗中厨师与服务员在相互运作
		exec.execute(chef);
		exec.execute(waitPerson);
	}
	public static void main(String[] args) {
		new Restaurant();
	}
}

//食物
class Meal{
	//订单号
	private final int orderNum;
	public Meal(int orderNum){
		this.orderNum = orderNum;
	}
	public String toString(){
		return "Meal"+orderNum;
	}
}
//服务员
class WaitPerson implements Runnable{
	private Restaurant restaurant;
	//和餐窗关联
	public WaitPerson(Restaurant r){
		restaurant = r;
	}
	@Override
	public void run() {
		try{
			//看任务是否中断
			while(!Thread.interrupted()){
				synchronized(this){ //锁定自己（当前服务员)，一直查看是否上食物了，如果没有，则wait，释放锁
					while(restaurant.meal == null){
						wait();
					}
					System.out.println("WaitPerson got"+ restaurant.meal);
				}
				//否则有食物
				synchronized(restaurant.chef){
					//将食物拿走
						restaurant.meal = null;
					//通知厨师做食物
						restaurant.chef.notifyAll();
					}
				}
		}catch(InterruptedException e){
			System.out.println("WaitPerson interrupted!");
		}
	}
	
}

//厨师
class Chef implements Runnable{
	//关联餐窗
	private Restaurant restaurant;
	private int count = 0;
	public Chef(Restaurant r){
		restaurant = r;
	}
	@Override
	public void run() {
		try{
			//看线程是否中断
			while(!Thread.interrupted()){
				synchronized(this){//锁定自己
					while(restaurant.meal !=null)//如果餐窗上有食物，是wait
						wait();
				}
				if(++count==10){
					System.out.println("out of food.closing");
				}
				System.out.println("Order up!");
				synchronized(restaurant.waitPerson){  //销售服务员对象
					restaurant.meal = new Meal(count);  //做食物
					restaurant.waitPerson.notifyAll();//通知服务员
				}
				TimeUnit.MILLISECONDS.sleep(100);
			}
		}catch(InterruptedException e){
			System.out.println("Chef interrupted!");
		}
	}
	
}