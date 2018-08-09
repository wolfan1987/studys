package com.anmf.exception;

import java.util.Scanner;

/**
 * 此类为系统异常
 * 
 * @author Administrator
 * 
 */
public class ANMFException extends Exception {
	private Exception exception;

	public ANMFException(Exception exception) {
		this.exception = exception;
	}

	public ANMFException(Exception exception, String message) {
		super(message);
		this.exception = exception;
	}

	public ANMFException(String message) {
		super(message);
	}

	public Exception getException() {
		return exception;
	}

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
        System.out.println("请输入内容【输入Exit退出程序】：");
        String  content="";
        while(true){
        	content = sc.nextLine();
        	if(!(content!=null && content.length()>0)){
        		continue;
        	}
        	if(!content.equals("abc")&&(!content.equals("Exit"))){
        		System.out.println("你输入的是："+content);
        	}else if(content.equals("Exit")){
        		System.exit(0);
        	}else if(content.equals("abc")){
        		try {
					throw  new ANMFException("abc 异常");
				} catch (ANMFException e) {
					e.printStackTrace();
				}
        	}
        	continue;
        }
	}
	
}
