package com.zjht.hchpserver.asynclient;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.Charset;
import java.util.logging.Logger;

import com.zjht.hchpserver.poolsthread.AsynSocketChannelThread;
import com.zjht.hchpserver.poolsthread.AsynSocketChannelThreadPool;

public class AsynSocketClientHandler implements
		CompletionHandler<Integer, ByteBuffer> {
	private static final Logger logger = Logger.getLogger(AsynSocketClientHandler.class.getName());
	private static Charset utf8 = Charset.forName("utf-8");
	private AsynchronousSocketChannel channel;
	private AsynSocketChannelThread  thread;
	private ByteBuffer buffer;
	@Override
	public void completed(Integer result, ByteBuffer buff) {
		logger.info(channel+"result = "+result);
		CharBuffer cb = null;
		if (result > 0) {
			buffer.flip();
			cb = utf8.decode(buffer);
			logger.info("serverContent...="+cb);
			buffer.compact(); 
			 buffer.clear();  
			try{
			 channel.read(buff, buff, this);  
			}catch(Exception e){   
				//e.printStackTrace();
				//logger.info(channel+"result = "+result);
			}   
		} else if (result == -1) {
//			try {            
//				channel.close();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
			logger.info("通道无数据可读!"+channel);
		}
		  
		  
	}

	@Override
	public void failed(Throwable exc, ByteBuffer buff) {
		exc.printStackTrace();
	}

	public AsynSocketClientHandler(AsynSocketChannelThread thread,AsynchronousSocketChannel  socketChanne,
			ByteBuffer buffer) {
		super();
		this.thread = thread;
		this.buffer = buffer;
		this.channel = socketChanne;
		
	}

}
