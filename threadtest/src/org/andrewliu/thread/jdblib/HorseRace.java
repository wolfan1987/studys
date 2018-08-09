package org.andrewliu.thread.jdblib;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class HorseRace {

	static final int FINISH_LINE = 75;
	private List<Horse> horses = new ArrayList<Horse>();//保存马匹
	private ExecutorService exec = Executors.newCachedThreadPool();//执行赛马任务
	private CyclicBarrier barrier;//栅栏对象
	public HorseRace(int nHorses,final int pause){
		//初始化栅栏对象,不停的显示轨迹
		barrier = new CyclicBarrier(nHorses,new Runnable(){  //传入马匹数，用匿名内部类实现栅栏

			@Override
			public void run() {
				StringBuilder s = new StringBuilder(); //构造终点线长度
				for(int i = 0; i < FINISH_LINE; i++){
					s.append("=");
				}
				System.out.println(s);
				
				for(Horse horse : horses){  //打印每匹马的轨迹
					System.out.println(horse.tracks());
				}
				for(Horse horse : horses){
					if(horse.getStrides() >= FINISH_LINE){//当有某区马路的线和终点线一样或更长时表示此马胜利。
						System.out.println(horse + "won!");
						exec.shutdownNow();//将所有线程全部关闭
						return ; 
					}
				}
				
				try{
					TimeUnit.MILLISECONDS.sleep(pause); //当没有马跑完时，暂停200毫秒
				}catch(InterruptedException e){
					System.out.println(" barrier-action sleep interrupted!");
				}
			}
			
		});
		
		for(int i = 0; i < nHorses; i++){
			Horse horse = new Horse(barrier);//将栅栏和马匹关联
			horses.add(horse);//加入马
			exec.execute(horse);//开始跑
		}
	}
	
	public static void main(String[] args) {
		int nHorses = 7;//7匹马
		int pause = 200;//暂停200毫秒
		new HorseRace(nHorses,pause);  //开始赛马
	}
}

/**
 * 定义一马，用多线程模拟其在跑，跑完然后await()
 * @author de
 *
 */
class Horse implements Runnable{
    private  static int counter = 0;
    private final int id = counter++;
    private int strides = 0;
    private static Random rand = new Random(47);
    private static CyclicBarrier barrier;
    public Horse(CyclicBarrier b){
    	barrier = b;
    }
    public synchronized int getStrides(){
    	return strides;
    }
	@Override
	public void run() {
		try{
			while(!Thread.interrupted()){
				synchronized(this){
					strides += rand.nextInt(3);   //产生0-2之间的数
				} 	
				System.out.println("i'm waiting....."+id);
				barrier.await();  //
			}
		}catch(InterruptedException e){
			System.out.println(e.getMessage());
		}catch(BrokenBarrierException e){
			System.out.println(e.getMessage());
			throw new RuntimeException(e);
		}
	}
	
	public String toString(){
		return "Horse" + id +" ";
	}
	
	public String tracks(){
	   StringBuilder s = new StringBuilder();
	   for(int i = 0; i < getStrides(); i++){
		   s.append("*");
	   }
	   s.append(id);
	   return s.toString();
	}
}