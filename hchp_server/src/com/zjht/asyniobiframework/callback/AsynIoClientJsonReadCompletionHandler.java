package com.zjht.asyniobiframework.callback;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Logger;

public class AsynIoClientJsonReadCompletionHandler implements
		CompletionHandler<Integer, ByteBuffer> {
	private CountDownLatch latch;
	public AsynIoClientJsonReadCompletionHandler(
			AsynchronousSocketChannel socketChannel,CountDownLatch latch) {
		super();
		this.socketChannel = socketChannel;
		this.latch = latch;
	}
	private AsynchronousSocketChannel socketChannel;
	private static Charset utf8 = Charset.forName("utf-8");
	private final CharsetDecoder decoder = Charset.forName("GBK").newDecoder();
	private static final Logger logger = Logger
			.getLogger(AsynIoClientJsonReadCompletionHandler.class.getName());
	private  ByteBuffer readBuffer = ByteBuffer.allocate(128);

	@Override
	public void completed(Integer result, ByteBuffer buffer) {
		
		if (result > 0) {
			buffer.flip();  
//             try {    
			     int len = buffer.getInt(0);
			     if(len==1024){
			    	 System.out.println("服务端说收取数据成功！");
			     }else{
			    	 System.out.println("服务端说收到的数据有问题！-----------");
			     }
			   //  System.out.println("len = "+len);
               //  System.out.println("收到的消息:" + utf8.decode(buffer));  
                 buffer.compact();   
                 
               // System.out.println("callback.getborrowobjectjson="+callback.getBorrowObjectJson());
                // callback.getIoThreadPool().returnObject(callback.getBorrowObjectJson());
                // socketChannel.read(buffer, buffer, this);
//             } catch (CharacterCodingException e) {  
//                 e.printStackTrace();  
//             } catch (IOException e) {  
//                 e.printStackTrace();  
//             }  
		} else if (result == 0 || result == -1) {
			 try {   
				 System.out.println("与服务器连接断线:" + socketChannel.getRemoteAddress());
				socketChannel.close();
				   
			} catch (IOException e) {
				e.printStackTrace();
			}  
			logger.info("通道无数据可读!" + socketChannel);
		}
		
		 latch.countDown();

	}

	@Override
	public void failed(Throwable exc, ByteBuffer attachment) {
		 System.out.println(exc);  
         cancelled(attachment);
	}

	
	 public void cancelled(ByteBuffer attachment) {  
         System.out.println("cancelled");  
     }  
}
