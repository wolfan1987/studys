package com.zjht.asyniobiframework.callback;

import org.apache.commons.pool2.impl.GenericObjectPool;

import com.zjht.asyniobiframework.context.RequestSession;
import com.zjht.asyniobiframework.pools.AbstractAsynIOClientThread;
import com.zjht.asyniobiframework.pools.AsynIOClientJsonThread;
import com.zjht.asyniobiframework.pools.AsynIOClientMessageThread;

/**
 * 读取完数据后回调清理相关动作封装
 * @author de
 *
 */
public class ReadedClearCallback {

	private GenericObjectPool<AsynIOClientJsonThread>  jsonIoThreadPool;
	private GenericObjectPool<AsynIOClientMessageThread>  msgIoThreadPool;
	private AsynIOClientJsonThread     borrowObjectJson;
	private AsynIOClientMessageThread  borrowObjectMsg;
	private RequestSession         requestSession;
	
	public ReadedClearCallback(
			GenericObjectPool<AsynIOClientJsonThread> jsonIoThreadPool,
			AsynIOClientJsonThread borrowObjectJson, RequestSession requestSession) {
		super();    
		this.jsonIoThreadPool = jsonIoThreadPool;
		this.borrowObjectJson = borrowObjectJson;
		this.requestSession = requestSession;
	}

	public ReadedClearCallback(RequestSession requestSession,
			GenericObjectPool<AsynIOClientMessageThread> msgIoThreadPool,
			AsynIOClientMessageThread borrowObjectMsg) {
		super();    
		this.msgIoThreadPool = msgIoThreadPool;
		this.borrowObjectMsg = borrowObjectMsg;
		this.requestSession = requestSession;
	}
	
	public GenericObjectPool<AsynIOClientJsonThread> getIoThreadPool() {
		return jsonIoThreadPool;
	}

	public GenericObjectPool<AsynIOClientJsonThread> getJsonIoThreadPool() {
		return jsonIoThreadPool;
	}

	public GenericObjectPool<AsynIOClientMessageThread> getMsgIoThreadPool() {
		return msgIoThreadPool;
	}

	public AsynIOClientJsonThread getBorrowObjectJson() {
		return borrowObjectJson;
	}

	public AsynIOClientMessageThread getBorrowObjectMsg() {
		return borrowObjectMsg;
	}

	public RequestSession getRequestSession() {
		return requestSession;
	}
	
	
}
