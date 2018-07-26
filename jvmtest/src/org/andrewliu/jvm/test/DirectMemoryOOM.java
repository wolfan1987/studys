package org.andrewliu.jvm.test;

import java.lang.reflect.Field;

import sun.misc.Unsafe;

/**
 * ͨ���ֶ�ָ������ֱ���ڴ棬�������ڴ����
 * vmArgs: -Xmx20M  -XX:MaxDirectMemorySize=10M
 * @author AndrewLiu
 *
 */
public class DirectMemoryOOM {

	private  static final  int _1MB = 1024 * 1024;
	
	public static void main(String[] args) throws Exception {
		Field unsafeField = Unsafe.class.getDeclaredFields()[0];
		unsafeField.setAccessible(true);
		Unsafe unsafe = (Unsafe)unsafeField.get(null);
		while(true){
			unsafe.allocateMemory(_1MB);
		}
	}

}
