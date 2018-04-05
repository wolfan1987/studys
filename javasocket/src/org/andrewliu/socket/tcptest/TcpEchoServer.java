package org.andrewliu.socket.tcptest;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;

/***
 * �׽��ַ����:SocketServer
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
			Socket clntSock  = serverSock.accept();//���ܿͻ�������
			SocketAddress clientAddress  = clntSock.getRemoteSocketAddress();
			//��ӡ�ͻ���IP��ַ
			System.out.println(" Handling client at "+clientAddress);
			//�õ������
			InputStream in = clntSock.getInputStream();
			//�õ������
			OutputStream out = clntSock.getOutputStream();
			//��ȡ�ͻ������ݣ�ֱ���ͻ��˹ر����׽���
			while((recvMsgSize = in.read(receiveBuf))!= -1){
				//���ͻ��˻��Զ���������
				out.write(receiveBuf,0,recvMsgSize);
			}
			clntSock.close();
		}
	}
	
}
