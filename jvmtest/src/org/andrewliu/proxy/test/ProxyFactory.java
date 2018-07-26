package org.andrewliu.proxy.test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * DK动态代理小结
现在我们对JDK代理有个简单的源码级别的认识,理清楚一下思路:JDK会帮我们在运行时生成一个代理类,这个代理类实际上就是我们需要代理的接口的实现类。实现的方法里面会调用InvocationHandler类中的invoke方法,并且同时传入自身被调用的方法的的Method对象和参数列表方便我们编码实现方法的调用。比如我们调用reduce方法，那么我们就可以通过Method.Invoke(Object obj, Object... args)调用我们具体的实现类,再在周围做一些代理做的事儿。就实现了动态代理。我们对JDK的特性做一些简单的认识：

JDK动态代理只能代理有接口的类,并且是能代理接口方法,不能代理一般的类中的方法
提供了一个使用InvocationHandler作为参数的构造方法。在代理类中做一层包装,业务逻辑在invoke方法中实现
重写了Object类的equals、hashCode、toString，它们都只是简单的调用了InvocationHandler的invoke方法，即可以对其进行特殊的操作，也就是说JDK的动态代理还可以代理上述三个方法
在invoke方法中我们甚至可以不用Method.invoke方法调用实现类就返回。这种方式常常用在RPC框架中,在invoke方法中发起通信调用远端的接口等

JDK中提供的生成动态代理类的机制有个鲜明的特点是：某个类必须有实现的接口，而生成的代理类也只能代理某个类接口定义的方法。
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
		//利用JDK提供的Proxy实现动态代理,产生实际的实现类
		return Proxy.newProxyInstance(target.getClassLoader(),new Class[]{target},this);
	}
	
	@Override
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		//代理开始
		System.out.println("begin");
		//执行实际的方法
		Object invoke = method.invoke(real,args);
		//代理结束
		System.out.println("end");
		return invoke;
	}
	
	public static void main(String[] args) {
		Calculator proxy = (Calculator)new ProxyFactory(Calculator.class).bind(new Calculator.CalculatorImpl());
		proxy.add(5,6);
	}

}
