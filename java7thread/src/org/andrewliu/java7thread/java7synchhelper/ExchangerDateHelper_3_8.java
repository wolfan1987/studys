package org.andrewliu.java7thread.java7synchhelper;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Exchanger;

/**
 * Exchanger: �����ڲ�������֮�佻������
 * Exchange�������������߳�֮�䶨��ͬ���㡣�������̶߳��ǵ���ͬ����ʱ�����ǽ������ݽṹ����˵�һ���߳�
 * �����ݽṹ���뵽�ڶ����߳��У�ͬʱ�ڶ����̵߳����ݽṹ���뵽��һ���߳��С�
 * Exchanger����������---�����������龳�к����ã�����һ�����ݻ�������һ�����߶�����������ߣ�һ�����߶�����������ߡ�
 * Exchanger��ֻ��ͬ�������̣߳���������Ƶ�ֻ��һ�������ߺ������ߵ����⣬����ʹ��Exchange��.
 * @author de
 * һ��һ������---������ʵ��
 */
public class ExchangerDateHelper_3_8 {

	public static void main(String[] args) {
		List<String>  buffer1 = new ArrayList<>();  
		List<String>  buffer2 = new ArrayList<>();
		//ָ��Exchanger�Ľ������ݽ����:List<String>����
		Exchanger<List<String>> exchanger = new Exchanger<>();  //������
		
		ExProducer producer = new ExProducer(buffer1, exchanger);   //�����߽����������ݷŵ�buffer1�У�������������ͬһ��exhanger�������ݽ���
		ExConsumer consumer = new ExConsumer(buffer2, exchanger);  //�����߽����������õ�����������buffer2�У�������������ͬһ��exhanger�������ݽ���
		
		Thread threadProducer = new Thread(producer);   //�������������������̣߳��������ݽ���
		Thread threadConsumer = new Thread(consumer);
		threadProducer.start();
		threadConsumer.start();
		
	}
}

/**
 * �������ݣ�������
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
				buffer = exchanger.exchange(buffer);  //������ּ��������
			}catch(InterruptedException e){
				e.printStackTrace();
			}
			System.out.printf("Producer: "+buffer.size());
			cycle++;
		}
	}
}
/**
 * �������ݣ�������
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
				buffer = exchanger.exchange(buffer); //�������߽�������
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