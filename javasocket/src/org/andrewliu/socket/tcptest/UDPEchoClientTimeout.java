package org.andrewliu.socket.tcptest;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * UDP数据报测试：
 * DatagramSocket/DatagramPacket
 * @author de
 *
 */
public class UDPEchoClientTimeout {

	private static final int TIMEOUT = 3000;
	private static final int MAXTRIES = 5;  //最多重试次数
	
	public static void main(String[] args) throws IOException {
	
		String ip = "192.168.1.101";
		String sendContent = "我是要发送的内容。";
		int  servPort = 2502;
		InetAddress serverAddress = InetAddress.getByName(ip);
		byte[] bytesToSend  = sendContent.getBytes();
		
		DatagramSocket socket = new DatagramSocket();//UDP数据发送器
		
		socket.setSoTimeout(TIMEOUT);//等待多久没有返回，则重发
		//sendData,maxSendDataLength
		DatagramPacket sendPacket = new DatagramPacket(bytesToSend,bytesToSend.length,serverAddress,servPort);  //发送数据包包
		//ReceiveArray,maxReceiveLeng
		DatagramPacket receivePacket = new DatagramPacket(new byte[bytesToSend.length],bytesToSend.length);// 接收数据包
		
		int tries = 0;
		boolean receivedResponse = false;
		do{
			//发送数据
			socket.send(sendPacket);
			try{
				//接收数据
				socket.receive(receivePacket);
				//判断接收数据端与发送数据端的IP是否一至
				if(!receivePacket.getAddress().equals(serverAddress)){
					throw new IOException("Received packet from an unknown source");
				}
				//标识接收成功
				receivedResponse = true;
			}catch(InterruptedIOException e){
				tries +=1;  //当接收失败时，就重试，一直可以试5次
				System.out.println("Timed out,"+(MAXTRIES - tries)+ "more tries....");
			}
		}while((!receivedResponse) && (tries < MAXTRIES));  //当未接收成功且，在可重试范围内时，不断循环
		
		//如果接收到数据就从接收包中获得数据(getData()，将转换为字符串
		if(receivedResponse){
			System.out.println("Received: "+ new String(receivePacket.getData()));
		}else{
			System.out.println("No response -- giving up.");
		}
		socket.close();
		
		
	}
}
