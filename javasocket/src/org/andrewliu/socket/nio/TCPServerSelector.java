package org.andrewliu.socket.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;


/**
 * Selector:
 * Selector类可用于避免使用非阻塞式客户端中很浪费资源的”忙等“方法。NIO的选择器主要用于众多信道中选择一个客户端需要的进行I/O操作，
 * 一个selector实例可以同时检查一级信道的I/O状态。选择器就是一个多路开关选择器，因为一个选择器能够管理多个信道上的I/O操作。
 * 
 * 创建一个Selector实例也是使用静态工厂方法open()，并将其注册到想要监控的信道上（通过channel的方法实现，而不是使用selector的方法，
 * 最后，调用选择器的select()方法。该方法会阻塞等待，直到有一个或更多的信道准备好了I/O操作或等待超时。select()方法将返回可进行I/O操作的信道数量。
 * 现在，在一个单独的线程中，通过调用select()方法就能检查多个信道是否准备好进行I/O操作。如果经过一段时间后依然没有信道准备好，select()方法就返回0,
 * 并允许程序继续执行其他任务。
 * @author de
 *
 */
public class TCPServerSelector {

	private static final int BUFSIZE = 256;
	private static final int TIMEOUT = 3000;
	public static void main(String[] args) throws IOException {
		int[]  ports = {5201,5202,5203,5204,5205,5206,5207};
		Selector selector = Selector.open();//创建一个通道选择器
		for(int  port : ports){  //每个端口创建一个通道
			ServerSocketChannel listnChannel = ServerSocketChannel.open();
			listnChannel.socket().bind(new InetSocketAddress(port));  //并在此端口绑定
 			listnChannel.configureBlocking(false);  //设置通道为非阻塞式（即用完或超时就不占用资源）
			listnChannel.register(selector, SelectionKey.OP_ACCEPT); //给通道注册一个选择器，此时选择器指示通过可以进行接受连接操作
		}
		
		TCPProtocol protocol = new EchoSelectorProtocol(BUFSIZE);
		
		while(true){
			//如果没有得到准备好的I/O操作的信道就继续等
			if(selector.select(TIMEOUT) == 0){ 
				System.out.println("....");
				continue;
			}
			//当等待到有至少有一个I/O操作的信道时
			Iterator<SelectionKey> keyIter = selector.selectedKeys().iterator();  //该集合中包含了每个准备好某一I/O操作的信道的SelectionKey
			while(keyIter.hasNext()){
				SelectionKey key = keyIter.next();
				if(key.isAcceptable()){  //看是否连接就绪
					protocol.handleAccept(key);
				}
				if(key.isReadable()){  //看是否读取就绪
					protocol.handleRead(key);
				}
				if(key.isValid() && key.isWritable()){  //检测是否可读就绪
					protocol.handleWrite(key);
				}
				keyIter.remove();  //移除处理过的键
			}
		}
	}
}
