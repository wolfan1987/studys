package org.andrewliu.socket.threadsocket;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;


/**
 * Socket���shutdownInput()��shutdownOutput()�����ܹ�������������໥�����عرա�����shutdownInput()���׽��ֵ����������޷�ʹ�á�
 * �κ�û�з��͵����ݶ���������ʾ�ر��������κ�����׽��ֵ���������ȡ���ݵĲ�����������-1����socket����shutdownOutput()�������׽��ֵ�������
 * ���޷��ٷ������ݣ��κγ����������д���ݵĲ��������׳�һ��IOException�쳣���ڵ���shutdownOutput()֮ǰд�������ݿ����ܹ���Զ���׽ӽ��ֶ�ȡ��
 * ֮����Զ���׽����������ϵĶ�ȡ����������-1��Ӧ�ó������shutdownOutput()���ܼ������׽��ֶ�ȡ���ݣ����Ƶģ��ڵ���shutdownInput()��Ҳ�ܹ�
 * ����д����.
 * @author de
 *
 */
public class CompressClient {

	public static final int BUFSIZE = 256;
	public static void main(String[] args) throws UnknownHostException, IOException {
		String  server = "192.168.1.101";
		int port = 5202;
		String filename = "F:\\pms.sql";
		
		FileInputStream fileIn = new FileInputStream(filename);
		FileOutputStream fileOut = new FileOutputStream(filename+".gz");
		
		Socket sock = new Socket(server,port);
		//�ȷ���δѹ������
		sendBytes(sock,fileIn);
		//��ȡ�������ѹ��������������
		InputStream sockIn = sock.getInputStream();
		int bytesRead;
		byte[] buffer = new byte[BUFSIZE];
		while((bytesRead = sockIn.read(buffer))!=-1){
			fileOut.write(buffer,0,bytesRead);
			System.out.println("R");
		}
		System.out.println();
		
		sock.close();
		fileIn.close();
		fileOut.close();
		
	}
	//�����˷�������
	private static void sendBytes(Socket sock,InputStream fileIn) throws IOException{
		OutputStream sockOut = sock.getOutputStream();
		int bytesRead;
		byte[] buffer = new byte[BUFSIZE];
		while((bytesRead = fileIn.read(buffer)) != -1){
			sockOut.write(buffer,0,bytesRead);
			System.out.println("W");
		}
		sock.shutdownOutput();
	}
}
