package org.andrewliu.jvm.test;

import java.util.Map;

public class ThreadStackTracePrint {
	
	public void  printThreadStackTrace(){
		
		for(Map.Entry<Thread,StackTraceElement[]> stackTrace : Thread.getAllStackTraces().entrySet()){
			Thread thread = (Thread) stackTrace.getKey();
			StackTraceElement[]  stack = (StackTraceElement[]) stackTrace.getValue();
			if(thread.equals(Thread.currentThread())){
				continue;
			}
			System.out.println("Ïß³Ì£º"+thread.getName()+"\n");
			for(StackTraceElement element : stack){
				System.out.println("\t"+element+"\n");
			}
		}
	}
	
	public static void main(String[] args) {
		ThreadStackTracePrint  tstp = new  ThreadStackTracePrint();
		tstp.printThreadStackTrace();
	}

}
