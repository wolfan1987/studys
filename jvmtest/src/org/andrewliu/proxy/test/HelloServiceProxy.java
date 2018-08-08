package org.andrewliu.proxy.test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class HelloServiceProxy implements InvocationHandler{
	
	//��ʵִ�ж���
	private  Object  target;
	
	//��ί�ж��󲢷���һ��������
	public Object bind(Object target){
		this.target = target;
		//ȡ�ô������
		return Proxy.newProxyInstance(target.getClass().getClassLoader(),target.getClass().getInterfaces(),this);
	}

	//ͨ�����������÷�����������˷�������
	/**
	 * @param  proxy �������
	 * @param  method �����÷���
	 * @param  args   ��������
	 */
	@Override
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		System.out.println(" I[m  JDK proxy");
		Object result = null;
		System.out.println("��׼��˵Hello");
		//ִ�з������൱��ִ��HelloServiceImpl ��sayHello����
		result = method.invoke(target,args);
		//���䷽�����ú�
		System.out.println("��˵��Hello��");
		return result;
	}
	
	public static void main(String[] args) {
		HelloServiceProxy  helloServiceProxy = new  HelloServiceProxy();
		IHelloService proxy = (IHelloService)helloServiceProxy.bind(new HelloServiceImpl());
		proxy.sayHello("������");
	}

}
