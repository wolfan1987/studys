package org.andrewliu.proxy.test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * DK��̬����С��
�������Ƕ�JDK�����и��򵥵�Դ�뼶�����ʶ,�����һ��˼·:JDK�������������ʱ����һ��������,���������ʵ���Ͼ���������Ҫ����Ľӿڵ�ʵ���ࡣʵ�ֵķ�����������InvocationHandler���е�invoke����,����ͬʱ�����������õķ����ĵ�Method����Ͳ����б������Ǳ���ʵ�ַ����ĵ��á��������ǵ���reduce��������ô���ǾͿ���ͨ��Method.Invoke(Object obj, Object... args)�������Ǿ����ʵ����,������Χ��һЩ���������¶�����ʵ���˶�̬�������Ƕ�JDK��������һЩ�򵥵���ʶ��

JDK��̬����ֻ�ܴ����нӿڵ���,�������ܴ���ӿڷ���,���ܴ���һ������еķ���
�ṩ��һ��ʹ��InvocationHandler��Ϊ�����Ĺ��췽�����ڴ���������һ���װ,ҵ���߼���invoke������ʵ��
��д��Object���equals��hashCode��toString�����Ƕ�ֻ�Ǽ򵥵ĵ�����InvocationHandler��invoke�����������Զ����������Ĳ�����Ҳ����˵JDK�Ķ�̬�������Դ���������������
��invoke�����������������Բ���Method.invoke��������ʵ����ͷ��ء����ַ�ʽ��������RPC�����,��invoke�����з���ͨ�ŵ���Զ�˵Ľӿڵ�

JDK���ṩ�����ɶ�̬������Ļ����и��������ص��ǣ�ĳ���������ʵ�ֵĽӿڣ������ɵĴ�����Ҳֻ�ܴ���ĳ����ӿڶ���ķ�����
 * @author AndrewLiu
 *
 */
public class ProxyFactory implements  InvocationHandler{

	private  Class<?>  target;
	private  Object real;
	
	public  ProxyFactory(Class<?> target){
		this.target = target;
	}
	
	public  Object bind(Object real){
		this.real = real;
		//����JDK�ṩ��Proxyʵ�ֶ�̬����,����ʵ�ʵ�ʵ����
		return Proxy.newProxyInstance(target.getClassLoader(),new Class[]{target},this);
	}
	
	@Override
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		//����ʼ
		System.out.println("begin");
		//ִ��ʵ�ʵķ���
		Object invoke = method.invoke(real,args);
		//�������
		System.out.println("end");
		return invoke;
	}
	
	public static void main(String[] args) {
		Calculator proxy = (Calculator)new ProxyFactory(Calculator.class).bind(new Calculator.CalculatorImpl());
		proxy.add(5,6);
	}

}
