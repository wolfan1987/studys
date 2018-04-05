package org.andrewliu.java7thread.java7synchhelper;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Exchanger;

/**
 * Exchanger: 允许在并发任务之间交换数据
 * Exchange类允许在两个线程之间定义同步点。当两个线程都是到达同步点时，它们交换数据结构，因此第一个线程
 * 的数据结构进入到第二个线程中，同时第二个线程的数据结构进入到第一个线程中。
 * Exchanger类在生产者---消费者问题情境中很有用：包含一个数据缓冲区，一个或者多个数据生产者，一个或者多个数据消费者。
 * Exchanger类只能同步两个线程，如果有类似的只有一个生产者和消费者的问题，可以使用Exchange类.
 * @author de
 * 一对一生产者---消费者实现
 */
public class ExchangerDateHelper_3_8 {

	public static void main(String[] args) {
		List<String>  buffer1 = new ArrayList<>();  
		List<String>  buffer2 = new ArrayList<>();
		//指定Exchanger的交换数据结果是:List<String>类型
		Exchanger<List<String>> exchanger = new Exchanger<>();  //交换器
		
		ExProducer producer = new ExProducer(buffer1, exchanger);   //生产者将生产的数据放到buffer1中，并和消费者用同一个exhanger进行数据交换
		ExConsumer consumer = new ExConsumer(buffer2, exchanger);  //消费者将从生产者拿到的数据主到buffer2中，并和生产者用同一个exhanger进行数据交换
		
		Thread threadProducer = new Thread(producer);   //启动生产者与消费者线程，进行数据交换
		Thread threadConsumer = new Thread(consumer);
		threadProducer.start();
		threadConsumer.start();
		
	}
}

/**
 * 交换数据：生产者
 * @author de
 *
 */
class ExProducer implements Runnable{
	private List<String>  buffer;
	private final Exchanger<List<String>> exchanger;
	public ExProducer(List<String> buffer,Exchanger<List<String>> exchanger){
		this.buffer = buffer;
		this.exchanger = exchanger;
	}
	@Override
	public void run() {
		int cycle = 1;
		for ( int i = 0; i < 10; i++){
			System.out.printf("Producer: Cycle %d\n",cycle);
			for(int j = 0; j < 10; j++){
				String message = "Event "+((i*10)+j);
				System.out.printf("Producer: %s\n", message);
				buffer.add(message);
			}
			
			try{
				buffer = exchanger.exchange(buffer);  //和消费旨交换数据
			}catch(InterruptedException e){
				e.printStackTrace();
			}
			System.out.printf("Producer: "+buffer.size());
			cycle++;
		}
	}
}
/**
 * 交换数据：消费者
 * @author de
 *
 */
class ExConsumer implements Runnable{
	private List<String>  buffer;
	private final Exchanger<List<String>>	exchanger;
	public ExConsumer(List<String> buffer,Exchanger<List<String>> exchanger){
		this.buffer = buffer;
		this.exchanger = exchanger;
	}
	
	
	@Override
	public void run() {
		int cycle = 1;
		for ( int i = 0; i < 10; i++){
			System.out.printf("Consumer: Cycle %d\n", cycle);
			try{
				buffer = exchanger.exchange(buffer); //和生产者交换数据
			}catch(InterruptedException e){
				e.printStackTrace();
			}
			
			System.out.println("Consumer: "+buffer.size());
			for ( int j = 0; j < 10; j++){
				String message = buffer.get(0);
				System.out.println("Consumer: "+message);
				buffer.remove(0);
			}
			cycle++;
			
		}
	}
}