package org.andrewliu.thread.pipestream;

/**
 * ģ�����ݿ⽻�汸�ݣ���A��B��A��B
 * @author de
 *
 */
public class DBTools {
	
	//��ʶǰһ�������Ƿ�ΪA�ڱ���
	volatile private boolean prevIsA = false;
	
	/**
	 * �ȱ���A��Ȼ����֪ͨB
	 */
	public synchronized void  backupA(){
		try{
			
			while(prevIsA == true){
				wait();
			}
			for(int i=0 ; i < 5; i++){
				System.out.println("AAAAAA");
			}
			//�����꣬��prevIsA��Ϊtrue��Ȼ��֪ͨB���Լ�������wait״̬
			prevIsA = true;
			notifyAll();
		}catch(InterruptedException e){
			e.printStackTrace();
		}
	}
	
	/**
	 * ��
	 */
	public synchronized void backupB(){
		try{
			while(prevIsA == false){
				wait();
			}
			for(int i=0; i<5; i++){
				System.out.println("BBBBBB");
			}
			//�����꽫prevIsA��Ϊfalse����֪ͨA���������ˣ�ͬʱ�Լ�����ȴ�״̬
			prevIsA = false;
			notifyAll();
		}catch(InterruptedException ex){
			ex.printStackTrace();
		}
	}

}
