package org.andrewliu.thread.pipestream;

import java.io.IOException;
import java.io.PipedReader;
import java.io.PipedWriter;

public class TestWriteReadPipeStringStream {

	public static void main(String[] args) {
		try {
		WriteByteStringData writeData = new WriteByteStringData();
		ReadByteStringData  readData = new ReadByteStringData();
		
		PipedReader inputStream = new PipedReader();
		PipedWriter outputStream = new  PipedWriter();
		
		//inputStream.connect(outputStream)
		outputStream.connect(inputStream);
		ThreadStringReader threadStringReader = new ThreadStringReader(readData,inputStream);
		threadStringReader.start();
		
		Thread.sleep(2000);
		
		ThreadStringWriter  threadStringWriter = new ThreadStringWriter(writeData,outputStream);
		threadStringWriter.start();
		
		} catch (IOException e) {
		
			e.printStackTrace();
		} catch (InterruptedException e) {
			
			e.printStackTrace();
		}
	}
}
