package org.andrewliu.events;

/**
 * 容器初始化事件监听器
 * @author AndrewLiu
 *
 */
public class InitContextListener  implements ContextListener{

	@Override
	public void contextEvent(ContextEvent event) {
		if(event!=null && event.getContextStatus().length()>0){
			String  eventType = event.getContextStatus().trim();
			if(eventType.equalsIgnoreCase("init")){
				System.out.println("------Context  初始化中......------");
			}
		}
	}

}
