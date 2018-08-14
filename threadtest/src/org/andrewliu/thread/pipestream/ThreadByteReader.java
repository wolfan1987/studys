package org.andrewliu.thread.pipestream;

import java.io.PipedInputStream;

public class ThreadByteReader extends Thread{
	

	private ReadByteStringData  read;
	private PipedInputStream input;
	
	public ThreadByteReader(ReadByteStringData read,PipedInputStream input){
		super();
		this.read = read;
		this.input = input;
	}
	
	@Override
	public void run(){
		read.readByte(input);
	}
	
}
