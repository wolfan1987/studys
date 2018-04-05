package org.andrewliu.socket.codedecode;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Arrays;

/**
 * UDP套接字实现服务端
 * @author de
 *
 */
public class VoteServerUDP {

	public static void main(String[] args) throws IOException {
		//监听接收的端口
		int port = Integer.parseInt("5202");
		//UDP服务羰监听
		DatagramSocket sock = new DatagramSocket(port);
		byte[] inBuffer = new byte[VoteMsgTextCoder.MAX_WIRE_LENGTH];
		//以字符串格式编码
		IVoteMsgCoder coder = new VoteMsgTextCoder();
		//统计投票的服务
		VoteService service = new VoteService();
		
		while(true){
			//接收数据包
			DatagramPacket packet = new DatagramPacket(inBuffer,inBuffer.length);
			sock.receive(packet);
			//将数据保存到encodedMsg
			byte[] encodedMsg = Arrays.copyOfRange(packet.getData(), 0, packet.getLength());
			System.out.println("Handling request from "+ packet.getSocketAddress()+ " ("+ encodedMsg.length + "bytes)");
			
			try{
				//将encodedMsg解码为对象
				VoteMsg msg = coder.fromWire(encodedMsg);
				//统计投票,并返回结果
				msg = service.handleRequest(msg);
				//设置UDP包中的数据为编码过的msg
				packet.setData(coder.toWire(msg));
				System.out.println("Sending response ("+ packet.getLength()+"byte);");
				System.out.println(msg);
				//将投票结果返回给客户端
				sock.send(packet);
			}catch(IOException ioe){
				System.err.println("Parse error in message: "+ioe.getMessage());
			}
		}
	}
	
}
