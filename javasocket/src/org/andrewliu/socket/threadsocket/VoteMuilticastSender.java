package org.andrewliu.socket.threadsocket;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

import org.andrewliu.socket.codedecode.IVoteMsgCoder;
import org.andrewliu.socket.codedecode.VoteMsg;
import org.andrewliu.socket.codedecode.VoteMsgTextCoder;


/**
 * ͶƱ�㲥����ʵ��
 * �㲥�����������е������������ǻ���յ�һ�����ݸ������㲥ʱʹ�õ���һ���㲥��ַ���ǵ�����ַ.�㲥����ʹ������.
 * IPV4�ı��ع㲥��ַΪ: 255.255.255.255�������Խ���Ϣ���͵�ͬһ�㲥�����ϵ�ÿ�����������ع㲥��Ϣ���ᱻ·����ת����
 * �仹����ָ������㲥��������ָ�������е������������й㲥.
 * IPV6û����ȷ�ṩ�㲥��ַ������һ������ȫ�ڵ㡢�������ӷ�Χ�Ķಥ��ַ��FF02::1)�����͸��õ�ַ����Ϣ���ಥ��һ�������ϵ����нڵ㡣
 * IPЭ�����û�ж��廥������Χ�ڵĹ㲥���ơ�
 * ���ع㲥ͨ��������������Ϸ�д���ͬһ���أ��㲥�������һ��֮�佻��״̬��Ϣ��
 * �������ϵĴ󲿷�·����������������㲥��Ϣ��
 * @author de
 *
 *
 *һ���ಥ��ַָʾ��һ������ߣ�IPЭ��������Ϊ�ಥ������һ����Χ�ĵ�ַ�ռ䣬IPv4�еĶಥ��ַ��Χ��224.0.0.0��239.255.255.255; 
 *IPv6�еĶಥ���ʾ��κ���FF��ͷ�ĵ�ַ����������ϵͳ�����Ķಥ��ַ�⣬�����߿��������Ϸ�Χ�ڵ��κε�ַ�������ݡ�
 *��java�У��ಥӦ�ó�����Ҫ��MulticastSocketͨ��������DatagramSocket��һ������.
 *һ��MulticastSocketʵ��ʵ�ʾ���һ��UDP�׽��֡�
 *TCP/IP����Ĭ����Ϊ��
 *Keep-alive ----���ֻ�Ծ,ʱ����ڽ�̽��
 *���÷��ͺͽ��ܻ������Ĵ�С
 *��ȡ���ͺͽ��ܻ������Ĵ�С
 *������ʱ
 *���û��ȡ��ַ���ã��ಥʱ��Ӧ�ó�����ͬһ���׽���)
 *���û��ȡTCP�����ӳ�
 *���ͽ�������
 *�رպ�ͣ��
 *���û��ȡ�㲥���
 *���û��ȡͨ�ŵȼ�(�������ȼ�)
 *�������ܵ�Э��ѡ��
 *�ر�����
 *
 */
public class VoteMuilticastSender {

	public static final int CANDIDATEID = 475;
	public static void main(String[] args) throws IOException {
		InetAddress destAddr = InetAddress.getByName("192.168.1.101");
		//��֤�Ƿ�Ϊ�ಥ��ַ
		if(!destAddr.isMulticastAddress()){
			throw new IllegalArgumentException(" Not a multicast address");
		}
		int  destPort = Integer.parseInt("5202");
		int TTL = 1;//���ñ����������� ����ֵ��ÿ��·��ת��ʱ��1���Ӷ����Կ������ݰ��ӷ����߿�ʼ���ܴ��ݵ�����Զ����
		
		MulticastSocket sock = new MulticastSocket();
		sock.setTimeToLive(TTL);  //���ô������
		//������
		IVoteMsgCoder coder = new VoteMsgTextCoder();
		//ҪͶƱ�Ķ���
		VoteMsg vote = new VoteMsg(true,true,CANDIDATEID,1000001L);
		byte[] msg = coder.toWire(vote);
		//�������
		DatagramPacket message = new DatagramPacket(msg,msg.length,destAddr,destPort);
		System.out.println(" Sending Text-Encoded Request( "+msg.length + " byte);");
		System.out.println(vote);
		//��ʼ�㲥(ֻ��IPV4���Թ㲥)
		sock.send(message);
		sock.close();
		
		
	}
}
