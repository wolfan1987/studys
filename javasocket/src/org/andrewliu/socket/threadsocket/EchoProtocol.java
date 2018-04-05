package org.andrewliu.socket.threadsocket;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.omg.CORBA_2_3.portable.OutputStream;

/**
 * 回馈服务器协议，多线程处理端
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
			//得到客户端Socket的输入输出流
			InputStream in = clntSock.getInputStream();
			java.io.OutputStream out  = clntSock.getOutputStream();
			
			int recvMsgSize;
			int totalBytesEchoed = 0;
			//缓存区
			byte[] echoBuffer = new byte[BUFSIZE];
			//一直读数据，读到客户端关闭连接
			while((recvMsgSize = in.read(echoBuffer)) != -1){
				out.write(echoBuffer,0,recvMsgSize);
				//将读到的数据累加
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
