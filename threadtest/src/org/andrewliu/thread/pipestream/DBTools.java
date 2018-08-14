package org.andrewliu.thread.pipestream;

/**
 * 模拟数据库交叉备份，先A再B再A再B
 * @author de
 *
 */
public class DBTools {
	
	//标识前一个备份是否为A在备份
	volatile private boolean prevIsA = false;
	
	/**
	 * 先备份A，然后再通知B
	 */
	public synchronized void  backupA(){
		try{
			
			while(prevIsA == true){
				wait();
			}
			for(int i=0 ; i < 5; i++){
				System.out.println("AAAAAA");
			}
			//备份完，将prevIsA设为true，然后将通知B，自己将进行wait状态
			prevIsA = true;
			notifyAll();
		}catch(InterruptedException e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 当
	 */
	public synchronized void backupB(){
		try{
			while(prevIsA == false){
				wait();
			}
			for(int i=0; i<5; i++){
				System.out.println("BBBBBB");
			}
			//备份完将prevIsA设为false，并通知A可以运行了，同时自己进入等待状态
			prevIsA = false;
			notifyAll();
		}catch(InterruptedException ex){
			ex.printStackTrace();
		}
	}

}
