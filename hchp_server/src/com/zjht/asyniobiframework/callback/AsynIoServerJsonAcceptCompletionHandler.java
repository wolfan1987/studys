package com.zjht.asyniobiframework.callback;

import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

public class AsynIoServerJsonAcceptCompletionHandler implements CompletionHandler<AsynchronousSocketChannel, AsynchronousServerSocketChannel> {

	@Override
	public void completed(AsynchronousSocketChannel result,
			AsynchronousServerSocketChannel attachment) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void failed(Throwable exc, AsynchronousServerSocketChannel attachment) {
		// TODO Auto-generated method stub
		
	}

}
