package org.andrewliu.thread.pipestream;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedReader;

/**
 * 从管道流中读数据
 * @author de
 *
 */
public class ReadByteStringData {

	public void  readByte(PipedInputStream input){
		try{
			System.out.println("read :");
			byte[]  byteArray = new byte[20];
			int readLength = input.read(byteArray);
			while(readLength != -1){
				String newData = new String(byteArray,0,readLength);
				System.out.print(newData);
				readLength = input.read(byteArray);
			}
			System.out.println();
			input.close();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	public void readString(PipedReader input){
		try{
			System.out.println("read :");
			char[]  byteArray = new char[20];
			int readLength = input.read(byteArray);
			while(readLength != -1){
				String newData = new String(byteArray,0,readLength);
				System.out.print(newData);
				readLength  = input.read(byteArray);
			}
			System.out.println();
			input.close();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
}
