package com.zjht.asyniobiframework.callback;

import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

public class AsynIoClientJsonWriteCompletionHandler implements CompletionHandler<Integer, AsynchronousSocketChannel>{

	
	@Override
	public void completed(Integer result,
			AsynchronousSocketChannel attachment) {
		System.out.println("发送json数据，结束！");
	}

	@Override
	public void failed(Throwable exc, AsynchronousSocketChannel attachment) {
		// TODO Auto-generated method stub
		
	}

}
