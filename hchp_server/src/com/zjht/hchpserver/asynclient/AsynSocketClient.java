package com.zjht.hchpserver.asynclient;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.charset.Charset;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zjht.hchpserver.poolsconnect.AbstractAsynSocketPool;
import com.zjht.hchpserver.poolsthread.AbstractAsynSocketChannelThreadPool;
import com.zjht.hchpserver.poolsthread.AsynSocketChannelThread;
import com.zjht.hchpserver.poolsthread.AsynSocketChannelThreadPool;

public class AsynSocketClient  {
	private  static final Logger  logger = Logger.getLogger(AsynSocketClient.class.getName());
	private static Charset utf8 = Charset.forName("utf-8");
	private AbstractAsynSocketPool<AsynchronousSocketChannel>   connectorPool ;
	private  AbstractAsynSocketChannelThreadPool<AsynSocketChannelThread>  socketChannelPool;
	//private AsynSocketChannelThread socketChannelThread = null;
	
	public AsynSocketClient() {
		//connectorPool = AsynSocketConnectPool.getInstance();  
		//connectorPool.initPool(1);
		socketChannelPool = AsynSocketChannelThreadPool.getInstance();
		//初始化连接池，并启动线程消费客户发送的数据
		socketChannelPool.initPool(100);
		
	}
   
	
	public  void  process(HttpServletRequest request){
		String sessionId = System.currentTimeMillis()+""; 
				//request.getSession().getId();
		try {
		     AsynSocketChannelThread	socketChannelThread = socketChannelPool.checkOut();
			if(socketChannelThread!=null){
				//socketChannelThread.setRequest(request);
				socketChannelThread.getCousumerDataQueue().add("clientReqData: 这是客户端发来的数据!"+sessionId);
				//socketChannelThread.notify();      
				//AsynSocketChannelThreadPool.getInstance().checkIn(socketChannelThread);
				//socketChannelPool.checkIn(socketChannelThread);
				//socketChannelThread = null;    
			}
		}  catch (InterruptedException e) {
			e.printStackTrace();      
		}
	}    
	
	public void processRequest(HttpServletRequest request,
			HttpServletResponse response) {
		ReentrantLock  lock = new ReentrantLock();
		if(lock.tryLock()){
			lock.lock();
		}  
		try {
			
			AsynchronousSocketChannel socketChanne = null;
			//String sessionId = request.getSession().getId();
			//logger.info("sessionId="+sessionId);
			socketChanne =  connectorPool.checkOut();
			Future<Integer> w = socketChanne.write(utf8.encode("I'm from  client!"));
			ByteBuffer buffer = ByteBuffer.allocate(1024);
			//socketChanne.read(buffer, buffer, new AsynSocketClientHandler(socketChanne,buffer,request));
			
		}  catch (InterruptedException e) {
			e.printStackTrace();      
		}finally{   
			lock.unlock();
			
		}
	}
	
	
	public static void main(String[] args) {
		AsynSocketClient client =new AsynSocketClient();
		long startTime = System.currentTimeMillis();
		Thread t1 = new Thread(new RunClient(client));
		t1.start();
		Thread t2 = new Thread(new RunClient(client));
		t2.start();
		Thread t3 = new Thread(new RunClient(client));
		t3.start();
		Thread t4 = new Thread(new RunClient(client));
		t4.start();
		Thread t5 = new Thread(new RunClient(client));
		t5.start();
		Thread t6 = new Thread(new RunClient(client));
		t6.start();
		Thread t7 = new Thread(new RunClient(client));
		t7.start();
		Thread t8 = new Thread(new RunClient(client));
		t8.start();
		try {
			t1.join();    
			t2.join();
			t3.join();
			t4.join();
			t5.join();
			t6.join();
			t7.join();
			t8.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		System.out.println("1个请求，共花时间:"+(System.currentTimeMillis() - startTime));
		
		
		
		
	}

}

class  RunClient implements  Runnable{
	int i = 0;
	AsynSocketClient  client;
	public RunClient(AsynSocketClient client){
		this.client = client;
	}
	
	@Override
	public void run() {
		while(true){
			i++;    
			client.process(null);
			if(i>=5000){
				
				break;
			}
			
		}
	}
	
}
