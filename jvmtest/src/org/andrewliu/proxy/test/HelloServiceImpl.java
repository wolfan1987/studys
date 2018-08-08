package org.andrewliu.proxy.test;

public class HelloServiceImpl implements IHelloService {

	@Override
	public void sayHello(String name) {
		System.out.println("Hello you 2"+name);
	}

}
