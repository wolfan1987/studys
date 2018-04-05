package org.andrewliu.java7thread.test;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

/**
 * 通过改写Thread的interrupt方法将非标准的取消操作封装在Thread中
 * 不可中断的阻塞有：
 * 1、java.io包中的同步Socket I/O
 * 2、java.io包中的同不I/O
 * 3、Selector异步I/O
 * 4、正在获取某个锁的线程
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
			System.out.println("允许线程退出!");
		}
	}
	
	
	public  void processBuffer(byte[] buf,int count){
		
	}
}
