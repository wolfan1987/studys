package org.andrewliu.jvm.test;

import java.util.List;
import java.util.ArrayList;

/**
 * -Xms100m -Xmx100m -XX:+UseSerialGC
 * @author AndrewLiu
 *
 */
public class JConsoleMonitorTest {
	
	static class OOMObject{
		public  byte[] placeholder = new byte[64*1024];
	}
	public static void fillHeap(int num) throws InterruptedException{
		List<OOMObject> list = new ArrayList<OOMObject>();
		for(int i=0; i<num; i++){
			//稍作延时，令监视曲线的变化更加明显
			Thread.sleep(50);
			list.add(new OOMObject());
		}
	}
	
	public static void main(String[] args) {
		
		try {
			fillHeap(1500);
			System.gc();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
