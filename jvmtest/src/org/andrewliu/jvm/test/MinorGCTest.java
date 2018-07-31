package org.andrewliu.jvm.test;

/**
 * 新生代Minor GC 测试： 设置堆区最小为20M(Xms)，最大为20M(Xmx),新生代为10M(Xmn)，同时设置，新生代与幸存区(Survivor)的比例是8:1，即新生代总可用
 * 内存为9M，还有1M是用来表示Survivor和from或to区的，这两个区，同一时间只有一个可使用,再通过设置:  PrintGCDetails，来打印在进行Minor GC的日志信息
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
		allocation4 = new byte[4*_1MB];  //这时会出现一次GC
	}
	
	public static void main(String[] args) {
		testAllocation();   
	}

}
