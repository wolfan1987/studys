package org.andrewliu.thread.test2;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExceptionThread  implements Runnable{
	@Override
	public void run() {
		//抛处异常给客户端处理，但客户端捕获不到此异常，无法进行处理
		throw new RuntimeException();
	}

	public static void main(String[] args) {
		ExecutorService exec =  Executors.newCachedThreadPool();
		//此处会在控制台抛出异常，ExceptionThread中throw 出搂异常，在main中catch不到
		exec.execute(new ExceptionThread());
	}
}
