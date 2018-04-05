package org.andrewliu.java7thread.java7synch;

import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Lock.ReetrantLock��Condition��ʹ��
 * Condition����:await()��signal(),signalAll()������������Ӧ��wait(),notify(),notifyAll();
 * 
 * @author de
 *
 */
public class ProducerConsumerByLockCondition_2_7 {

	public static void main(String[] args) {
		FileMock mock = new FileMock(100,10);
		Buffer buffer = new Buffer(20);
		
		ProducerCondition  producer = new ProducerCondition(mock, buffer);
		
		Thread  threadProducer = new Thread(producer,"Producer");
		
		ConsumerCondition  consumers[] = new ConsumerCondition[3];
		Thread threadConsumers[] = new Thread[3];
		for( int i = 0; i < 3; i++){
			consumers[i] = new ConsumerCondition(buffer);
			threadConsumers[i] = new Thread(consumers[i],"Consumer"+i);
		}
		
		threadProducer.start();
		for (int i = 0; i < 3; i++){
			threadConsumers[i].start();
		}
	}
}

class FileMock{
	private String content[];  //����
	private int index;//�к�
	
	public FileMock(int size,int length){
		content = new String[size];
		for ( int i = 0; i < size; i++){
			StringBuilder buffer = new StringBuilder(length);
			for(int j = 0; j < length; j++){
				int indice = (int)Math.random()*255;
				buffer.append((char)indice);
			}
			content[i] = buffer.toString();
		}
		index=0;
	}
	
	public boolean hasMoreLines(){
		return index<content.length;
	}
	
	public String getLine(){
		if(this.hasMoreLines()){
			System.out.println("Mock:"+(content.length-index));
			return content[index++];
		}
		return null;
	}
	
}

class Buffer{
	private LinkedList<String> buffer; //������Ź�������
	private int maxSize;//�������buffer�ĳ���
	private ReentrantLock lock; //�������޸�buffer�Ĵ������п���
	private Condition lines; //������
	private Condition space; //�ռ�����
	private boolean pendingLines; //���������������Ƿ�������
	public Buffer( int maxSize) {
		buffer = new LinkedList<>();
		this.maxSize = maxSize;
		lock = new ReentrantLock();
		lines = lock.newCondition();
		space = lock.newCondition();
		pendingLines = true;
	}
	
	
	public void insert(String line){
		lock.lock();  //�õ�������ʼ�ٽ���
		try{
			while ( buffer.size() == maxSize){  //�����˾͵�
				space.await();
			}
			buffer.offer(line); //����Ͳ���
			System.out.printf("%s: Inserted Line: %d\n", Thread.currentThread().getName(),buffer.size());
			lines.signalAll(); //���������ڵ����������߳�Ҳ���Ըɻ���
		}catch(InterruptedException e){
			e.printStackTrace();
		}finally{
			lock.unlock();  //�ͷ��ٽ���
		}
	}
	
	
	
	public String get(){
		String line = null;
		lock.lock();
		try{
			while((buffer.size()==0) && (hasPendingLines())){
				lines.await();
			}
			if(hasPendingLines()){
				line = buffer.poll();
				System.out.printf("%s: Line Readed: %d\n",Thread.currentThread().getName(),buffer.size());
				space.signalAll();
			}
		}catch(InterruptedException e){
			e.printStackTrace();
		}finally{
			lock.unlock();
		}
		
		return line;
	}
	
	public void setPendingLines(boolean pendingLines){
		this.pendingLines = pendingLines;
	}
	
	public boolean hasPendingLines(){
		return pendingLines || buffer.size()>0;
	}
}

class ProducerCondition implements Runnable{
	private FileMock mock;
	private Buffer buffer;
	
	
	public ProducerCondition(FileMock mock, Buffer buffer) {
		this.mock = mock;
		this.buffer = buffer;
	}



	@Override
	public void run() {
		buffer.setPendingLines(true);
		while(mock.hasMoreLines()){
			String line = mock.getLine();
			buffer.insert(line);
		}
		buffer.setPendingLines(false);
	}
	
}

class ConsumerCondition implements Runnable{
	private Buffer buffer;

	public ConsumerCondition(Buffer buffer) {
		this.buffer = buffer;
	}

	@Override
	public void run() {
		while(buffer.hasPendingLines()){
			String  line = buffer.get();
			processLine(line);
		}
	}
	
	private void processLine(String line){
		try{
			Random random = new Random();
			Thread.sleep(random.nextInt(100));
		}catch(InterruptedException e){
			e.printStackTrace();
		}
	}
	
}