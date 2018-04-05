package org.andrewliu.socket.codedecode;

import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


/**
 * ��֡�����:
 * ��֡�����������˽��ɶ���ζ�λ��Ϣ����βλ�����⡣��Ҫ����TCP�׽����У�
 * ��ΪTCPЭ����û����Ϣ�߽�ĸ�����ֽ����ǿɱ��ʱ���޷�ȷ����Ϣ���ȣ�
 * �����������ͼ���׽����ж�ȡ����Ϣ���������ֽڣ������ܷ��������������֮һ�������Ϣ��û��������Ϣ��
 * �����߽������ȴ���ͬʱ�޷�������յ�����Ϣ�����������Ҳ�ڵȴ����ն˵���Ӧ��Ϣ������γ���������һ���棬����ŵ��л���������Ϣ
 * ������߻Ὣ������Ϣ��һ��������ȫ��������һ����Ϣ��ȥ���⽫����Э�����������Ҫ������Ϣ��ÿ���ֶεı߽磺
 * ��������Ҫ֪��ÿ���ֶεĽ���λ�ú���һ���ֶεĿ�ʼλ�á�
 * ��֡���������ȶ�λ��Ϣ�Ľ���λ�ã�Ȼ����Ϣ��Ϊһ��������н������ڴ�����רע�ڽ�������Ϣ��Ϊһ֡���д�����Ҫ�����ּ�����ʹ������
 * ׼ȷ���ҵ���Ϣ�Ľ���λ�á�
 * ���ڶ��������Ϣ�Ľ�����һ��Ψһ���ָ�������������ڴ��������ݺ���ʾ��ӵ�һ�������ֽ����С���������ǲ����ڴ���������г��֡�(��ر����Ӻ󷵻ص�-1),
 *             ������似������Ϣ�г��ֵĶ���������޸ģ��Ӷ�ʹ�����߲�����ʶ��Ϊ��������ڽ�����ɨ�趨���ʱ������ʶ����޸��ص����ݣ�����
 *             �����Ϣ�ж�����л�ԭ���Ӷ�ʹ����ԭʼ��Ϣһ�¡�ȱ���Ƿ������������˫��������ɨ����Ϣ��
 * ��ʽ���ȣ��ڱ䳤�ֶλ���Ϣǰ����һ���̶���С���ֶΣ�����ָʾ���ֶλ���Ϣ�а����˶����ֽڡ�
 *          ǰ����Ҫ֪����Ϣ���ȵ����ޣ���������Ҫȷ����Ϣ�ĳ��ȣ���������Ϣ����һ����������Ϊ��Ϣ��ǰ׺����Ϣ�ĳ������޶�������������
 *          ��Ϣ��������Ҫ���ֽ�����
 * @author de
 * ���ڶ������֡�·����䶨��������� ����("\n"���ֽ�ֵΪ10)��
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
