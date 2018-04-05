package org.andrewliu.socket.threadsocket;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.Arrays;

import org.andrewliu.socket.codedecode.VoteMsg;
import org.andrewliu.socket.codedecode.VoteMsgTextCoder;


/**
 * �ಥ������ʵ��
 * ��㲥��ͬ������ಥֻ����Ϣ�������͸�ָ����һ������ߣ���������߽����ಥ�飬ͨ������Ķಥ���飩��ַȷ�������ն���Ҫһ�ֻ���һ֪ͨ����
 * ���Է��͵�ĳһ�ض���ַ����Ϣ����Ȥ����ʹ���罫���ݰ�ת�����������ֻ��ƽ����ͼ���һ��:sk et multicastSocket���joinGroup()����ʵ�֡�
 * @author de
 *
 */
public class VoteMulticastReceiver {

	
	public static void main(String[] args) throws IOException {
		InetAddress address = InetAddress.getByName("192.168.1.101");  //�жϷ��Ͷ˵�ַ�ǲ��Ƕಥ��ַ
		if(!address.isMulticastAddress()){
			throw new IllegalArgumentException("Not a multicast address");
		}
		int port = 5202;
		// �õ�һ���ಥ�׽���
		MulticastSocket sock = new MulticastSocket(port);
		//����������ಥ��,������Ҫ������ಥ��ַ�ϵõ�����
		sock.joinGroup(address);
		
		VoteMsgTextCoder coder = new VoteMsgTextCoder();
		//������հ�
		DatagramPacket packet = new DatagramPacket(new byte[VoteMsgTextCoder.MAX_WIRE_LENGTH],VoteMsgTextCoder.MAX_WIRE_LENGTH);
		//���նಥ����������
		sock.receive(packet);
		//���յ������ݽ���Ϊ����
		VoteMsg vote = coder.fromWire(Arrays.copyOfRange(packet.getData(), 0, packet.getLength()));
		System.out.println("Received Text-Encoded Request ( "+ packet.getLength() + "bytes ) ;");
		
		System.out.println(vote);;
		sock.close();
	}
	
}
