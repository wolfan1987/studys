package org.andrewliu.socket.tcptest;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 * UDP回馈服务器，服务端
 * @author de
 *
 */
public class UDPEchoServer {

	private static final int ECHOMAX = 255;
	
	public static void main(String[] args) throws IOException {
		
		int servPort = 2502;
		DatagramSocket socket = new DatagramSocket(servPort);   //UDP套接字服务端，在servPort指定端口监听数据报
		DatagramPacket packet = new DatagramPacket(new byte[ECHOMAX],ECHOMAX);  //用于接收或发送数据的数据包
		
		while(true){
			socket.receive(packet);//从客户端接受数据报
			System.out.println("Handling client at "+ packet.getAddress().getHostAddress()+" on port "+ packet.getPort());  //打包客户端信息
			socket.send(packet);  //将收到的数据，返回给客户羰
			packet.setLength(ECHOMAX); //重新设置包的大小，为下次接受数据做准备.
		}
	}
}
