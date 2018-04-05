package org.andrewliu.socket.codedecode;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;


/**
 * 以TCP套接字形式，实现投票，对投标信息进行编码+包装成帧,再发送到服务端VoteServerTCP.java
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
		//用二进制形式对信息进行编码
		IVoteMsgCoder coder = new VoteMsgBinCoder();
		//以显式长度方式来将数据对象包装成帧
		Framer framer = new LengthFramer(sock.getInputStream());
		VoteMsg msg = new VoteMsg(false,true,CANDIDATEID,0);
		//将数据编码
		byte[] encodedMsg = coder.toWire(msg);
		System.out.println("Sending Inquiry ("+encodedMsg.length+" bytes ) ;");
		System.out.println(msg);
		//包装成帧后，将数据发送出去
		framer.frameMsg(encodedMsg,out);
		
		//再投一次票
		msg.setInquiry(false);
		encodedMsg = coder.toWire(msg);
		System.out.println("Sending Vote ("+ encodedMsg.length + "bytes);");
		framer.frameMsg(encodedMsg, out);
		//接收从服务端返回的信息，并解码返回成对象
		encodedMsg = framer.nextMsg();
		msg = coder.fromWire(encodedMsg);
		System.out.println(" Received Response ( "+ encodedMsg.length+" bytes);");
		System.out.println(msg);
		//得到下一个返回信息
		msg = coder.fromWire(framer.nextMsg());
		System.out.println("Received Response("+encodedMsg.length+"bytes);");
		System.out.println(msg);;
		sock.close();
	}
}
