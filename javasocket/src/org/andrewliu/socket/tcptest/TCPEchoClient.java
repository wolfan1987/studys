package org.andrewliu.socket.tcptest;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * 回馈服务器：
 * Socket与ServerSocket的使用
 * @author de
 *
 */
public class TCPEchoClient {

	
	public static void main(String[] args) throws UnknownHostException, IOException {
		String server = "192.168.1.101";
		//用默认的编码给要传输的数据字符串编码
		byte[] data = "Hello, andrewLiu....".getBytes();
		int serverPort = 2502;
		Socket socket = new Socket(server,serverPort);
		
		System.out.println("Connected to server ... sending echo string");
		
		InputStream in = socket.getInputStream();  //得到输入流（接收时用）
		OutputStream out = socket.getOutputStream();//得到输出流，（发送时用)
		
		out.write(data);
		
		int totalBytesRcvd = 0;  //接收到了总字节大小
		int bytesRcvd; //每次读到的字节大小
		
		while(totalBytesRcvd < data.length){
			//每次将数据读到data中，读的位置是：从已读的大小开始到，总数据大小减去已读的， 直到坊到=-1，表示服务羰关闭了套接字
			if((bytesRcvd = in.read(data,totalBytesRcvd,data.length - totalBytesRcvd))== -1 ){    
				throw new SocketException("Connection closed prematurely");
			}
			totalBytesRcvd += bytesRcvd;// 将大小累加
		}
		//打印读到的数据
		System.out.println("Received: "+ new String(data));
		socket.close();
	}
	
}
