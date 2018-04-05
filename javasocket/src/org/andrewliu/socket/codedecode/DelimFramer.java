package org.andrewliu.socket.codedecode;

import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


/**
 * 成帧与解析:
 * 成帧技术：则解决了接由端如何定位消息的首尾位置问题。主要用于TCP套接字中：
 * 因为TCP协议中没有消息边界的概念。当字节数是可变的时，无法确定消息长度，
 * 如果接收者试图从套接字中读取比消息本身更多的字节，将可能发生以下两种情况之一：如果信息中没有其他消息，
 * 接收者将阻塞等待，同时无法处理接收到的消息；如果发送者也在等待接收端的响应消息，则会形成死锁；另一方面，如果信道中还有其他消息
 * 则接收者会将后面消息的一部分甚至全部读到第一条消息中去，这将产生协议错误。所以需要查找消息中每个字段的边界：
 * 接收者需要知道每个字段的结束位置和下一个字段的开始位置。
 * 成帧技术：首先定位消息的结束位置，然后将消息作为一个整体进行解析。在此我们专注于将整个消息作为一帧进行处理。主要有两种技术可使接收者
 * 准确的找到消息的结束位置。
 * 基于定界符：消息的结束由一个唯一标记指出，即发送者在传输完数据后显示添加的一个特殊字节序列。这个特殊标记不会在传输的数据中出现。(如关闭连接后返回的-1),
 *             可用填充技术对消息中出现的定界符进行修改，从而使接收者不将其识别为定界符，在接收者扫描定界符时，还能识别出修改守的数据，并在
 *             输出消息中对其进行还原，从而使其与原始消息一致。缺点是发送者与接收者双方都必须扫描信息。
 * 显式长度：在变长字段或消息前附加一个固定大小的字段，用来指示该字段或消息中包信了多少字节。
 *          前提是要知道消息长度的上限，发送者先要确定消息的长度，将长度信息存入一个整数，作为消息的前缀。消息的长度上限定义了用来编码
 *          消息长度所需要的字节数。
 * @author de
 * 基于定界符成帧衣服，其定界符“换行 ‘符("\n"，字节值为10)。
 */
public class DelimFramer  implements Framer {

	
	private InputStream in;
	private static final byte  DELIMITER = 10;
	
	public DelimFramer(InputStream in){
		this.in = in;
	}
	
	@Override
	public void frameMsg(byte[] message, OutputStream out) throws IOException {
		
		for ( byte b : message){
			System.out.println("message.b = "+b);
			if(b == DELIMITER){
				throw new IOException("Message contains delimiter");
			}
		}
		
		out.write(message);
		out.write(DELIMITER);
		out.flush();
	}

	@Override
	public byte[] nextMsg() throws IOException {
		ByteArrayOutputStream messageBuffer = new ByteArrayOutputStream();
		int nextByte;
		while((nextByte = in.read())!= DELIMITER){
			if ( nextByte == -1){
				if(messageBuffer.size() == 1){
					return null;
				}else{
					throw new EOFException("Non-empty message without delimiter");
				}
			}
			messageBuffer.write(nextByte);
		}
		
		return messageBuffer.toByteArray();
	}

}
