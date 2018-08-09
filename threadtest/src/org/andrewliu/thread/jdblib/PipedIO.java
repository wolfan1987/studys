package org.andrewliu.thread.jdblib;

import java.io.IOException;
import java.io.PipedReader;
import java.io.PipedWriter;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class PipedIO {

	public static void main(String[] args) throws IOException, InterruptedException {
		Sender sender = new Sender();
		Receiver receiver = new Receiver(sender);
		ExecutorService exec = Executors.newCachedThreadPool();
		exec.execute(sender);  //sender���ϵ�д
		exec.execute(receiver);//receiver���ϵĶ�
		TimeUnit.SECONDS.sleep(4);
		exec.shutdownNow();
	}
}

/**
 * �����ߣ����ܵ���д��
 * @author de
 *
 */
class Sender implements Runnable{

	private Random rand = new Random(47);
	private PipedWriter out = new  PipedWriter();
	public PipedWriter getPipedWriter(){
		return out;
	}
	@Override
	public void run() {
		try{
			while(true){
				for(char c =  'A'; c <= 'z'; c++){  //��ͣ��д�����������sleep
					out.write(c);
					TimeUnit.MILLISECONDS.sleep(rand.nextInt(500));
				}
			}
		}catch(IOException e){
			System.out.println(e+"Sender writer exception!");
		}catch(InterruptedException e){
			System.out.println(e + "Sender sleep interrupted");
		}
	}
}

/**
 * �����ߣ���ȡ������
 * @author de
 *
 */
class Receiver implements Runnable{

	private PipedReader in ;
	public Receiver(Sender sender) throws IOException{
		in = new PipedReader(sender.getPipedWriter()); //�õ�������д�����
	}
	
	@Override
	public void run() {
		try{
		while(true){
			System.out.println("read:"+ (char)in.read()+ ",");
		}
		}catch(IOException e){
			System.out.println(e+ "  Receiver read exception!");
		}
	}
	
}