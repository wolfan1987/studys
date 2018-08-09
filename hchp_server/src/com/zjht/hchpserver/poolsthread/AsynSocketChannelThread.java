package com.zjht.hchpserver.poolsthread;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.ReadPendingException;
import java.nio.charset.Charset;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import com.zjht.hchpserver.asynclient.AsynSocketClientHandler;
import com.zjht.hchpserver.config.AbstractConfig;


public class AsynSocketChannelThread  implements  Runnable{
	private static final Logger logger = Logger.getLogger(AsynSocketChannelThread.class.getName());
	private AsynchronousSocketChannel socketChannel;
	private static Charset utf8 = Charset.forName("utf-8");
	private  Queue<String>   cousumerDataQueue = new ConcurrentLinkedQueue<String>();
	private  HttpServletRequest request;
	private   Future<Void> connectFuture;
	public AsynSocketChannelThread(AsynchronousChannelGroup asyncChannelGroup,AbstractConfig configInfo){
		
		try {
			socketChannel  = AsynchronousSocketChannel.open(asyncChannelGroup);
			connectFuture = socketChannel.connect(new InetSocketAddress(configInfo
					.getServerIp(), configInfo.getListenerPort()));
			
			socketChannel.setOption(StandardSocketOptions.TCP_NODELAY,
				      true);
			socketChannel.setOption(StandardSocketOptions.SO_REUSEADDR,
				      true);  
			socketChannel.setOption(StandardSocketOptions.SO_KEEPALIVE,
				      true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		while(true){
			if(processRequest()){
				//logger.info(this.toString()+"---执行完毕！");
			}
			if(cousumerDataQueue.size()==0){
				try {
					TimeUnit.MILLISECONDS.sleep(400);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			
				 AsynSocketChannelThreadPool.getInstance().checkIn(this);
			}
		}
	}
	private boolean  processRequest(){
	
		synchronized (socketChannel) {
		try {     
			String s = cousumerDataQueue.poll();
			if(s!=null){
				Future<Integer> w = socketChannel.write(utf8.encode(s));
				w.get();  
				ByteBuffer buffer = ByteBuffer.allocate(1024);
				socketChannel.read(buffer, buffer, new AsynSocketClientHandler(this,socketChannel,buffer));
				
			}else{   
			//	logger.info("队列为空！等待生产!!!");
			}
		} catch (InterruptedException e) {
			e.printStackTrace();        
		} catch (ExecutionException e) {   
			e.printStackTrace();
		} catch (ReadPendingException e){    
			//e.printStackTrace();
			//logger.info("出错了！！socketChannel="+socketChannel);
		}
		}
		return true;
	}


	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public AsynchronousSocketChannel getSocketChannel() {
		return socketChannel;
	}

	public void setSocketChannel(AsynchronousSocketChannel socketChannel) {
		this.socketChannel = socketChannel;
	}

	public Queue<String> getCousumerDataQueue() {
		return cousumerDataQueue;
	}   

	public void setCousumerDataQueue(Queue<String> cousumerDataQueue) {
		this.cousumerDataQueue = cousumerDataQueue;
	}

	public int getQueueSize(){
		return this.cousumerDataQueue.size();
	}
}
