package org.andrewliu.events;

import java.util.EventObject;

/**
 * �����¼�
 * @author andrewLiu
 *
 */
public class ContextEvent extends EventObject{

	private static final long serialVersionUID = -6122042271236821048L;
	
	private  String  contextStatus = ""; //��ʾ����״̬
	
	public ContextEvent(Object source,String contextStatus) {
		super(source);
		this.contextStatus = contextStatus;
	}

	public String getContextStatus() {
		return contextStatus;
	}

	public void setContextStatus(String contextStatus) {
		this.contextStatus = contextStatus;
	}
	
}
