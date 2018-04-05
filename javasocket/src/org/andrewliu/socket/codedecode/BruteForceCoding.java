package org.andrewliu.socket.codedecode;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * 对基本类型进行编码，以方便其存入数组
 * int--32bit--4字节
 * short--16bit--2字节
 * long---64bit--8字节
 * byte-- 8bit--字节
 * 对于起过一个字节来表示的数据类型，必须知道这些字节的发送顺序：
 * 1、从整数的右边开始，由低位到高位的发磅，即little-endian顺序;  0在前，其它大数在后
 * 2、从左边开始，由高位到低位发磅，即：big-endian。 大数在前，0在后
 * 发送者与接收者要在使用传输顺序时，达成共训，到底是little-endian还是big-endian.
 * 发送者和接收者需要达成共识的最后一个细节是：所传输的数值是有符号的还是无符号的。java中的4种基本整型都是有符号的，它们的值
 * 以二进制补码的方式存储，这是有符号数据的常用表示方式。
 * 编码：  使用“位操作”（移位和屏蔽）来显式编码进行，然后将编码的数据存入字节数组.
 * 如果在发送端进行了编码，那么必须能够在接收端进行解码。
 * 发送者和接收者必须在文本字符串的表示方式上达成共识。即采用标准字符集.
 * 用位操作，对布尔值进行编码。
 * @author de
 *进行强制编码的实现
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
	 * 将byte型数据转换为字节字符
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
	 * 对int型进行从高位到低位编码
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
	 * 对int型进行从高位到低位解码
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
		
		
		//以上是自己手动以强制编码的方式进行转换，java提供了相关接口来进行字节数组转换，如下：
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
		 * 其它组合输入输出流类:
		 * 示例： Socket socket = new Socket(server,port);
		 * DataOutputStream out = new DateOutputStream(new BufferdOutputStream(socket.getOutputStream()));
		 * 
		 * Buffered[Input/Output]Stream     为输入/输出优化，执行中间缓存转换
		 * Checked[Input/Output]Stream      维护数据检查和
		 * Cipher[Input/Output]Stream       加密/解密数据
		 * Data[Input/Output]Stream         处理基本二进制类型的读/写
		 * Digest[Input/Output]Stream      维护数据摘要
		 * GZIP[Input/Output]Stream			以gzip格式解压缩/压缩字节流
		 * Object[Input/Output]Stream      处理读写对象和基本数据类型
		 * PushbackInputStream              允许一个字节是可以未读回退的
		 * PrintOutputStream               输出数据类型的字符串表示法
		 * Zip[Input/Output]Stream         以Zip格式解压缩/压缩字节流
		 */
	}
	
	
}
