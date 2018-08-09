package org.andrewliu.events;

public class DestoryContextListener implements  ContextListener{

	@Override
	public void contextEvent(ContextEvent event) {
		if(event!=null && event.getContextStatus().length()>0){
			String  eventType = event.getContextStatus().trim();
			if(eventType.equalsIgnoreCase("destory")){
				System.out.println("------Context  Ïú»ÙÖĞ......------");
			}
		}
	}

}
