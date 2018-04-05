package org.andrewliu.socket.codedecode;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 显式长度成帧实现
 * @author de
 *
 */
public class LengthFramer  implements Framer{

	private static final int MAXMESSAGELENGTH = 65535;
	private static final int BYTEMASK = 0xff;
	private static final int SHORTMASK = 0xffff;
	private static final int BYTESHIFT = 8;
	private DataInputStream in;
	
	public LengthFramer(InputStream in){
		this.in = new DataInputStream(in);
	}
	
	
	
	@Override
	public void frameMsg(byte[] message, OutputStream out) throws IOException {
		if(message.length > MAXMESSAGELENGTH){
			throw new IOException("message too long");
		}
		
		out.write((message.length>>BYTESHIFT)& BYTEMASK); //在消息前写入两个字节长的前缘
		out.write(message.length&BYTEMASK);
		out.write(message);  //写入消息
		out.flush();
	}

	@Override
	public byte[] nextMsg() throws IOException {
		
		int length;
		try{
			length = in.readUnsignedShort();//读取前两个前缀字节
		}catch(EOFException e){
			return null;
		}
		byte[] msg = new byte[length];
		in.readFully(msg);   //阻塞读字节直到读满数组
		return msg;
	}

}
