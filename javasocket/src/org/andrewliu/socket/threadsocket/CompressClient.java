package org.andrewliu.socket.threadsocket;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;


/**
 * Socket类的shutdownInput()和shutdownOutput()方法能够将输入输出流相互独立地关闭。调用shutdownInput()后，套接字的输入流将无法使用。
 * 任何没有发送的数据都将毫无提示地被丢弃，任何想从套接字的输入流读取数据的操作都将返回-1。当socket调用shutdownOutput()方法后，套接字的输入流
 * 将无法再发送数据，任何尝试向输出流写数据的操作都将抛出一个IOException异常。在调用shutdownOutput()之前写出的数据可能能够被远程套接接字读取，
 * 之后，在远程套接字输入流上的读取操作将返回-1。应用程序调用shutdownOutput()后还能继续从套接字读取数据，类似的，在调用shutdownInput()后也能够
 * 继续写数据.
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
		//先发送未压缩数据
		sendBytes(sock,fileIn);
		//读取服务端已压缩传过来的数据
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
	//向服务端发磅数据
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
