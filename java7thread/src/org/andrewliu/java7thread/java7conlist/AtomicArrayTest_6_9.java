package org.andrewliu.java7thread.java7conlist;

import java.util.concurrent.atomic.AtomicIntegerArray;


/**
 * ���Ҫͬ���������������ݹ���������⣬Ϊ���ṩ���ŵ����ܣ�Java ���������˱ȽϺͽ�������(Compare-and-Swap Operation)���������ʹ��
 * ���������޸ı�����ֵ��
 * 1��ȡ�ñ���ֵ����������ֵ��
 * 2����һ����ʱ�������޸ı���ֵ������������ֵ��
 * 3����������õı�����ֵ�뵱ǰ����ֵ��ȣ�������ֵ�滻��ֵ��������������߳��޸������ֵ�������õı����ľ�ֵ�Ϳ��ܲ���ǰ����ֵ��ͬ��
 * ���ñȽϺͽ������Ʋ���Ҫʹ��ͬ�����ƣ����Ա������������ܸ��á�
 * ��javaԭ�ӱ���(Atomic Variable)��ʵ�����⴦���ƣ���Щ�����ṩ��ʵ�ֱȽϺͽ���������compareAndSet()������
 *  
 * ԭ�����飨Atomic Array)��java�ṩ�˶�ԭ�����������ΪInteger �� long���������ԭ�Ӳ�����
 * AtomicIntegerArray����AtomicLongArray��ķ�����ͬ��
 * AtomicIntegerArray.getAndIncrement(i); //����i��Ԫ��ֵ��1
 * AtomicLongArray.getAndDecrement(i);//����i��Ԫ��ֵ��1 
 *����������
 * get(int i); ����������ͬ����ָ��λ�õ�ֵ��
 *set(int I,int newValue):�����ɲ���ָ��λ�õ���ֵ��
 * @author de
 * 
 */
public class AtomicArrayTest_6_9 {

	public static void main(String[] args) {
		final int THREADS = 100;
		AtomicIntegerArray vector = new AtomicIntegerArray(1000);
		Incrementer incrementer = new Incrementer(vector);
		Decrementer decrementer = new Decrementer(vector);
		
		Thread threadIncrementer[] = new Thread[THREADS];
		Thread threadDecrementer[] = new Thread[THREADS];
		
		for ( int i = 0 ; i < THREADS; i++){
			threadIncrementer[i] = new Thread(incrementer);
			threadDecrementer[i] = new Thread(decrementer);
			threadIncrementer[i].start();
			threadDecrementer[i].start();
		}
		
		for(int i = 0; i<100;i++){
			try{
				threadIncrementer[i].join();
				threadDecrementer[i].join();
			}catch(InterruptedException e){
				e.printStackTrace();
			}
		}
		
		for ( int i = 0; i < vector.length(); i++){
			if(vector.get(i)!=0){  //��get(i)������Ԫ��i��ֵ
				System.out.printf("Vector["+i+"] :"+vector.get(i));
			}
		
		}
		
		System.out.println("Main: end of the example!");
	}
	
}


class Incrementer implements Runnable{
	private AtomicIntegerArray vector;

	public Incrementer(AtomicIntegerArray vector) {
		super();
		this.vector = vector;
	}

	@Override
	public void run() {
		for ( int i = 0; i < vector.length();i++){
			vector.getAndIncrement(i);//����i��Ԫ�ؼ�1
		}
	}
}

class Decrementer implements Runnable{
	private AtomicIntegerArray vector;

	public Decrementer(AtomicIntegerArray vector) {
		super();
		this.vector = vector;
	}

	@Override
	public void run() {
		for ( int i = 0; i < vector.length(); i++){
			vector.getAndDecrement(i); //����i��Ԫ��ͷ1
		}
	}
}