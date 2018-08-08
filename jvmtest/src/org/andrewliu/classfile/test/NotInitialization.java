package org.andrewliu.classfile.test;

/**
 * 非主动使用类字段演示
 * -XX:+TraceClassLoading
 * @author zjhtadmin
 *
 */
public class NotInitialization {
	public static void main(String[] args) {
		//此处只会输出父类的静态字段值，不会输出子类的静态字段值,因为只会加载父类，但不会初始化父类
		//System.out.println(SubClass.value);  
		//通过数组定义来引用类，不会触发此类的初始化，所以不会打印SuperClass init!
		//SuperClass[]  sca = new SuperClass[10];
		 System.out.println(ConstClass.HELLOWORLD);
	}
}
