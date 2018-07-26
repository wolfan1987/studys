package org.andrewliu.proxy.test;

import java.lang.reflect.Method;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

/**
 * JDK中提供的生成动态代理类的机制有个鲜明的特点是：某个类必须有实现的接口，而生成的代理类也只能代理某个类接口定义的方法。
 * 那么如果一个类没有实现接口怎么办呢？这就有CGLIB的诞生了,前面说的JDK的代理类的实现方式是实现相关的接口成为接口的实现类,
 * 那么我们自然而然的可以想到用继承的方式实现相关的代理类。CGLIB就是这样做的。一个简单的CGLIB代理是这样实现的:
 * CGlib可以传入接口也可以传入普通的类，接口使用实现的方式,普通类使用会使用继承的方式生成代理类.
由于是继承方式,如果是 static方法,private方法,final方法等描述的方法是不能被代理的
做了方法访问优化，使用建立方法索引的方式避免了传统Method的方法反射调用.
提供callback 和filter设计，可以灵活地给不同的方法绑定不同的callback。编码更方便灵活。
CGLIB会默认代理Object中finalize,equals,toString,hashCode,clone等方法。比JDK代理多了finalize和clone。
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
