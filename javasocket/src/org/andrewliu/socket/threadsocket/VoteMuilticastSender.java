package org.andrewliu.socket.threadsocket;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

import org.andrewliu.socket.codedecode.IVoteMsgCoder;
import org.andrewliu.socket.codedecode.VoteMsg;
import org.andrewliu.socket.codedecode.VoteMsgTextCoder;


/**
 * 投票广播发送实现
 * 广播：本地网络中的所有主机都是会接收到一份数据副本，广播时使用的是一个广播地址而非单播地址.广播不能使用连接.
 * IPV4的本地广播地址为: 255.255.255.255，它可以将消息发送到同一广播网络上的每个主机，本地广播信息不会被路由器转发。
 * 其还允许指定定向广播，允许向指定网络中的所有主机进行广播.
 * IPV6没有明确提供广播地址，但有一个特殊全节点、本地连接范围的多播地址（FF02::1)，发送给该地址的消息将多播到一个连接上的所有节点。
 * IP协议故意没有定义互联网范围内的广播机制。
 * 本地广播通用用于在网络游戏中处于同一本地（广播）网络的一家之间交换状态信息。
 * 互联网上的大部分路由器都不轩发定向广播消息。
 * @author de
 *
 *
 *一个多播地址指示了一组接收者，IP协议的设计者为多播分配了一定范围的地址空间，IPv4中的多播地址范围是224.0.0.0到239.255.255.255; 
 *IPv6中的多播地质局任何由FF开头的地址，除了少数系统保留的多播地址外，发送者可以向以上范围内的任何地址发送数据。
 *在java中，多播应用程序主要用MulticastSocket通过，它是DatagramSocket的一个子类.
 *一个MulticastSocket实例实际就是一个UDP套接字。
 *TCP/IP控制默认行为：
 *Keep-alive ----保持活跃,时间段内将探测
 *设置发送和接受缓存区的大小
 *获取发送和接受缓存区的大小
 *阻塞超时
 *设置或获取地址重用（多播时多应用程序用同一个套接字)
 *设置或获取TCP缓冲延迟
 *发送紧急数据
 *关闭后停留
 *设置或获取广播许可
 *设置或获取通信等级(传输优先级)
 *基于性能的协议选择
 *关闭连接
 *
 */
public class VoteMuilticastSender {

	public static final int CANDIDATEID = 475;
	public static void main(String[] args) throws IOException {
		InetAddress destAddr = InetAddress.getByName("192.168.1.101");
		//验证是否为多播地址
		if(!destAddr.isMulticastAddress()){
			throw new IllegalArgumentException(" Not a multicast address");
		}
		int  destPort = Integer.parseInt("5202");
		int TTL = 1;//设置报文生命周期 ，此值在每次路由转发时减1，从而可以控制数据包从发送者开始听能传递到的最远距离
		
		MulticastSocket sock = new MulticastSocket();
		sock.setTimeToLive(TTL);  //设置存活周期
		//编码器
		IVoteMsgCoder coder = new VoteMsgTextCoder();
		//要投票的对象
		VoteMsg vote = new VoteMsg(true,true,CANDIDATEID,1000001L);
		byte[] msg = coder.toWire(vote);
		//打包数据
		DatagramPacket message = new DatagramPacket(msg,msg.length,destAddr,destPort);
		System.out.println(" Sending Text-Encoded Request( "+msg.length + " byte);");
		System.out.println(vote);
		//开始广播(只有IPV4可以广播)
		sock.send(message);
		sock.close();
		
		
	}
}
