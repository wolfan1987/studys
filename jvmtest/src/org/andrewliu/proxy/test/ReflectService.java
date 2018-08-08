package org.andrewliu.proxy.test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ReflectService {
	public void  sayHello(String name){
		System.out.println("hello  "+name);
	}
	
	
	public static void main(String[] args) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, ClassNotFoundException, NoSuchMethodException, SecurityException {
		Object service = Class.forName(ReflectService.class.getName()).newInstance();
		Method method = service.getClass().getMethod("sayHello",String.class);
		method.invoke(service,"zhangsan");
	}

}
