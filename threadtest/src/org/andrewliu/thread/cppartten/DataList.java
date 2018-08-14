package org.andrewliu.thread.cppartten;

import java.util.ArrayList;
import java.util.List;

public class DataList {
	private  static List<String>  dataList = new ArrayList<String>();
	
	public  static void  addData(){
		dataList.add("addData0000000");
	}
	
	public  static int  size(){
		return dataList.size();
	}

}
