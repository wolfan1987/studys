package org.andrewliu.socket.codedecode;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * �Ի������ͽ��б��룬�Է������������
 * int--32bit--4�ֽ�
 * short--16bit--2�ֽ�
 * long---64bit--8�ֽ�
 * byte-- 8bit--�ֽ�
 * �������һ���ֽ�����ʾ���������ͣ�����֪����Щ�ֽڵķ���˳��
 * 1�����������ұ߿�ʼ���ɵ�λ����λ�ķ�������little-endian˳��;  0��ǰ�����������ں�
 * 2������߿�ʼ���ɸ�λ����λ����������big-endian�� ������ǰ��0�ں�
 * �������������Ҫ��ʹ�ô���˳��ʱ����ɹ�ѵ��������little-endian����big-endian.
 * �����ߺͽ�������Ҫ��ɹ�ʶ�����һ��ϸ���ǣ����������ֵ���з��ŵĻ����޷��ŵġ�java�е�4�ֻ������Ͷ����з��ŵģ����ǵ�ֵ
 * �Զ����Ʋ���ķ�ʽ�洢�������з������ݵĳ��ñ�ʾ��ʽ��
 * ���룺  ʹ�á�λ����������λ�����Σ�����ʽ������У�Ȼ�󽫱�������ݴ����ֽ�����.
 * ����ڷ��Ͷ˽����˱��룬��ô�����ܹ��ڽ��ն˽��н��롣
 * �����ߺͽ����߱������ı��ַ����ı�ʾ��ʽ�ϴ�ɹ�ʶ�������ñ�׼�ַ���.
 * ��λ�������Բ���ֵ���б��롣
 * @author de
 *����ǿ�Ʊ����ʵ��
 */
public class BruteForceCoding {

	
	private static byte byteVal = 101;
	private static short shortVal = 10001;
	private static int intVal = 100000001;
	private static long longVal = 1000000000001L;
	
	private final static int BSIZE = Byte.SIZE / Byte.SIZE;
	private final static int SSIZE = Short.SIZE / Byte.SIZE;
	private final static int ISIZE = Integer.SIZE / Byte.SIZE;
	private final static int LSIZE = Long.SIZE / Byte.SIZE;
	
	private final static int BYTEMASK = 0xFF;  //8
	
	/**
	 * ��byte������ת��Ϊ�ֽ��ַ�
	 * @param bArray
	 * @return
	 */
	public static String byteArrayToDecimalString(byte[] bArray){
		StringBuilder rtn = new StringBuilder();
		for ( byte b : bArray){
			rtn.append(b & BYTEMASK).append(" ");
		}
		return rtn.toString();
	}
	
	/**
	 * ��int�ͽ��дӸ�λ����λ����
	 * @param dst
	 * @param val
	 * @param offset
	 * @param size
	 * @return
	 * Warning: Untested preconditions ( e.g., o<= size <=8)
	 */
	public static int encodeIntBigEndian(byte[] dst, long val,int offset,int size){
		for ( int i = 0; i < size; i++){
			dst[offset++] = (byte)(val>> ((size-i-1)* Byte.SIZE));
		}
		return offset;
	}
	
	/**
	 * ��int�ͽ��дӸ�λ����λ����
	 * @param dst
	 * @param val
	 * @param offset
	 * @param size
	 * @return
	 *Warning:Untested preconditions (e.g., o <= size <=8)
	 */
	public static long decodeIntBigEndian(byte[] val,int offset,int size){
		long rtn = 0;
		for ( int i = 0; i < size; i++){
			rtn = (rtn << Byte.SIZE) | ((long) val[offset+i] & BYTEMASK);
		}
		return rtn;
	}
	
	
	public static void main(String[] args) {
		byte[] message = new byte[BSIZE + SSIZE + ISIZE + LSIZE];
		int offset = encodeIntBigEndian(message,byteVal,0,BSIZE);
		offset = encodeIntBigEndian(message,shortVal,offset,SSIZE);
		offset = encodeIntBigEndian(message,intVal,offset,ISIZE);
		encodeIntBigEndian(message,longVal,offset,LSIZE);
		System.out.println("Encoded message : "+ byteArrayToDecimalString(message));
		
		long value = decodeIntBigEndian(message,BSIZE,SSIZE);
		System.out.println("Decoded short = "+ value);
		value = decodeIntBigEndian(message,BSIZE+SSIZE+ISIZE,LSIZE);
		System.out.println("Decoded long = "+value);
		
		offset = 4;
		value = decodeIntBigEndian(message,offset,BSIZE);
		System.out.println("Decoded value ( offset "+ offset+", size" + BSIZE+") = "+value);
		
		byte bValue = (byte)decodeIntBigEndian(message,offset,BSIZE);
		System.out.println("Same value as byte = "+bValue);
		
		
		//�������Լ��ֶ���ǿ�Ʊ���ķ�ʽ����ת����java�ṩ����ؽӿ��������ֽ�����ת�������£�
		ByteArrayOutputStream buf = new ByteArrayOutputStream();
		DataOutputStream out = new  DataOutputStream(buf);
		
		try {
			out.writeByte(byteVal);
			out.writeShort(shortVal);
			out.writeInt(intVal);
			out.writeLong(longVal);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		byte[] msg = buf.toByteArray();
		
		System.out.println("java auto translate:   "+byteArrayToDecimalString(msg));
		
		/****
		 * ������������������:
		 * ʾ���� Socket socket = new Socket(server,port);
		 * DataOutputStream out = new DateOutputStream(new BufferdOutputStream(socket.getOutputStream()));
		 * 
		 * Buffered[Input/Output]Stream     Ϊ����/����Ż���ִ���м仺��ת��
		 * Checked[Input/Output]Stream      ά�����ݼ���
		 * Cipher[Input/Output]Stream       ����/��������
		 * Data[Input/Output]Stream         ����������������͵Ķ�/д
		 * Digest[Input/Output]Stream      ά������ժҪ
		 * GZIP[Input/Output]Stream			��gzip��ʽ��ѹ��/ѹ���ֽ���
		 * Object[Input/Output]Stream      �����д����ͻ�����������
		 * PushbackInputStream              ����һ���ֽ��ǿ���δ�����˵�
		 * PrintOutputStream               ����������͵��ַ�����ʾ��
		 * Zip[Input/Output]Stream         ��Zip��ʽ��ѹ��/ѹ���ֽ���
		 */
	}
	
	
}
