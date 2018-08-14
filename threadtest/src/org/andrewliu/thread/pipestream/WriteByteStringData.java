package org.andrewliu.thread.pipestream;

import java.io.IOException;
import java.io.PipedOutputStream;
import java.io.PipedWriter;

/**
 * ͨ���ܵ������̼߳���ֽ���ͨ��
 * ���ܵ��o��д�ֽ�����
 * @author de
 *
 */
public class WriteByteStringData {

	public void writeByte(PipedOutputStream out){
		try{
			System.out.println("write :");
			for(int i = 0; i < 300; i++){
				String outData = ""+(i+1);
				out.write(outData.getBytes());
				System.out.print(outData);
			}
			System.out.println();
			out.close();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	public void writeString(PipedWriter out){
		try{
			System.out.println("write :");
			for(int i=0; i < 300; i++){
				String outData = ""+(i+1);
				out.write(outData);
				System.out.print(outData);
			}
			System.out.println();
			out.close();
		}catch(IOException ex){
			ex.printStackTrace();
		}
	}
}
