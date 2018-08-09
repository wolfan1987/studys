package org.andrewliu.events;

import java.util.HashSet;
import java.util.Set;

public class ContextManager {

	private  Set<ContextListener>   listeners =  new HashSet<ContextListener>();
	
	/** 
	  * ����¼� 
	  *  
	  * @param listener 
	  *   ContextListener 
	  */  
	 public void addContextListener(ContextListener listener) {  
	  if (listeners != null) {  
		  listeners.add(listener); 
	  }else{
		  throw new  IllegalArgumentException("listener ����Ϊnull");
	  }
	  
	 }  
	   
	 /** 
	  * �Ƴ��¼� 
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
	  * ������ʼ���¼� 
	  */  
	 protected void fireInitEvent() {  
	  if (listeners == null)  
	   return;  
	  ContextEvent event = new ContextEvent(this, "init");  
	  notifyListeners(event);  
	 }  
	   
	 /** 
	  * ��������¼� 
	  */  
	 protected void fireActivit() {  
	  if (listeners == null)  
	   return;  
	  ContextEvent event = new ContextEvent(this, "activit");  
	  notifyListeners(event);  
	 }  
	 /** 
	  * �����������¼� 
	  */  
	 protected void fireDestory() {  
	  if (listeners == null)  
	   return;  
	  ContextEvent event = new ContextEvent(this, "destory");  
	  notifyListeners(event);  
	 }  
	 /** 
	  * ֪ͨ���е�DoorListener 
	  */  
	 private void notifyListeners(ContextEvent event) {  
		 for(ContextListener  listener : listeners){
			 listener.contextEvent(event);  
		 }
	 }  
	
}
