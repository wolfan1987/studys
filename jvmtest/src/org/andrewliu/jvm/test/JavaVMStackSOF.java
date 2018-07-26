package org.andrewliu.jvm.test;

/**
 * vmArgs:  -Xss128k
 * 指定栈内存大小小128k
 * 测试将栈深度达到一定值时，会报StackOverflowError
 * @author AndrewLiu
 *
 */
public class JavaVMStackSOF {

	//保存栈深度
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
