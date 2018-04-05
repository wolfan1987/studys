package org.andrewliu.socket.codedecode;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Arrays;

/**
 * 以UDP数据包形式实现投票
 * @author de
 *
 */
public class VoteClientUDP {

	public static void main(String[] args) throws IOException {
		InetAddress destAddr = InetAddress.getByName("192.168.1.101");
		int    destPort = 5202;
		int candidate = 888;
		//构建UDP套接字，并连接
		DatagramSocket sock = new DatagramSocket();
		sock.connect(destAddr, destPort);
		
		VoteMsg vote = new VoteMsg(false,false,candidate,0);
		//将投票信息以字符串形式编码
		IVoteMsgCoder coder = new VoteMsgTextCoder();
		//编码
		byte[]  encodedVote = coder.toWire(vote);
		System.out.println("Send ing Text-Encoded Request ("+ encodedVote.length+" bytes);");
		
		System.out.println(vote);;
		//构建UDP数据包，并发送到服务器端（UDP不要包装成帧，因为UDP已有维护消息边界
		DatagramPacket message = new DatagramPacket(encodedVote,encodedVote.length);
		sock.send(message);
		//再构造一个消息数据包，用于接收返回的数据
		message = new DatagramPacket(new byte[VoteMsgTextCoder.MAX_WIRE_LENGTH],VoteMsgTextCoder.MAX_WIRE_LENGTH);
		sock.receive(message);
		//从DP的getDate()方法中取得返回的数据
		encodedVote = Arrays.copyOfRange(message.getData(), 0, message.getLength());
		System.out.println("Received Text-Encoded Response("+ encodedVote.length+ "  bytes);");
		//将数据解码为对象
		vote = coder.fromWire(encodedVote);
		System.out.println(vote);
		
	}
	
}
