package org.andrewliu.proxy.test;

import java.lang.reflect.Method;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

public class HelloServiceCgLib implements MethodInterceptor{
	private  Object proxyTarget;
	
	public Object getTargetInstance(Object target){
		this.proxyTarget = target;
		Enhancer enhancer = new Enhancer();
		enhancer.setSuperclass(this.proxyTarget.getClass());
		//�ص�����
		enhancer.setCallback(this);
		//�����������
		return enhancer.create();
	}
	@Override
	public Object intercept(Object obj, Method method, Object[] args,
			MethodProxy proxy) throws Throwable {
		System.out.println("I'm  CGLIB proxy");
		//����ִ��ǰ
		System.out.println("exe  before......");
		Object returnObj = proxy.invokeSuper(obj,args);
		//����ִ�к�
		System.out.println("exe  after");
		return returnObj;
	}
	
	public static void main(String[] args) {
		HelloServiceCgLib   cgLib = new  HelloServiceCgLib();
		IHelloService  proxy = (IHelloService)cgLib.getTargetInstance(new HelloServiceImpl());
		proxy.sayHello("����");
		
	}

}
