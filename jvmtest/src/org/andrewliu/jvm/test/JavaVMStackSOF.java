package org.andrewliu.jvm.test;

/**
 * vmArgs:  -Xss128k
 * ָ��ջ�ڴ��СС128k
 * ���Խ�ջ��ȴﵽһ��ֵʱ���ᱨStackOverflowError
 * @author AndrewLiu
 *
 */
public class JavaVMStackSOF {

	//����ջ���
	private int stackLength = 1;
	
	public void stackLeak(){
		stackLength++;
		stackLeak();
	}
	
	public static void main(String[] args) {
		JavaVMStackSOF oom = new JavaVMStackSOF();
		try{
			oom.stackLeak();
		}catch(Throwable e){
			System.out.println("stack Length:"+oom.stackLength);
			throw e;
		}
	}
}
