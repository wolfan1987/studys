package com.zjht.asyniobiframework.message;

public abstract class AbstractRequestMessage  implements IMessage<AbstractRequestMessage> {

	protected  String  msgSeq;
	
	protected  String  msgType;
	
	

	public String getMsgSeq() {
		return msgSeq;
	}

	public void setMsgSeq(String msgSeq) {
		this.msgSeq = msgSeq;
	}

	public String getMsgType() {
		return msgType;
	}

	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}
	
	
	
	
	
}
