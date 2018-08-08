package org.andrewliu.proxy.test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class HelloServiceProxy implements InvocationHandler{
	
	//真实执行对象
	private  Object  target;
	
	//绑定委托对象并返回一个代理类
	public Object bind(Object target){
		this.target = target;
		//取得代理对象
		return Proxy.newProxyInstance(target.getClass().getClassLoader(),target.getClass().getInterfaces(),this);
	}

	//通过代理对象调用方法，将进入此方法调用
	/**
	 * @param  proxy 代理对象
	 * @param  method 被调用方法
	 * @param  args   方法参数
	 */
	@Override
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		System.out.println(" I[m  JDK proxy");
		Object result = null;
		System.out.println("我准备说Hello");
		//执行方法，相当于执行HelloServiceImpl 的sayHello方法
		result = method.invoke(target,args);
		//反射方法调用后
		System.out.println("我说过Hello了");
		return result;
	}
	
	public static void main(String[] args) {
		HelloServiceProxy  helloServiceProxy = new  HelloServiceProxy();
		IHelloService proxy = (IHelloService)helloServiceProxy.bind(new HelloServiceImpl());
		proxy.sayHello("张三“");
	}

}
