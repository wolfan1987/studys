package org.andrewliu.thread.jdblib;

import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class ActiviteObjectDemo {

	private ExecutorService ex = Executors.newSingleThreadExecutor();//���߳�ִ�������������뵽�����е�����һ��ֻ�ܵ���һ��������ͬʱ����
	private Random rand = new Random(47);
	
	private void pause(int factor){
		try{
			TimeUnit.MILLISECONDS.sleep(100+rand.nextInt(factor));
		}catch(InterruptedException e){
			System.out.println("sleep() interrupted!");
		}
	}
	
	public Future<Integer> calculateInt(final int x,final int y ){  //��ҵ���Է�����Ϣ���������ã���ʽ����Callableִ�У�Ȼ����Future���ؽ��
		return ex.submit(new Callable<Integer>(){
			public Integer call(){
				System.out.println("Starting "+ x + " + "+ y);
				pause(500);
				return x+y;
			}
		});
	}
	
	public Future<Float> calculateFloat(final float x, final float y){
		return ex.submit(new Callable<Float>(){
			public Float call(){
				System.out.println("starting"+ x + "+ "+ y);
				pause(2000);
				return x+y;
			}
		});
	}
	
	public void shutdown(){
		ex.shutdown();
	}
	
	
	public static void main(String[] args) {
		ActiviteObjectDemo d1 = new ActiviteObjectDemo();
		List<Future<?>>  results = new CopyOnWriteArrayList<Future<?>>();  //���淢����Ϣִ�к�Ľ��
		for(float f = 0.0f; f< 1.0f; f+=0.2f){
			results.add(d1.calculateFloat(f, f));
		}
		for(int i = 0; i < 5; i++){
			results.add(d1.calculateInt(i, i));  //������d1.calculateʱ��ex�ǴӶ�����һ��һ����ִ�����񲢷��ؽ��,�����ǽ����������Ŷӣ�
			                                      	//ʹ����ʱ�̶�ֻ�ܷ���һ�����ã��Ӷ���ͬ����������Ϣ�����Ϸ�����
		}
		System.out.println(" All asynch calls made");
		
		while(results.size() > 0){
			for(Future<?> f : results){
				if(f.isDone()){ //������������
					try {
						System.out.println(f.get());  //��ȡ���
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
					results.remove(f); //��ȡ���ͽ���Futureɾ������
				}
			}
		}
		
		d1.shutdown();
	}
}
