package org.andrewliu.socket.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * NIO:
 * NIO�����һ��һ����ѯһ��ͻ��ˣ��Բ����ĸ��ͻ�����Ҫ����ķ�����
 * NIO�еĹؼ��������Selector��Channel��һ��Channelʵ��������һ��������ѯ"��,I/OĿ�꣨���׽���).Channel�ܹ�ע��һ��Selector���ʵ����
 * Selector��select()����������ѯ�ʡ���һ���ŵ����У���һ����ǰ��Ҫ���񣨱����ܣ�����д��
 * NIO�е�BufferΪ��selector��channelһ�δ������ͻ���ϵͳ�����ṩ�˸��߼��Ŀ��ƺͿ�Ԥ���ԡ�
 * Stream��Buffer�ĶԱȣ�
 * Stream:����õķ����������˵ײ㻺�����������ԣ��ṩ��һ���ܹ��������ⳤ�����ݵ������ļ��󡣻��ķ�����Ҫʵ������һ������Ҫô�����
 *         ���������ڿ�����Ҫô������������������л��������߶��У��ڶ��߳��и�������ʵ�֣���ʧȥ����ɿ������Ԥ���ԡ���ǰ��Socket��ֻ����������ʽ.
 * Buffer:�ְ�Channel���Ϊʹ��Bufferʵ�����������ݣ�Buffer���������һ�����������������������䱾����һ�����飬��ָ��ָʾ�����Ĵ�����ݺʹ��Ķ�ȡ����.
 *         �ô�1�����д�����������������ϵͳ������¶���˳���Ա���ô�2��һЩ��java���������Bufferӳ������ܹ�ֱ�Ӳ����ײ�ƽ̨����Դ����Щ����
 *         ��ʡ���ڲ�ͬ��ַ�ռ��и������ݵĿ�����
 *         �������й̶��ģ����޵���������ͬ�ڲ�״̬��¼���ж������ݷ����ȡ���������������Ķ��С�
 *         �������У�FloatBuffer,IntBuffer,ByteBuffer
 *         
 *Channel: Channelʵ��������һ�����豸�����ӣ����Ƿ������Եģ�ͨ�������Խ���������������������˼�����׽��ַǳ����ƣ�TCPЭ�飬����ʹ��ServerSocketChannel��SocketChannel
 *          ���������ͣ�FileChannel)���ŵ����׽���֮��Ĳ�ͬ��֮һ���ŵ�ͨ��Ҫ���þ�̬������������ȡʵ��,����ʹ�û����������ͻ��ȡ���ݡ�
 *          Channel��ʹ��Bufferʵ��ͨ������ʹ�ù��캯�������ģ�����ͨ������allocate()��������ָ��������Bufferʵ���磺
 *          ByteBuffer buffer = ByteBuffer.allocate(capacity);
 *          ByteBuffer  buffer =  ByteBuffer.wrap(new byte[3000]);
 *          ͨ��channel�� channel.configureBlocking(false)��ָ����Ϊ������ʽ���ŵ����ڷ�����ʽ�ŵ��ϵ���һ���������ǻ��������ء�
 * @author de
 *
 */
public class TCPEchoClientNoblocking {

	public static void main(String[] args) throws IOException {
		String server = "192.168.1.101";
		byte[] argument = "Hello ����!".getBytes();
		int servPort = 5202;
		//��һ��ͨ��
		SocketChannel clntChan = SocketChannel.open();
		//ָ��Ϊ������ʽͨ��
		clntChan.configureBlocking(false);
		
		if(!clntChan.connect(new InetSocketAddress(server,servPort))){  //�������ӣ��п��ܷ���ture��Ҳ�п�����Ϊ�Ƿ�����ʽ��û���ϡ���ʱ����while�У�finishConnect()����
			//�����Ƿ��������ˣ�û������(false)��һֱ��ӡ��������˳�ѭ��
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
				clntChan.write(writeBuf); //д
			}
			if((bytesRcvd = clntChan.read(readBuf)) == -1){  //��,������ -1 ��ʾ���ӹر�
				throw new  SocketException("Connectioin closed prematurely");
			}
			totalBytesRcvd += bytesRcvd;
			System.out.println("....");
		}
		System.out.println(" Received: "+ new String(readBuf.array(),0,totalBytesRcvd));
		clntChan.close();
	}
    
}
