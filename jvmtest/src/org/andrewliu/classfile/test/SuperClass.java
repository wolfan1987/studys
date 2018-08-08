package org.andrewliu.classfile.test;

/**
 * 被动使用类字段演示一：
 * 通过子类引用父类的静态字段，不会导致导致子类初始化。
 * @author zjhtadmin
 *
 */
public class SuperClass {
	static {
		System.out.println("SuperClass init!");
	}
	
	public static int value = 123;
}
