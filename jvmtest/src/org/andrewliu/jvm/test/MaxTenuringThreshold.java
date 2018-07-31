package org.andrewliu.jvm.test;

/**
 * ����������䷧ֵ
 * vmArgs:  -verbose:gc -Xms20m -Xmx20M -Xmn10M -XX:SurvivorRatio=8
 * -XX:MaxTenuringThreshold=1 -XX:+PrintTenuringDistribution
 * @author zjhtadmin
 *
 */
public class MaxTenuringThreshold {
	
	private  static final int _1MB = 1024 * 1024;
	
	public static void testTenuringThreshold(){
		byte[]  allocation1,allocation2,allocation3;
		allocation1 = new byte[_1MB/4];
		//ʲôʱ����������ȡ����XX:MaxTenuringThreshold����
		allocation2 = new byte[4*_1MB];
		allocation3 = new byte[4*_1MB];
		allocation3 = null;
		allocation3 = new byte[4*_1MB];
	}
	
	public static void main(String[] args) {
		testTenuringThreshold();
	}
	
	
	

}
