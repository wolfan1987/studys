package org.andrewliu.events;

import java.util.EventListener;

/**
 * �����������ӿ�,�����������״̬
 * @author AndrewLiu
 *
 */
public interface ContextListener extends  EventListener {

	void   contextEvent(ContextEvent  event);
}
