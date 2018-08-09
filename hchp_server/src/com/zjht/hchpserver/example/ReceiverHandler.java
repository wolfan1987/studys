package com.zjht.hchpserver.example;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.Charset;

public class ReceiverHandler implements CompletionHandler<Integer, ByteBuffer> {  
    private static Charset utf8 = Charset.forName("utf-8");  
    private AsynchronousSocketChannel channel;  
    private ByteBuffer buffer;  
  
    @Override  
    public void completed(Integer result, ByteBuffer buff) {  
          
        if (result > 0) {              
            buffer.flip();  
            System.out.println(utf8.decode(buffer));  
            buffer.clear();  
            channel.read(buff, buff, this);  
        }else if (result==-1){  
            try {  
                channel.close();  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
        }  
    }  
  
    @Override  
    public void failed(Throwable exc, ByteBuffer buff) {  
        exc.printStackTrace();  
    }  
  
    public ReceiverHandler(AsynchronousSocketChannel channel, ByteBuffer buffer) {  
        super();  
        this.channel = channel;  
        this.buffer = buffer;  
    }  
}  