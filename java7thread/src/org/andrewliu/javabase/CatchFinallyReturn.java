package org.andrewliu.javabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class CatchFinallyReturn {

	
	public static void main(String[] args) {
		CatchFinallyReturn  cfr = new CatchFinallyReturn();
		System.out.println(cfr.test());
	}
	
	public  int test(){
		String abc = "dad";
		
		SimpleDateFormat  sd = new SimpleDateFormat();
		try {
			sd.parse(abc);
		} catch (ParseException e) {
			e.printStackTrace();
			return 1;
		}finally{
			return 2;
		}
	}
}
