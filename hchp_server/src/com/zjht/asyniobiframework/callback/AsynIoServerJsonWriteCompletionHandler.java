package com.zjht.asyniobiframework.callback;

import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

public class AsynIoServerJsonWriteCompletionHandler implements CompletionHandler<Integer, AsynchronousSocketChannel>{

	@Override
	public void completed(Integer result,
			AsynchronousSocketChannel attachment) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void failed(Throwable exc, AsynchronousSocketChannel attachment) {
		// TODO Auto-generated method stub
		
	}

}
