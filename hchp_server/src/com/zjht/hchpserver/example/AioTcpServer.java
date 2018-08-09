package com.zjht.hchpserver.example;

import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class AioTcpServer implements Runnable {  
    private AsynchronousChannelGroup asyncChannelGroup;//aio的核心之一通道组.由它负责处理事件,完成之后通知相应的handler  
    private AsynchronousServerSocketChannel listener;//端口侦听器  
  
    public AioTcpServer(int port) throws Exception {  
        ExecutorService executor = Executors.newFixedThreadPool(20);  
        asyncChannelGroup = AsynchronousChannelGroup.withThreadPool(executor);  
        listener = AsynchronousServerSocketChannel.open(asyncChannelGroup).bind(new InetSocketAddress("localhost",port));  
    }  
  
    public void run() {  
//        try {  
           // Future<AsynchronousSocketChannel> future = 
            		listener.accept(listener, new AioAcceptHandler()); 
          //  future.get();//此步为阻塞方法,直到有连接上来为止.  
//        } catch (InterruptedException e) {  
//           e.printStackTrace();  
//        } catch (Exception e) {  
//            e.printStackTrace();  
//        } finally {  
//  
//        }  
    }  
  
    public static void main(String... args) throws Exception {  
        AioTcpServer server = new AioTcpServer(9998);  
        new Thread(server).start();  
    }  
}  


