package org.andrewliu.jvm.test;

/**
 * 测试当对象大于某个值时，将对象直接放入老年代。当对象>XX:PretenureSizeThreshold的值时，将对象直接放入老年代
 * vmArgs: -verbose:gc -Xms20M  -Xmx20M  -Xmn10M -XX:SurvivorRatio=8 -XX:PretenureSizeThreshold=3145728
 * @author AndrewLiu
 *
 */
public class LargeObjToOld {
   
	private static final  int _1MB =1024*1024;
	
	public static void testPretenureSizeThreshold(){
		byte[]  allocation;
		allocation = new byte[4*_1MB];  //直接分配到老年代
		
	}
	
	public static void main(String[] args) {
		testPretenureSizeThreshold();
	}
}
