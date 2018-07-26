package org.andrewliu.proxy.test;

import java.lang.reflect.Method;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

/**
 * JDK���ṩ�����ɶ�̬������Ļ����и��������ص��ǣ�ĳ���������ʵ�ֵĽӿڣ������ɵĴ�����Ҳֻ�ܴ���ĳ����ӿڶ���ķ�����
 * ��ô���һ����û��ʵ�ֽӿ���ô���أ������CGLIB�ĵ�����,ǰ��˵��JDK�Ĵ������ʵ�ַ�ʽ��ʵ����صĽӿڳ�Ϊ�ӿڵ�ʵ����,
 * ��ô������Ȼ��Ȼ�Ŀ����뵽�ü̳еķ�ʽʵ����صĴ����ࡣCGLIB�����������ġ�һ���򵥵�CGLIB����������ʵ�ֵ�:
 * CGlib���Դ���ӿ�Ҳ���Դ�����ͨ���࣬�ӿ�ʹ��ʵ�ֵķ�ʽ,��ͨ��ʹ�û�ʹ�ü̳еķ�ʽ���ɴ�����.
�����Ǽ̳з�ʽ,����� static����,private����,final�����������ķ����ǲ��ܱ������
���˷��������Ż���ʹ�ý������������ķ�ʽ�����˴�ͳMethod�ķ����������.
�ṩcallback ��filter��ƣ��������ظ���ͬ�ķ����󶨲�ͬ��callback�������������
CGLIB��Ĭ�ϴ���Object��finalize,equals,toString,hashCode,clone�ȷ�������JDK�������finalize��clone��
 * @author AndrewLiu
 *
 */
public class CgLibProxy {
	
	public static void main(String[] args) {
		Enhancer enhancer = new Enhancer();
		enhancer.setSuperclass(Calculator.class);
		enhancer.setCallback(new MethodInterceptor(){
			
			@Override
			public Object intercept(Object obj, Method method, Object[] objects,
					MethodProxy methodProxy) throws Throwable {
				System.out.println("begin");
                Object invoke = methodProxy.invoke(new Calculator.CalculatorImpl(), objects);
                System.out.println("end");
                return invoke;
			}
		});
	
		Calculator proxy =(Calculator) enhancer.create();
		proxy.add(1,2);
	}

}
