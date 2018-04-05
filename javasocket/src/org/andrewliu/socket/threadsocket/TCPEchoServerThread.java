package org.andrewliu.socket.threadsocket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Logger;


/**
 * 回馈服务器多线程版服务端
 * 一客户端不线程处理模式
 * @author de
 *
 */
public class TCPEchoServerThread {

	public static void main(String[] args) throws IOException {
		int echoServPort = 5202;
		ServerSocket servSock = new ServerSocket(echoServPort);
		Logger logger = Logger.getLogger("practical");
		while(true){
			//在循环中不断等待客户端连接,收到一个就启动一个线程去处理数据
			Socket clntSock = servSock.accept();
			Thread thread = new Thread(new EchoProtocol(clntSock,logger));
			thread.start();
			logger.info("Created and started Thread )"+ thread.getName());
		}
	}
}
