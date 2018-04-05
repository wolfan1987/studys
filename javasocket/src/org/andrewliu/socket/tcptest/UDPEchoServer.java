package org.andrewliu.socket.tcptest;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 * UDP�����������������
 * @author de
 *
 */
public class UDPEchoServer {

	private static final int ECHOMAX = 255;
	
	public static void main(String[] args) throws IOException {
		
		int servPort = 2502;
		DatagramSocket socket = new DatagramSocket(servPort);   //UDP�׽��ַ���ˣ���servPortָ���˿ڼ������ݱ�
		DatagramPacket packet = new DatagramPacket(new byte[ECHOMAX],ECHOMAX);  //���ڽ��ջ������ݵ����ݰ�
		
		while(true){
			socket.receive(packet);//�ӿͻ��˽������ݱ�
			System.out.println("Handling client at "+ packet.getAddress().getHostAddress()+" on port "+ packet.getPort());  //����ͻ�����Ϣ
			socket.send(packet);  //���յ������ݣ����ظ��ͻ���
			packet.setLength(ECHOMAX); //�������ð��Ĵ�С��Ϊ�´ν���������׼��.
		}
	}
}
