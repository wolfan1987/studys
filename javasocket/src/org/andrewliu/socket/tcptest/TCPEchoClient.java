package org.andrewliu.socket.tcptest;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * ������������
 * Socket��ServerSocket��ʹ��
 * @author de
 *
 */
public class TCPEchoClient {

	
	public static void main(String[] args) throws UnknownHostException, IOException {
		String server = "192.168.1.101";
		//��Ĭ�ϵı����Ҫ����������ַ�������
		byte[] data = "Hello, andrewLiu....".getBytes();
		int serverPort = 2502;
		Socket socket = new Socket(server,serverPort);
		
		System.out.println("Connected to server ... sending echo string");
		
		InputStream in = socket.getInputStream();  //�õ�������������ʱ�ã�
		OutputStream out = socket.getOutputStream();//�õ��������������ʱ��)
		
		out.write(data);
		
		int totalBytesRcvd = 0;  //���յ������ֽڴ�С
		int bytesRcvd; //ÿ�ζ������ֽڴ�С
		
		while(totalBytesRcvd < data.length){
			//ÿ�ν����ݶ���data�У�����λ���ǣ����Ѷ��Ĵ�С��ʼ���������ݴ�С��ȥ�Ѷ��ģ� ֱ������=-1����ʾ�����ʹر����׽���
			if((bytesRcvd = in.read(data,totalBytesRcvd,data.length - totalBytesRcvd))== -1 ){    
				throw new SocketException("Connection closed prematurely");
			}
			totalBytesRcvd += bytesRcvd;// ����С�ۼ�
		}
		//��ӡ����������
		System.out.println("Received: "+ new String(data));
		socket.close();
	}
	
}
