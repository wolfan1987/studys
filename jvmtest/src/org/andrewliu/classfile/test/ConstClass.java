package org.andrewliu.classfile.test;

/**
 * ����ʹ�����ֶ���ʾ����
 * �����ڱ���׶λ���������ĳ������У�������û��ֱ�����õ����峣�����࣬
 * ��˲��ᴥ����������ĳ�ʼ����
 * @author zjhtadmin
 *
 */
public class ConstClass {

	static {
		System.out.println("ConstClass init!");
	}
	
	public static final String HELLOWORLD="hello world";
}
