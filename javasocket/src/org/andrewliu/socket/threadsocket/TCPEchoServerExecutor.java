package org.andrewliu.socket.threadsocket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

/**
 * TCP�׽��ַ����ʵ�֣�
 * ����jdk Executor ʵ�ֶ��̴߳���ͻ���
 * @author de
 *
 */
public class TCPEchoServerExecutor {

	
	public static void main(String[] args) throws IOException {
		int echoServPort = 5202;
		ServerSocket servSock = new ServerSocket(echoServPort);
		Logger logger = Logger.getLogger("practical");
		//���첻����С�̳߳�
		Executor service = Executors.newCachedThreadPool();
		while(true){
			//���տͻ�������
			Socket clntSock = servSock.accept();
			//�����ӽ��ɵ������е�ĳ���߳�ȥ����
			service.execute(new EchoProtocol(clntSock,logger));
		}
		
	}
	
}
