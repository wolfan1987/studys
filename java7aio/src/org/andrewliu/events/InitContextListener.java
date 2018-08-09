package org.andrewliu.events;

/**
 * ������ʼ���¼�������
 * @author AndrewLiu
 *
 */
public class InitContextListener  implements ContextListener{

	@Override
	public void contextEvent(ContextEvent event) {
		if(event!=null && event.getContextStatus().length()>0){
			String  eventType = event.getContextStatus().trim();
			if(eventType.equalsIgnoreCase("init")){
				System.out.println("------Context  ��ʼ����......------");
			}
		}
	}

}
