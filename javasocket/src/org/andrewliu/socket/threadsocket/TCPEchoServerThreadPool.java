package org.andrewliu.socket.threadsocket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Logger;

/**
 * TCP套接字服务端实现
 * 线程池模式
 * @author de
 *
 */
public class TCPEchoServerThreadPool {

	public static void main(String[] args) throws IOException {
		int echoServPort = 5202;
		int threadPoolSize = 8;
		final ServerSocket servSock = new ServerSocket(echoServPort);
		
		final Logger logger = Logger.getLogger("practical");
		//用固定大小个数的服务端线程处理客户端连接
		for ( int i = 0; i < threadPoolSize; i++){
			Thread thread = new Thread(){
				public void run(){
					while(true){
						Socket clntSock;
						try {
							clntSock = servSock.accept();
							EchoProtocol.handleEchoClient(clntSock, logger);
						} catch (IOException e) {
							e.printStackTrace();
						}
						
					}
				}
			};
			thread.start();
			logger.info("Created and started Thread = "+ thread.getName());
		}
		
	}
	
}
