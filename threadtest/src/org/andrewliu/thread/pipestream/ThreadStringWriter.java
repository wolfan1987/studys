package org.andrewliu.thread.pipestream;

import java.io.PipedWriter;

public class ThreadStringWriter extends Thread{
	private WriteByteStringData  write;
	private PipedWriter out;
	
	public ThreadStringWriter(WriteByteStringData write,PipedWriter out){
		super();
		this.write = write;
		this.out = out;
	}
	
	@Override
	public void run(){
		write.writeString(out);
	}
}
