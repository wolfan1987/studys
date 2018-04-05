package org.andrewliu.socket.threadsocket;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.Arrays;

import org.andrewliu.socket.codedecode.VoteMsg;
import org.andrewliu.socket.codedecode.VoteMsgTextCoder;


/**
 * 多播接收者实现
 * 与广播不同，网络多播只将消息副本发送给指定的一组接收者，这组接收者叫做多播组，通过共享的多播（组）地址确定。接收都需要一种机制一通知网络
 * 它对发送到某一特定地址的消息感兴趣，以使网络将数据包转发给它，这种机制叫做和加入一组:sk et multicastSocket类的joinGroup()方法实现。
 * @author de
 *
 */
public class VoteMulticastReceiver {

	
	public static void main(String[] args) throws IOException {
		InetAddress address = InetAddress.getByName("192.168.1.101");  //判断发送端地址是不是多播地址
		if(!address.isMulticastAddress()){
			throw new IllegalArgumentException("Not a multicast address");
		}
		int port = 5202;
		// 得到一个多播套接字
		MulticastSocket sock = new MulticastSocket(port);
		//并加入这个多播组,即：我要从这个多播地址上得到数据
		sock.joinGroup(address);
		
		VoteMsgTextCoder coder = new VoteMsgTextCoder();
		//构造接收包
		DatagramPacket packet = new DatagramPacket(new byte[VoteMsgTextCoder.MAX_WIRE_LENGTH],VoteMsgTextCoder.MAX_WIRE_LENGTH);
		//接收多播过来的数据
		sock.receive(packet);
		//将收到的数据解码为对象
		VoteMsg vote = coder.fromWire(Arrays.copyOfRange(packet.getData(), 0, packet.getLength()));
		System.out.println("Received Text-Encoded Request ( "+ packet.getLength() + "bytes ) ;");
		
		System.out.println(vote);;
		sock.close();
	}
	
}
