package org.andrewliu.socket.codedecode;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * ��ʽ���ȳ�֡ʵ��
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
		
		out.write((message.length>>BYTESHIFT)& BYTEMASK); //����Ϣǰд�������ֽڳ���ǰԵ
		out.write(message.length&BYTEMASK);
		out.write(message);  //д����Ϣ
		out.flush();
	}

	@Override
	public byte[] nextMsg() throws IOException {
		
		int length;
		try{
			length = in.readUnsignedShort();//��ȡǰ����ǰ׺�ֽ�
		}catch(EOFException e){
			return null;
		}
		byte[] msg = new byte[length];
		in.readFully(msg);   //�������ֽ�ֱ����������
		return msg;
	}

}
