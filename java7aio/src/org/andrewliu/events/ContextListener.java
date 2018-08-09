package org.andrewliu.events;

import java.util.EventListener;

/**
 * ÈİÆ÷¼àÌıÆ÷½Ó¿Ú,¸ºÔğ¼àÌıÈİÆ÷×´Ì¬
 * @author AndrewLiu
 *
 */
public interface ContextListener extends  EventListener {

	void   contextEvent(ContextEvent  event);
}
