package org.andrewliu.socket.codedecode;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Arrays;

/**
 * UDP�׽���ʵ�ַ����
 * @author de
 *
 */
public class VoteServerUDP {

	public static void main(String[] args) throws IOException {
		//�������յĶ˿�
		int port = Integer.parseInt("5202");
		//UDP�����ʼ���
		DatagramSocket sock = new DatagramSocket(port);
		byte[] inBuffer = new byte[VoteMsgTextCoder.MAX_WIRE_LENGTH];
		//���ַ�����ʽ����
		IVoteMsgCoder coder = new VoteMsgTextCoder();
		//ͳ��ͶƱ�ķ���
		VoteService service = new VoteService();
		
		while(true){
			//�������ݰ�
			DatagramPacket packet = new DatagramPacket(inBuffer,inBuffer.length);
			sock.receive(packet);
			//�����ݱ��浽encodedMsg
			byte[] encodedMsg = Arrays.copyOfRange(packet.getData(), 0, packet.getLength());
			System.out.println("Handling request from "+ packet.getSocketAddress()+ " ("+ encodedMsg.length + "bytes)");
			
			try{
				//��encodedMsg����Ϊ����
				VoteMsg msg = coder.fromWire(encodedMsg);
				//ͳ��ͶƱ,�����ؽ��
				msg = service.handleRequest(msg);
				//����UDP���е�����Ϊ�������msg
				packet.setData(coder.toWire(msg));
				System.out.println("Sending response ("+ packet.getLength()+"byte);");
				System.out.println(msg);
				//��ͶƱ������ظ��ͻ���
				sock.send(packet);
			}catch(IOException ioe){
				System.err.println("Parse error in message: "+ioe.getMessage());
			}
		}
	}
	
}
