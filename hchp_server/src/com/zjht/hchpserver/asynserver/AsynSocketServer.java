package com.zjht.hchpserver.asynserver;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.Charset;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zjht.asyniobiframework.handler.HandlerConstantData;
import com.zjht.hchpserver.config.AbstractConfig;
import com.zjht.hchpserver.config.ServerConfig;

public class AsynSocketServer {

	private AbstractConfig configInfo = new ServerConfig();
	protected static Charset utf8 = Charset.forName("utf-8");  
	private  static final  Logger logger = LoggerFactory.getLogger(AsynSocketServer.class);
	 private ByteBuffer buffer = ByteBuffer.allocate(HandlerConstantData.BUFFER_SIZE);
	 private ByteBuffer transBuffer = ByteBuffer.allocate(HandlerConstantData.BUFFER_SIZE);
	 private Lock  lock = new ReentrantLock();
	public AsynSocketServer() {
       
	}

	public void startServer() {

		ExecutorService executor = Executors.newFixedThreadPool(configInfo
				.getThreadPoolSize());   
		AsynchronousChannelGroup group;
		try {  
			group = AsynchronousChannelGroup.withCachedThreadPool(executor,
					configInfo.getInitialSize());
			AsynchronousServerSocketChannel listener = AsynchronousServerSocketChannel
					.open(group);
			listener.bind(new InetSocketAddress(configInfo.getServerIp(),configInfo.getListenerPort()),
					configInfo.getBacklog());
			listener.accept(   
					listener,
					new CompletionHandler<AsynchronousSocketChannel, AsynchronousServerSocketChannel>() {
						
						@Override    
						public void completed(      
								AsynchronousSocketChannel channel,
								AsynchronousServerSocketChannel listener) {
							listener.accept(listener, this);
							
								channel.read(         
										buffer,  
										buffer,  
										new AsynSocketServerHandler(channel));
								  
						}    
						@Override     
						public void failed(Throwable exc,
								AsynchronousServerSocketChannel listener) {
							exc.printStackTrace();
							try {        
								listener.close();  
							} catch (IOException e) {
								e.printStackTrace();
							} finally {     
								System.exit(-1);
							}
						}  
					});
		} catch (IOException e1) {
			e1.printStackTrace();
		}

	}
	
	
	
	 public static void main(String[] args) throws IOException {  
		 System.out.println("启动AsynSocketServer......开始....");
		 AsynSocketServer server = new AsynSocketServer();  
	        server.startServer();
	     System.out.println("启动AsynSocketServer......结束....");
	    }  

}
