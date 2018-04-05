package org.andrewliu.socket.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * NIO:
 * NIO提出了一种一次轮询一组客户端，以查找哪个客户端需要服务的方法。
 * NIO中的关键抽象点是Selector和Channel。一个Channel实例代表了一个“可轮询"的,I/O目标（如套接字).Channel能够注册一个Selector类的实例。
 * Selector的select()方法允许你询问”在一组信道“中，哪一个当前需要服务（被接受，读或写）
 * NIO中的Buffer为像selector和channel一次处理多个客户羰系统开销提供了更高级的控制和可预测性。
 * Stream与Buffer的对比：
 * Stream:抽象好的方面是隐藏了底层缓冲区的有限性，提供了一个能够容纳任意长度数据的容器的假象。坏的方面是要实现这样一个假象，要么会产生
 *         大量的内在开销，要么会引入大量的上下文切换，或两者都有，在多线程中更隐藏了实现，会失去对其可控性与可预测性。以前在Socket中只能用这种形式.
 * Buffer:现把Channel设计为使用Buffer实例来传递数据，Buffer抽象代表了一个有限容量的数据容器，其本质是一个数组，由指针指示了在哪存放数据和从哪读取数据.
 *         好处1：与读写缓冲氏数据相关联的系统开销暴露给了程序员。好处2：一些对java对象的特殊Buffer映射操作能够直接操作底层平台的资源。这些操作
 *         节省了在不同地址空间中复制数据的开销。
 *         缓冲区有固定的，有限的容量，并同内部状态记录了有多少数据放入或取出，像有限容量的队列。
 *         其子类有：FloatBuffer,IntBuffer,ByteBuffer
 *         
 *Channel: Channel实例代表了一个与设备的连接，它是非阻塞性的，通过它可以进入输入输出操作，其基本思想与套接字非常相似，TCP协议，可以使用ServerSocketChannel和SocketChannel
 *          （其它类型：FileChannel)，信道与套接字之间的不同点之一：信道通过要调用静态工厂方法来获取实例,其是使用缓冲区来发送或读取数据。
 *          Channel中使用Buffer实例通常不是使用构造函数创建的，而是通过调用allocate()方法创建指定容量的Buffer实例如：
 *          ByteBuffer buffer = ByteBuffer.allocate(capacity);
 *          ByteBuffer  buffer =  ByteBuffer.wrap(new byte[3000]);
 *          通过channel的 channel.configureBlocking(false)来指定其为非阻塞式的信道。在非阻塞式信道上调用一个方法总是会立即返回。
 * @author de
 *
 */
public class TCPEchoClientNoblocking {

	public static void main(String[] args) throws IOException {
		String server = "192.168.1.101";
		byte[] argument = "Hello 刘大安!".getBytes();
		int servPort = 5202;
		//打开一个通道
		SocketChannel clntChan = SocketChannel.open();
		//指定为非阻塞式通道
		clntChan.configureBlocking(false);
		
		if(!clntChan.connect(new InetSocketAddress(server,servPort))){  //建立连接，有可能返回ture，也有可能因为是非阻塞式，没连上。此时进入while中，finishConnect()用来
			//测试是否连接上了，没连接上(false)就一直打印，否则会退出循环
			while(!clntChan.finishConnect()){
				System.out.println(" I'm waiting......");
			}
		}
		ByteBuffer writeBuf = ByteBuffer.wrap(argument);
		ByteBuffer readBuf = ByteBuffer.allocate(argument.length);
		int totalBytesRcvd = 0;
		int bytesRcvd;
		while(totalBytesRcvd < argument.length){
			if(writeBuf.hasRemaining()){
				clntChan.write(writeBuf); //写
			}
			if((bytesRcvd = clntChan.read(readBuf)) == -1){  //读,当返回 -1 表示连接关闭
				throw new  SocketException("Connectioin closed prematurely");
			}
			totalBytesRcvd += bytesRcvd;
			System.out.println("....");
		}
		System.out.println(" Received: "+ new String(readBuf.array(),0,totalBytesRcvd));
		clntChan.close();
	}
    
}
