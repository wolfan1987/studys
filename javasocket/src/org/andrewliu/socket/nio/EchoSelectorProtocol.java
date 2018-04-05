package org.andrewliu.socket.nio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;



public class EchoSelectorProtocol implements TCPProtocol {

	
	private int bufSize;

	
	public EchoSelectorProtocol(int bufSize) {
		super();
		this.bufSize = bufSize;
	}

	@Override
	public void handleAccept(SelectionKey key) throws IOException {
		//通过key可以得到信道,然后接受连接
		SocketChannel clntChan = ((ServerSocketChannel)key.channel()).accept();
		//设置信息为非阻塞式
		clntChan.configureBlocking(false);
		//注册通道可以进行的操作
		clntChan.register(key.selector(), SelectionKey.OP_READ,ByteBuffer.allocate(bufSize));
	}

	@Override
	public void handleRead(SelectionKey key) throws IOException {
		//得到信息
		SocketChannel clntChan = (SocketChannel)key.channel();
		//得到缓冲区的
		ByteBuffer buf = (ByteBuffer)key.attachment();  //访问信道的附件
		//读取数据
		long bytesRead = clntChan.read(buf);
		if(bytesRead == -1){
			//关闭时，将移除该信道关联的键
			clntChan.close();
		}else if(bytesRead >0){
			//当接收完数据，将信息标记为可写，并保留其可读,这些key只有在下次调用select()操作时才会起作用
			key.interestOps(SelectionKey.OP_READ | SelectionKey.OP_WRITE);
		}
	}

	@Override
	public void handleWrite(SelectionKey key) throws IOException {
		//得到要写的数据
		ByteBuffer buf = (ByteBuffer)key.attachment();
		//准备写数据
		buf.flip();
		//得到通道
		SocketChannel clntChan = (SocketChannel)key.channel();
		//往通道写数据
		clntChan.write(buf);
		//如果还有数据
		if(!buf.hasRemaining()){
			//设置其现在只能做的工作
			key.interestOps(SelectionKey.OP_READ);
		}
		//压缩buf中的数据
		buf.compact();
	}

}
