package com.zjht.hchpserver.example;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.charset.Charset;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class AsynEchoClient {

	 private static Charset utf8 = Charset.forName("utf-8");  
	    private int port = 9999;  
	    private String remoteHost;  
	    private String[] message;  
	    private AsynchronousSocketChannel channel;  
	  
	    public static void main(String args[]) throws Exception {  
	        if (args.length==0) {  
	            String msgs[] = new String[3];  
	            msgs[0] = " one ";
	            msgs[1] = " two ";
	            msgs[2] = " three ";
	            AsynEchoClient client = new AsynEchoClient("localhost", msgs);  
	            client.connect();  
	            for(int i = 0; i<5 ; i++){
	            	 client.sendAndReceive();  
	 	            Thread.sleep(3000);  
	            }
	           
	            Thread.sleep(3000);  
	        } else {  
	            System.out.println("usage EchoClient [remotehost] [messages .... ]");  
	        }  
	    }  
	  
	    public void sendAndReceive() throws InterruptedException, ExecutionException {  
	        ByteBuffer buffer = ByteBuffer.allocate(512);  
	        for (String msg : this.message) {  
	            Future<Integer> w = channel.write(utf8.encode(msg));  
	            w.get();  
	        }  
	          
	        channel.read(buffer, buffer, new ReceiverHandler(channel, buffer));  
//	        try {
//				close();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
	    }  
	  
	    public void close() throws IOException {  
	        channel.shutdownInput();  
	        channel.shutdownOutput();  
	    }  
	  
	
	    
	    public AsynEchoClient(String remoteHost, String[] message) {  
	        super();  
	        this.remoteHost = remoteHost;  
	        this.message = message;  
	    }  
	  
	    public void connect() throws IOException, InterruptedException, ExecutionException {  
	    	channel = AsynchronousSocketChannel.open();
	        Future<Void> r = channel.connect(new InetSocketAddress(this.remoteHost, this.port));  
	        r.get();  
	    }  
}
