package org.andrewliu.java7thread.java7synch;

import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Lock.ReetrantLock的Condition的使用
 * Condition中有:await()、signal(),signalAll()三个方法，对应于wait(),notify(),notifyAll();
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
	private String content[];  //内容
	private int index;//行号
	
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
	private LinkedList<String> buffer; //用来存放共享数据
	private int maxSize;//用来存放buffer的长度
	private ReentrantLock lock; //用来对修改buffer的代码块进行控制
	private Condition lines; //行条件
	private Condition space; //空间条件
	private boolean pendingLines; //用来表明缓冲区是否还有数据
	public Buffer( int maxSize) {
		buffer = new LinkedList<>();
		this.maxSize = maxSize;
		lock = new ReentrantLock();
		lines = lock.newCondition();
		space = lock.newCondition();
		pendingLines = true;
	}
	
	
	public void insert(String line){
		lock.lock();  //得到锁，开始临界区
		try{
			while ( buffer.size() == maxSize){  //当满了就等
				space.await();
			}
			buffer.offer(line); //否则就插入
			System.out.printf("%s: Inserted Line: %d\n", Thread.currentThread().getName(),buffer.size());
			lines.signalAll(); //提醒其它在等这个对象的线程也可以干活了
		}catch(InterruptedException e){
			e.printStackTrace();
		}finally{
			lock.unlock();  //释放临界区
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