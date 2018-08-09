package com.zjht.hchpserver.example;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.Charset;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class AsynEchoServer {

	 private int port = 9999;  
	  
	    private int backlog = 50;  
	  
	    private int threadPoolSize = 20;  
	  
	    private int initialSize = 5;  
	  
	    public void start() throws IOException {  
	        ExecutorService executor = Executors.newFixedThreadPool(threadPoolSize);  
	        AsynchronousChannelGroup group = AsynchronousChannelGroup.withCachedThreadPool(executor, initialSize);  
	        AsynchronousServerSocketChannel listener = AsynchronousServerSocketChannel.open(group);  
	        listener.bind(new InetSocketAddress(port), backlog);  
	        listener.accept(listener, new CompletionHandler<AsynchronousSocketChannel, AsynchronousServerSocketChannel>() {  
	  
	            @Override  
	            public void completed(AsynchronousSocketChannel channel, AsynchronousServerSocketChannel listener) {  
	                listener.accept(listener, this);  
	                ByteBuffer buffer = ByteBuffer.allocate(512);  
	                channel.read(buffer, buffer, new EchoHandler(channel, buffer));  
	            }  
	  
	            @Override  
	            public void failed(Throwable exc, AsynchronousServerSocketChannel listener) {  
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
	    }  
	  
	    /** 
	     * @param args 
	     * @throws IOException 
	     */  
	    public static void main(String[] args) throws IOException {  
	        AsynEchoServer server = new AsynEchoServer();  
	        server.start();  
	    }  
}

class EchoHandler implements CompletionHandler<Integer, ByteBuffer> {  
    private static Charset utf8 = Charset.forName("utf-8");  
    AsynchronousSocketChannel channel;  
    ByteBuffer buffer;  
  
    public EchoHandler(AsynchronousSocketChannel channel, ByteBuffer buffer) {  
        this.channel = channel;  
        this.buffer = buffer;  
    }  
  
    @Override  
    public void completed(Integer result, ByteBuffer buff) {  
        if (result == -1) {  
            try {  
                channel.close();  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
        } else if (result > 0) {  
            buffer.flip();  
            String msg = utf8.decode(buffer).toString();  
            System.out.println("echo: " + msg);  
            Future<Integer> w = channel.write(utf8.encode(msg));  
            try {  
                w.get();  
            } catch (InterruptedException e) {  
                e.printStackTrace();  
            } catch (ExecutionException e) {  
                e.printStackTrace();  
            }  
              
            buffer.clear();  
            channel.read(buff, buff, this);  
        }  
    }  
  
    @Override  
    public void failed(Throwable exc, ByteBuffer buff) {  
        // TODO Auto-generated method stub  
    }  
}  
