package org.andrewliu.socket.tcptest;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;

/***
 * 套接字服务端:SocketServer
 * @author de
 *
 */
public class TcpEchoServer {

	
	private static final int BUFSIZE = 32;
	public static void main(String[] args) throws IOException {
		int serverPort = 2502;
		ServerSocket serverSock = new ServerSocket(serverPort);
		int recvMsgSize;
		byte[] receiveBuf = new byte[BUFSIZE];
		while(true){
			Socket clntSock  = serverSock.accept();//接受客户端连接
			SocketAddress clientAddress  = clntSock.getRemoteSocketAddress();
			//打印客户端IP地址
			System.out.println(" Handling client at "+clientAddress);
			//得到输入济
			InputStream in = clntSock.getInputStream();
			//得到输出流
			OutputStream out = clntSock.getOutputStream();
			//坊取客户端数据，直到客户端关闭了套接字
			while((recvMsgSize = in.read(receiveBuf))!= -1){
				//往客户端回显读到的数据
				out.write(receiveBuf,0,recvMsgSize);
			}
			clntSock.close();
		}
	}
	
}
