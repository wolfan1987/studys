package org.andrewliu.thread.pipestream;

import java.io.PipedReader;

public class ThreadStringReader extends Thread{
	

	private ReadByteStringData read;
	private PipedReader input;
	
	public ThreadStringReader(ReadByteStringData read,PipedReader input){
		super();
		this.read = read;
		this.input = input;
	}
	
	@Override
	public void run(){
		read.readString(input);
	}
}
