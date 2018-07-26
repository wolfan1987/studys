package org.andrewliu.proxy.test;

public interface Calculator {
	//需要代理有的接口
	public int add(int a ,int b);
	//接口实现类，执行真正的a+b操作
	public static class CalculatorImpl  implements Calculator{
		@Override
		public int add(int a, int b) {
			return a+b;
		}		
	}
	
	//静态代理类实现
	public static  class  CalculatorProxy implements  Calculator{
		private  Calculator  calculator;
		public  CalculatorProxy(Calculator calculator){
			this.calculator = calculator;
		}
		@Override
		public int add(int a, int b) {
			System.out.println("执行add方法前的操作");
			int result = calculator.add(a,b);
			System.out.println("执行add方法后的操作");
			return result;
		}
	}
}
