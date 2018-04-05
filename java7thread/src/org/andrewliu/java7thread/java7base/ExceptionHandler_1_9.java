package org.andrewliu.java7thread.java7base;

/**
 * 1、已定义异常（非运行时异常/Checked Exception)如：IOException,ClassNotFoundException，这种异常要在throws语句中指定或在方法内捕获
 * 2、运行时异常（Unchecked Exception):如：NumberFormatException.这种异常是在运行时突然抛出的异常，是否发生要看程序运行正确来定。
 * 3、线程的run()方法，不支持throws语句，当线程抛出非运行时异常时，我们必须捕获并且处理它们。当运行时异常从run方法中抛出时，默认行为是在控制台输
 * 出堆栈记录并且退出程序。但java有提供在线程对象里捕获和处理运行时异常的机制。
 * @author de
 *
 *实现UncaughtExceptonHandler接口，要设置线程的异常处理时，可将此实现指定给线程
 *
 *Thread类，有一个setDefaultUncaughtExceptionHandler()方法，用来设置所有线程默认的异常处理器.
 *线程抛出一个未捕获的异常时，JVM将为异常寻找以下三种可能的处理器：
 *查找线程对象的未捕获异常处理器，若找不到，JVM继续查找线程对象所在的线程组的未捕获异常处理器。如果还找不到，JVM将
 *继续查找默认的未捕获异常处理吕.
 *自己的---线程组的---默认的
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
		//设置线程的异常处理器
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
