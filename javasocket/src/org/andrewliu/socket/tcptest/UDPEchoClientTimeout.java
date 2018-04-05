package org.andrewliu.socket.tcptest;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * UDP���ݱ����ԣ�
 * DatagramSocket/DatagramPacket
 * @author de
 *
 */
public class UDPEchoClientTimeout {

	private static final int TIMEOUT = 3000;
	private static final int MAXTRIES = 5;  //������Դ���
	
	public static void main(String[] args) throws IOException {
	
		String ip = "192.168.1.101";
		String sendContent = "����Ҫ���͵����ݡ�";
		int  servPort = 2502;
		InetAddress serverAddress = InetAddress.getByName(ip);
		byte[] bytesToSend  = sendContent.getBytes();
		
		DatagramSocket socket = new DatagramSocket();//UDP���ݷ�����
		
		socket.setSoTimeout(TIMEOUT);//�ȴ����û�з��أ����ط�
		//sendData,maxSendDataLength
		DatagramPacket sendPacket = new DatagramPacket(bytesToSend,bytesToSend.length,serverAddress,servPort);  //�������ݰ���
		//ReceiveArray,maxReceiveLeng
		DatagramPacket receivePacket = new DatagramPacket(new byte[bytesToSend.length],bytesToSend.length);// �������ݰ�
		
		int tries = 0;
		boolean receivedResponse = false;
		do{
			//��������
			socket.send(sendPacket);
			try{
				//��������
				socket.receive(receivePacket);
				//�жϽ������ݶ��뷢�����ݶ˵�IP�Ƿ�һ��
				if(!receivePacket.getAddress().equals(serverAddress)){
					throw new IOException("Received packet from an unknown source");
				}
				//��ʶ���ճɹ�
				receivedResponse = true;
			}catch(InterruptedIOException e){
				tries +=1;  //������ʧ��ʱ�������ԣ�һֱ������5��
				System.out.println("Timed out,"+(MAXTRIES - tries)+ "more tries....");
			}
		}while((!receivedResponse) && (tries < MAXTRIES));  //��δ���ճɹ��ң��ڿ����Է�Χ��ʱ������ѭ��
		
		//������յ����ݾʹӽ��հ��л������(getData()����ת��Ϊ�ַ���
		if(receivedResponse){
			System.out.println("Received: "+ new String(receivePacket.getData()));
		}else{
			System.out.println("No response -- giving up.");
		}
		socket.close();
		
		
	}
}
