package org.andrewliu.classfile.test;

/**
 * ������ʹ�����ֶ���ʾ
 * -XX:+TraceClassLoading
 * @author zjhtadmin
 *
 */
public class NotInitialization {
	public static void main(String[] args) {
		//�˴�ֻ���������ľ�̬�ֶ�ֵ�������������ľ�̬�ֶ�ֵ,��Ϊֻ����ظ��࣬�������ʼ������
		//System.out.println(SubClass.value);  
		//ͨ�����鶨���������࣬���ᴥ������ĳ�ʼ�������Բ����ӡSuperClass init!
		//SuperClass[]  sca = new SuperClass[10];
		 System.out.println(ConstClass.HELLOWORLD);
	}
}
