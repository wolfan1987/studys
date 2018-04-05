package org.andrewliu.socket.threadsocket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * 限制回馈协议
 * 即:客户端与服务端进行连接(connect())或数据交换(read()/write())时，如果长时间阻塞，将影响性能，而java自带超时时间过长
 * 不利用应用，所以现实现自己的超时处理协议
 * @author de
 *
 */
public class TimeLimitEchoProtocol implements Runnable {

	private static final int BUFSIZE = 32;
	private static final String TIMELIMIT = "10000";  //超时长为10000毫秒
	private static final String TIMELIMITPROP = "Timelimit";
	private static int timelimit;
	private Socket clntSock;
	private Logger logger;
	
	
	
	public TimeLimitEchoProtocol(Socket clntSock, Logger logger) {
		this.clntSock = clntSock;
		this.logger = logger;
		timelimit = Integer.parseInt(System.getProperty(TIMELIMITPROP,TIMELIMIT));
	}
	
	public static void handleEchoClient(Socket clntSock, Logger logger){
		
		try{
			InputStream in = clntSock.getInputStream();
			OutputStream out = clntSock.getOutputStream();
			int recvMsgSize;
			int totalBytesEchoed = 0;
			byte[] echoBuffer = new byte[BUFSIZE];
			long endTime = System.currentTimeMillis()+timelimit;
			int timeBoundMillis = timelimit;
			
			clntSock.setSoTimeout(timeBoundMillis); //设置客户端的连接或处理超时时长
			//每接收一次时，检查是否还有时间接收timeBoundMiles》0和是否还有数据可接
			while((timeBoundMillis > 0) && ((recvMsgSize = in.read(echoBuffer)) !=-1)){
				out.write(echoBuffer,0,recvMsgSize);
				totalBytesEchoed += recvMsgSize;
				timeBoundMillis = (int)(endTime - System.currentTimeMillis()); //计算接收过了多长时间
				clntSock.setSoTimeout(timeBoundMillis);  //设置还有多久超时
			}
			
			logger.info(" Client "+ clntSock.getRemoteSocketAddress() + " , echoed "+ totalBytesEchoed + " bytes.");
			
		}catch(IOException ex){
			logger.log(Level.WARNING,"Exception in echo protocol",ex);
		}
	}



	@Override
	public void run() {
		handleEchoClient(this.clntSock,this.logger);
	}
}
