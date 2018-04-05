package org.andrewliu.java7thread.java7base;

import java.io.File;
import java.util.concurrent.TimeUnit;

/**
 * if(Thread.interrupted()){ //先改为true且返回true,如果是true则改为false，用来修改interrupted属性的值
			Thread.currentThread().isInterrupted();//判断是否中断
			Thread.currentThread().interrupt();//中断，设为true
 * @author de
 *
 */
public class FileSearch_1_5  implements Runnable{

	private String initPath;
	private String fileName;
	public FileSearch_1_5(String initPath,String fileName){
		this.initPath = initPath;
		this.fileName = fileName;
	}
	@Override
	public void run() {
		File file = new  File(initPath);
		if ( file.isDirectory()){
			try{
				directoryProcess(file);
			}catch(InterruptedException e){  //如果遇到中断异常，继续执行
				System.out.printf("%ss: The search has been interrupted",Thread.currentThread().getName());
			}
		}
	}
	
	private void directoryProcess(File file) throws InterruptedException{
		File list[]  = file.listFiles();
		if(list != null){
			for ( int i = 0; i < list.length;i++){
				if(list[i].isDirectory()){
					directoryProcess(list[i]);
				}else{
					fileProcess(list[i]);
				}
			}
		}
		if(Thread.interrupted()){ //先改为true且返回true,如果是true则改为false，用来修改interrupted属性的值
			throw new InterruptedException();
		}
	}
	
	
	private void fileProcess(File file) throws InterruptedException{
		if(file.getName().equals(fileName)){
			System.out.printf("%s : %s\n",Thread.currentThread().getName(),file.getAbsolutePath());
		}
		if(Thread.interrupted()){  //如果中断了，抛出异常到源处
			throw new InterruptedException();
		}
	}
	
	
	public static void main(String[] args) {
		FileSearch_1_5 searcher = new FileSearch_1_5("G:\\TDDOWNLOAD","解压密码1024.txt");
		Thread thread = new Thread(searcher);
		thread.start();
		
		try{
			TimeUnit.SECONDS.sleep(10);
		}catch(InterruptedException e){
			e.printStackTrace();
		}
		thread.interrupt();//运行10秒后，中断线程
	}

}
