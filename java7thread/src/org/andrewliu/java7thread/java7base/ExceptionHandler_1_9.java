package org.andrewliu.java7thread.java7base;

/**
 * 1���Ѷ����쳣��������ʱ�쳣/Checked Exception)�磺IOException,ClassNotFoundException�������쳣Ҫ��throws�����ָ�����ڷ����ڲ���
 * 2������ʱ�쳣��Unchecked Exception):�磺NumberFormatException.�����쳣��������ʱͻȻ�׳����쳣���Ƿ���Ҫ������������ȷ������
 * 3���̵߳�run()��������֧��throws��䣬���߳��׳�������ʱ�쳣ʱ�����Ǳ��벶���Ҵ������ǡ�������ʱ�쳣��run�������׳�ʱ��Ĭ����Ϊ���ڿ���̨��
 * ����ջ��¼�����˳����򡣵�java���ṩ���̶߳����ﲶ��ʹ�������ʱ�쳣�Ļ��ơ�
 * @author de
 *
 *ʵ��UncaughtExceptonHandler�ӿڣ�Ҫ�����̵߳��쳣����ʱ���ɽ���ʵ��ָ�����߳�
 *
 *Thread�࣬��һ��setDefaultUncaughtExceptionHandler()�������������������߳�Ĭ�ϵ��쳣������.
 *�߳��׳�һ��δ������쳣ʱ��JVM��Ϊ�쳣Ѱ���������ֿ��ܵĴ�������
 *�����̶߳����δ�����쳣�����������Ҳ�����JVM���������̶߳������ڵ��߳����δ�����쳣��������������Ҳ�����JVM��
 *��������Ĭ�ϵ�δ�����쳣������.
 *�Լ���---�߳����---Ĭ�ϵ�
 *
 */
public class ExceptionHandler_1_9  implements Thread.UncaughtExceptionHandler{

	@Override
	public void uncaughtException(Thread t, Throwable e) {
	     System.out.printf("An exception has been captured\n");
	     System.out.printf("thread: %s\n", t.getId());
	     System.out.printf("Exception: %s: %s\n",e.getClass().getName(),e.getMessage());
	     System.out.printf("Stack Trace: \n");
	     e.printStackTrace(System.out);
	     System.out.printf("Thread status: %s\n", t.getState());
	}

	public static void main(String[] args) {
		UnCaughtExceptionTask  task = new UnCaughtExceptionTask();
		Thread thread = new Thread(task);
		//�����̵߳��쳣������
		thread.setUncaughtExceptionHandler(new ExceptionHandler_1_9());
		thread.start();
	}
	
}

class UnCaughtExceptionTask implements Runnable{
	
	@Override
	public void run() {
		int numero = Integer.parseInt("TTT");
	}
	
}
