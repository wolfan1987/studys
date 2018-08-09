package org.andrewliu.events;


/**
 * 活动状态容器监听器
 * @author AndrweLiu
 *
 */
public class ActivitContextListener implements  ContextListener{

	@Override
	public void contextEvent(ContextEvent event) {
		if(event!=null && event.getContextStatus().length()>0){
			String  eventType = event.getContextStatus().trim();
			if(eventType.equalsIgnoreCase("activit")){
				System.out.println("------Context  活动中......------");
			}
		}
		
	}

}
