package org.andrewliu.events;

public class Test {
	
	public static void main(String[] args) {
		ContextManager  cm = new  ContextManager();
		cm.addContextListener(new InitContextListener());
		cm.addContextListener(new ActivitContextListener());
		cm.addContextListener(new  DestoryContextListener());
		
		cm.fireInitEvent();
		cm.fireActivit();
		cm.fireDestory();
	}

}
