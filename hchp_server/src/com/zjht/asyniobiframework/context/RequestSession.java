package com.zjht.asyniobiframework.context;

import java.util.Date;
import java.util.UUID;

public class RequestSession {

	
	public RequestSession() {
		super();
		this.sessionId = UUID.randomUUID().toString();
		this.reqTime = new Date();
	}

	private  String sessionId;
	/**
	 * 请求时间
	 */
	private  Date   reqTime;
	/**
	 * 响应时间
	 */
	private  Date   respTime;
	/**
	 * 请求路径
	 */
	private  String reqPath;
	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public Date getReqTime() {
		return reqTime;
	}

	public void setReqTime(Date reqTime) {
		this.reqTime = reqTime;
	}

	public Date getRespTime() {
		return respTime;
	}

	public void setRespTime(Date respTime) {
		this.respTime = respTime;
	}

	public String getReqPath() {
		return reqPath;
	}

	public void setReqPath(String reqPath) {
		this.reqPath = reqPath;
	}
	
}
