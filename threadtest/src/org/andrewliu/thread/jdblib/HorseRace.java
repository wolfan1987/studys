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
	private List<Horse> horses = new ArrayList<Horse>();//������ƥ
	private ExecutorService exec = Executors.newCachedThreadPool();//ִ����������
	private CyclicBarrier barrier;//դ������
	public HorseRace(int nHorses,final int pause){
		//��ʼ��դ������,��ͣ����ʾ�켣
		barrier = new CyclicBarrier(nHorses,new Runnable(){  //������ƥ�����������ڲ���ʵ��դ��

			@Override
			public void run() {
				StringBuilder s = new StringBuilder(); //�����յ��߳���
				for(int i = 0; i < FINISH_LINE; i++){
					s.append("=");
				}
				System.out.println(s);
				
				for(Horse horse : horses){  //��ӡÿƥ��Ĺ켣
					System.out.println(horse.tracks());
				}
				for(Horse horse : horses){
					if(horse.getStrides() >= FINISH_LINE){//����ĳ����·���ߺ��յ���һ�������ʱ��ʾ����ʤ����
						System.out.println(horse + "won!");
						exec.shutdownNow();//�������߳�ȫ���ر�
						return ; 
					}
				}
				
				try{
					TimeUnit.MILLISECONDS.sleep(pause); //��û��������ʱ����ͣ200����
				}catch(InterruptedException e){
					System.out.println(" barrier-action sleep interrupted!");
				}
			}
			
		});
		
		for(int i = 0; i < nHorses; i++){
			Horse horse = new Horse(barrier);//��դ������ƥ����
			horses.add(horse);//������
			exec.execute(horse);//��ʼ��
		}
	}
	
	public static void main(String[] args) {
		int nHorses = 7;//7ƥ��
		int pause = 200;//��ͣ200����
		new HorseRace(nHorses,pause);  //��ʼ����
	}
}

/**
 * ����һ���ö��߳�ģ�������ܣ�����Ȼ��await()
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
					strides += rand.nextInt(3);   //����0-2֮�����
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