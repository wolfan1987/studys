package org.andrewliu.socket.threadsocket;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.omg.CORBA_2_3.portable.OutputStream;

/**
 * ����������Э�飬���̴߳����
 * @author de
 *
 */
public class EchoProtocol  implements Runnable{

	
	private static final int BUFSIZE = 32;
	private Socket clntSock;
	private Logger logger;
	
	
	
	public EchoProtocol(Socket clntSock, Logger logger) {
		super();
		this.clntSock = clntSock;
		this.logger = logger;
	}

	public static void handleEchoClient(Socket clntSock,Logger logger){
		try{
			//�õ��ͻ���Socket�����������
			InputStream in = clntSock.getInputStream();
			java.io.OutputStream out  = clntSock.getOutputStream();
			
			int recvMsgSize;
			int totalBytesEchoed = 0;
			//������
			byte[] echoBuffer = new byte[BUFSIZE];
			//һֱ�����ݣ������ͻ��˹ر�����
			while((recvMsgSize = in.read(echoBuffer)) != -1){
				out.write(echoBuffer,0,recvMsgSize);
				//�������������ۼ�
				totalBytesEchoed += recvMsgSize;
			}
			logger.info("Client "+clntSock.getRemoteSocketAddress() + ", echoed "+ totalBytesEchoed + "bytes.");
		}catch(IOException ioe){
			logger.log(Level.WARNING, "Exception in echo protocol",ioe);
		}finally{
			try{
				clntSock.close();
			}catch(IOException e){
				
			}
		}
	}


	@Override
	public void run() {
		handleEchoClient(clntSock,logger);
	}
	
	

}
