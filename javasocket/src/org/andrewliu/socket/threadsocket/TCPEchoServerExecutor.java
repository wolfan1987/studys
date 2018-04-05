package org.andrewliu.socket.threadsocket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

/**
 * TCP套接字服务端实现，
 * 基于jdk Executor 实现多线程处理客户端
 * @author de
 *
 */
public class TCPEchoServerExecutor {

	
	public static void main(String[] args) throws IOException {
		int echoServPort = 5202;
		ServerSocket servSock = new ServerSocket(echoServPort);
		Logger logger = Logger.getLogger("practical");
		//构造不定大小线程池
		Executor service = Executors.newCachedThreadPool();
		while(true){
			//接收客户端连接
			Socket clntSock = servSock.accept();
			//将连接将由调试器中的某个线程去处理
			service.execute(new EchoProtocol(clntSock,logger));
		}
		
	}
	
}
