package com.zjht.hchpserver.poolsconnect;

import java.nio.channels.AsynchronousSocketChannel;

public class AsynSocketConnectWrap {

	private  long  checkOutTime = 0;
	private  long  useEndTime = 0;
	private  long  lostReqSessionTime = 0;
	private   AsynchronousSocketChannel socketChannel;
	
	public long getCheckOutTime() {
		return checkOutTime;
	}
	public void setCheckOutTime(long checkOutTime) {
		this.checkOutTime = checkOutTime;
	}
	public long getUseEndTime() {
		return useEndTime;
	}
	public void setUseEndTime(long useEndTime) {
		this.useEndTime = useEndTime;
	}
	public long getLostReqSessionTime() {
		return lostReqSessionTime;
	}
	public void setLostReqSessionTime(long lostReqSessionTime) {
		this.lostReqSessionTime = lostReqSessionTime;
	}
	public AsynchronousSocketChannel getSocketChannel() {
		return socketChannel;
	}
	public void setSocketChannel(AsynchronousSocketChannel socketChannel) {
		this.socketChannel = socketChannel;
	}
	
	
	
}
