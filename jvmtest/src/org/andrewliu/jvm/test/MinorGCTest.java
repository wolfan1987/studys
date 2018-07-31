package org.andrewliu.jvm.test;

/**
 * ������Minor GC ���ԣ� ���ö�����СΪ20M(Xms)�����Ϊ20M(Xmx),������Ϊ10M(Xmn)��ͬʱ���ã����������Ҵ���(Survivor)�ı�����8:1�����������ܿ���
 * �ڴ�Ϊ9M������1M��������ʾSurvivor��from��to���ģ�����������ͬһʱ��ֻ��һ����ʹ��,��ͨ������:  PrintGCDetails������ӡ�ڽ���Minor GC����־��Ϣ
 * 
 * vmArgs:  -verbose:gc -Xms20M -Xmx20M -Xmn10M -XX:SurvivorRatio=8 -XX:+PrintGCDetails
 * @author AndrewLiu
 *
 */
public class MinorGCTest {
	
	private  static final int _1MB = 1024*1024;
	
	
	public  static  void  testAllocation(){
		byte[] allocation1,allocation2,allocation3,allocation4;
		allocation1 = new byte[2*_1MB];
		allocation2 = new byte[2*_1MB];
		allocation3 = new byte[2*_1MB];
		allocation4 = new byte[4*_1MB];  //��ʱ�����һ��GC
	}
	
	public static void main(String[] args) {
		testAllocation();   
	}

}
