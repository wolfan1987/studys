package org.andrewliu.jvm.test;

import java.util.ArrayList;
import java.util.List;

/**
 * ���Գ��������
 * ����jvm������  -XX:PermSize=10M  -XX:MaxPermSize=10M
 * @author AndrewLiu
 *
 */
public class RuntimeConstantOOM {
	
	public static void main(String[] args) {
		//ʹ��List���ֳ����ص����ã�����Full GC���ճ�����
		List<String>  list = new  ArrayList<String>();
	
		int i = 0;
		while(true){
			list.add(String.valueOf(i++).intern());
		}
	}
	

}
