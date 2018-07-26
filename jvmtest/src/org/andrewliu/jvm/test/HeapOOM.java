package org.andrewliu.jvm.test;

import java.util.List;
import java.util.ArrayList;

/**
 * java ���������
 * ����jvm������
 * -Xms20m -Xmx20m  -XX:+HeapDumpOnOutOfMemoryError
 * @author andrewLiu
 *
 */
public class HeapOOM {

	static class OOMObject{
		
	}
	
	public static void main(String[] args) {
		List<OOMObject> list = new ArrayList<OOMObject>();
		while(true){
			
			list.add(new OOMObject());
	
		}
	}
	
}
