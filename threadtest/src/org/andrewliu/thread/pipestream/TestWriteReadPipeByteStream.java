package org.andrewliu.thread.pipestream;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

public class TestWriteReadPipeByteStream {
	
	public static void main(String[] args) {
		try{
			WriteByteStringData  writeByteData = new WriteByteStringData();
			ReadByteStringData   readByteData = new ReadByteStringData();
			PipedInputStream inputStream = new PipedInputStream();
			PipedOutputStream  outputStream = new PipedOutputStream();
			//������ܵ�������ܵ�����
			//inputStream.connect(outputStream)
			//������ܵ��������������
			outputStream.connect(inputStream);
			ThreadByteReader threadReader = new ThreadByteReader(readByteData,inputStream);
			threadReader.start();
			
			Thread.sleep(2000);
			
			ThreadByteWriter  threadWriter = new ThreadByteWriter(writeByteData,outputStream);
			threadWriter.start();
			
		}catch(IOException ex){
			ex.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	


}

