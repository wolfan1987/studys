package org.andrewliu.socket.codedecode;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;


/**
 * ��TCP�׽�����ʽ��ʵ��ͶƱ����Ͷ����Ϣ���б���+��װ��֡,�ٷ��͵������VoteServerTCP.java
 * @author de
 *
 */
public class VoteClientTCP {

	public static final int CANDIDATEID = 888;
	
	public static void main(String[] args) throws UnknownHostException, IOException {
		String destAddr = "192.168.1.101";
		int destPort = 5202;
		Socket sock = new Socket(destAddr,destPort);
		OutputStream out = sock.getOutputStream();
		//�ö�������ʽ����Ϣ���б���
		IVoteMsgCoder coder = new VoteMsgBinCoder();
		//����ʽ���ȷ�ʽ�������ݶ����װ��֡
		Framer framer = new LengthFramer(sock.getInputStream());
		VoteMsg msg = new VoteMsg(false,true,CANDIDATEID,0);
		//�����ݱ���
		byte[] encodedMsg = coder.toWire(msg);
		System.out.println("Sending Inquiry ("+encodedMsg.length+" bytes ) ;");
		System.out.println(msg);
		//��װ��֡�󣬽����ݷ��ͳ�ȥ
		framer.frameMsg(encodedMsg,out);
		
		//��Ͷһ��Ʊ
		msg.setInquiry(false);
		encodedMsg = coder.toWire(msg);
		System.out.println("Sending Vote ("+ encodedMsg.length + "bytes);");
		framer.frameMsg(encodedMsg, out);
		//���մӷ���˷��ص���Ϣ�������뷵�سɶ���
		encodedMsg = framer.nextMsg();
		msg = coder.fromWire(encodedMsg);
		System.out.println(" Received Response ( "+ encodedMsg.length+" bytes);");
		System.out.println(msg);
		//�õ���һ��������Ϣ
		msg = coder.fromWire(framer.nextMsg());
		System.out.println("Received Response("+encodedMsg.length+"bytes);");
		System.out.println(msg);;
		sock.close();
	}
}
