package org.andrewliu.thread.pipestream;

import java.io.PipedOutputStream;

public class ThreadByteWriter  extends Thread{
	private WriteByteStringData write;
	private PipedOutputStream out;
	
	public ThreadByteWriter(WriteByteStringData writeData,PipedOutputStream out){
		super();
		this.write = writeData;
		this.out = out;
	}

	@Override
	public void run(){
		write.writeByte(out);
	}
}
