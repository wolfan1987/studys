package org.andrewliu.proxy.test;

public class StaticProxyTest {
	
	public static void main(String[] args) {
		Calculator  calcProxy = new  Calculator.CalculatorProxy(new  Calculator.CalculatorImpl());
		int result = calcProxy.add(3,4);
		System.out.println("result="+result);
	}

}
