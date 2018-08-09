package com.zjht.asyniobiframework.message;

import java.util.Random;
import java.util.UUID;

public class BaseJsonString {
	
	
	private static  Random rand = new Random();
	public BaseJsonString() {
		super();
		this.sessioinId = UUID.randomUUID().toString();
		this.id = this.sessioinId.substring(1, 10);
		this.userName = "userName:"+sessioinId.substring(10, 16);
		this.age = rand.nextInt();
		this.socre = rand.nextDouble();
		this.remarks = this.id+this.userName+this.socre+"中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通中经汇通";
	}

	private String id;
	private String userName;
	private String sessioinId;
	private int age;
	private double socre;
	private String remarks;
//	/**
//	 * 消息要到达的目的IP地址
//	 */
//	private String  destIpAddress;
//	/**
//	 *消息要到达的目的IP应用程序的端口
//	 */
//	private int     destPort;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getSessioinId() {
		return sessioinId;
	}

	public void setSessioinId(String sessioinId) {
		this.sessioinId = sessioinId;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public double getSocre() {
		return socre;
	}

	public void setSocre(double socre) {
		this.socre = socre;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

//	public String getDestIpAddress() {
//		return destIpAddress;
//	}
//
//	public void setDestIpAddress(String destIpAddress) {
//		this.destIpAddress = destIpAddress;
//	}
//
//	public int getDestPort() {
//		return destPort;
//	}  
//
//	public void setDestPort(int destPort) {
//		this.destPort = destPort;
//	}
	
	

}
