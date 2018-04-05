package org.andrewliu.socket.threadsocket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Logger;


/**
 * �������������̰߳�����
 * һ�ͻ��˲��̴߳���ģʽ
 * @author de
 *
 */
public class TCPEchoServerThread {

	public static void main(String[] args) throws IOException {
		int echoServPort = 5202;
		ServerSocket servSock = new ServerSocket(echoServPort);
		Logger logger = Logger.getLogger("practical");
		while(true){
			//��ѭ���в��ϵȴ��ͻ�������,�յ�һ��������һ���߳�ȥ��������
			Socket clntSock = servSock.accept();
			Thread thread = new Thread(new EchoProtocol(clntSock,logger));
			thread.start();
			logger.info("Created and started Thread )"+ thread.getName());
		}
	}
}
