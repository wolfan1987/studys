package org.andrewliu.jvm.test;

/**
 * ���Ե��������ĳ��ֵʱ��������ֱ�ӷ����������������>XX:PretenureSizeThreshold��ֵʱ��������ֱ�ӷ��������
 * vmArgs: -verbose:gc -Xms20M  -Xmx20M  -Xmn10M -XX:SurvivorRatio=8 -XX:PretenureSizeThreshold=3145728
 * @author AndrewLiu
 *
 */
public class LargeObjToOld {
   
	private static final  int _1MB =1024*1024;
	
	public static void testPretenureSizeThreshold(){
		byte[]  allocation;
		allocation = new byte[4*_1MB];  //ֱ�ӷ��䵽�����
		
	}
	
	public static void main(String[] args) {
		testPretenureSizeThreshold();
	}
}
