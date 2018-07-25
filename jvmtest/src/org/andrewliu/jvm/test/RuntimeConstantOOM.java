package org.andrewliu.jvm.test;

import java.util.ArrayList;
import java.util.List;

/**
 * 测试常量池溢出
 * 设置jvm参数：  -XX:PermSize=10M  -XX:MaxPermSize=10M
 * @author AndrewLiu
 *
 */
public class RuntimeConstantOOM {
	
	public static void main(String[] args) {
		//使用List保持常量池的引用，避免Full GC回收常量池
		List<String>  list = new  ArrayList<String>();
	
		int i = 0;
		while(true){
			list.add(String.valueOf(i++).intern());
		}
	}
	

}
