package org.andrewliu.thread.test2;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExceptionThread  implements Runnable{
	@Override
	public void run() {
		//�״��쳣���ͻ��˴������ͻ��˲��񲻵����쳣���޷����д���
		throw new RuntimeException();
	}

	public static void main(String[] args) {
		ExecutorService exec =  Executors.newCachedThreadPool();
		//�˴����ڿ���̨�׳��쳣��ExceptionThread��throw ��§�쳣����main��catch����
		exec.execute(new ExceptionThread());
	}
}
