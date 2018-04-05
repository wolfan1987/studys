package org.andrewliu.java7thread.java7base;

import java.io.File;
import java.util.concurrent.TimeUnit;

/**
 * if(Thread.interrupted()){ //�ȸ�Ϊtrue�ҷ���true,�����true���Ϊfalse�������޸�interrupted���Ե�ֵ
			Thread.currentThread().isInterrupted();//�ж��Ƿ��ж�
			Thread.currentThread().interrupt();//�жϣ���Ϊtrue
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
			}catch(InterruptedException e){  //��������ж��쳣������ִ��
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
		if(Thread.interrupted()){ //�ȸ�Ϊtrue�ҷ���true,�����true���Ϊfalse�������޸�interrupted���Ե�ֵ
			throw new InterruptedException();
		}
	}
	
	
	private void fileProcess(File file) throws InterruptedException{
		if(file.getName().equals(fileName)){
			System.out.printf("%s : %s\n",Thread.currentThread().getName(),file.getAbsolutePath());
		}
		if(Thread.interrupted()){  //����ж��ˣ��׳��쳣��Դ��
			throw new InterruptedException();
		}
	}
	
	
	public static void main(String[] args) {
		FileSearch_1_5 searcher = new FileSearch_1_5("G:\\TDDOWNLOAD","��ѹ����1024.txt");
		Thread thread = new Thread(searcher);
		thread.start();
		
		try{
			TimeUnit.SECONDS.sleep(10);
		}catch(InterruptedException e){
			e.printStackTrace();
		}
		thread.interrupt();//����10����ж��߳�
	}

}
