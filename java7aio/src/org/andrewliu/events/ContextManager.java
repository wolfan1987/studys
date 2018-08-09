package org.andrewliu.events;

import java.util.HashSet;
import java.util.Set;

public class ContextManager {

	private  Set<ContextListener>   listeners =  new HashSet<ContextListener>();
	
	/** 
	  * 添加事件 
	  *  
	  * @param listener 
	  *   ContextListener 
	  */  
	 public void addContextListener(ContextListener listener) {  
	  if (listeners != null) {  
		  listeners.add(listener); 
	  }else{
		  throw new  IllegalArgumentException("listener 不能为null");
	  }
	  
	 }  
	   
	 /** 
	  * 移除事件 
	  *  
	  * @param listener 
	  *   DoorListener 
	  */  
	 public void removeContextListener(ContextListener listener) {  
	  if (listeners == null)  
	   return;  
	  listeners.remove(listener);  
	 }  
	   
	 /** 
	  * 触发初始化事件 
	  */  
	 protected void fireInitEvent() {  
	  if (listeners == null)  
	   return;  
	  ContextEvent event = new ContextEvent(this, "init");  
	  notifyListeners(event);  
	 }  
	   
	 /** 
	  * 触发活动中事件 
	  */  
	 protected void fireActivit() {  
	  if (listeners == null)  
	   return;  
	  ContextEvent event = new ContextEvent(this, "activit");  
	  notifyListeners(event);  
	 }  
	 /** 
	  * 触发销毁中事件 
	  */  
	 protected void fireDestory() {  
	  if (listeners == null)  
	   return;  
	  ContextEvent event = new ContextEvent(this, "destory");  
	  notifyListeners(event);  
	 }  
	 /** 
	  * 通知所有的DoorListener 
	  */  
	 private void notifyListeners(ContextEvent event) {  
		 for(ContextListener  listener : listeners){
			 listener.contextEvent(event);  
		 }
	 }  
	
}
