package org.andrewliu.socket.codedecode;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Arrays;

/**
 * ��UDP���ݰ���ʽʵ��ͶƱ
 * @author de
 *
 */
public class VoteClientUDP {

	public static void main(String[] args) throws IOException {
		InetAddress destAddr = InetAddress.getByName("192.168.1.101");
		int    destPort = 5202;
		int candidate = 888;
		//����UDP�׽��֣�������
		DatagramSocket sock = new DatagramSocket();
		sock.connect(destAddr, destPort);
		
		VoteMsg vote = new VoteMsg(false,false,candidate,0);
		//��ͶƱ��Ϣ���ַ�����ʽ����
		IVoteMsgCoder coder = new VoteMsgTextCoder();
		//����
		byte[]  encodedVote = coder.toWire(vote);
		System.out.println("Send ing Text-Encoded Request ("+ encodedVote.length+" bytes);");
		
		System.out.println(vote);;
		//����UDP���ݰ��������͵��������ˣ�UDP��Ҫ��װ��֡����ΪUDP����ά����Ϣ�߽�
		DatagramPacket message = new DatagramPacket(encodedVote,encodedVote.length);
		sock.send(message);
		//�ٹ���һ����Ϣ���ݰ������ڽ��շ��ص�����
		message = new DatagramPacket(new byte[VoteMsgTextCoder.MAX_WIRE_LENGTH],VoteMsgTextCoder.MAX_WIRE_LENGTH);
		sock.receive(message);
		//��DP��getDate()������ȡ�÷��ص�����
		encodedVote = Arrays.copyOfRange(message.getData(), 0, message.getLength());
		System.out.println("Received Text-Encoded Response("+ encodedVote.length+ "  bytes);");
		//�����ݽ���Ϊ����
		vote = coder.fromWire(encodedVote);
		System.out.println(vote);
		
	}
	
}
