package org.andrewliu.proxy.test;

public interface Calculator {
	//��Ҫ�����еĽӿ�
	public int add(int a ,int b);
	//�ӿ�ʵ���ִ࣬��������a+b����
	public static class CalculatorImpl  implements Calculator{
		@Override
		public int add(int a, int b) {
			return a+b;
		}		
	}
	
	//��̬������ʵ��
	public static  class  CalculatorProxy implements  Calculator{
		private  Calculator  calculator;
		public  CalculatorProxy(Calculator calculator){
			this.calculator = calculator;
		}
		@Override
		public int add(int a, int b) {
			System.out.println("ִ��add����ǰ�Ĳ���");
			int result = calculator.add(a,b);
			System.out.println("ִ��add������Ĳ���");
			return result;
		}
	}
}
