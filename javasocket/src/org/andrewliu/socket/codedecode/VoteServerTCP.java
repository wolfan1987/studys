package org.andrewliu.socket.codedecode;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 投票服务器端：
 * @author de
 *
 */
public class VoteServerTCP {

	public static void main(String[] args) throws IOException {
		int port = 5202;
		ServerSocket servSock = new ServerSocket(port);
		//用于解码
		IVoteMsgCoder coder = new VoteMsgBinCoder();
		//处理投票
		VoteService service = new VoteService();
		
		//在循环中不断监听客户端投票请求
		while(true){
			Socket clntSock = servSock.accept();
			System.out.println(" Handling client at "+ clntSock.getRemoteSocketAddress());
			//帧信息处理器
			Framer framer = new LengthFramer(clntSock.getInputStream());
			try{
				byte[] req ; 
				//循环接受处理从客户端传来的VoteMsg
				while((req = framer.nextMsg()) !=null){
					System.out.println("Received message("+req.length+" bytes )");
					//将得到字节消息转换为对象，并放到service中去处理票数，且返回当前人的投票信息，以返回给客户端
					VoteMsg responseMsg = service.handleRequest(coder.fromWire(req));
					//将投票信息转换编码，并包装成显式长度帧，然后返回给客户端
					framer.frameMsg(coder.toWire(responseMsg), clntSock.getOutputStream());
				}
			}catch(IOException ioe){
				System.err.println(" Error handling client: "+ ioe.getMessage());
				ioe.printStackTrace();
			}finally{
				System.out.println("Closing connection!");
				clntSock.close();
			}
		}
		
	}
}
