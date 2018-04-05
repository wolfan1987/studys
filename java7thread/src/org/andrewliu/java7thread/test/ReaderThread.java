package org.andrewliu.java7thread.test;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

/**
 * ͨ����дThread��interrupt�������Ǳ�׼��ȡ��������װ��Thread��
 * �����жϵ������У�
 * 1��java.io���е�ͬ��Socket I/O
 * 2��java.io���е�ͬ��I/O
 * 3��Selector�첽I/O
 * 4�����ڻ�ȡĳ�������߳�
 * @author de
 *
 */
public class ReaderThread extends Thread{

	private final Socket socket;
	private final InputStream in;
	public ReaderThread(Socket socket) throws IOException{
		this.socket = socket;
		this.in = socket.getInputStream();
	}
	
	public void interrupt(){
		try{
			socket.close();
		}catch (IOException ignored){
			
		}finally{
			super.interrupt();
		}
	}
	
	public void run(){
		try{
			byte[] buf = new byte[1024];
			while(true){
				int count = in.read(buf);
				if(count < 0){
					break;
				}else if(count > 0){
					processBuffer(buf,count);
				}
			}
		}catch(IOException e){
			System.out.println("�����߳��˳�!");
		}
	}
	
	
	public  void processBuffer(byte[] buf,int count){
		
	}
}
