package com.zjht.hchpserver;

import java.util.logging.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.zjht.hchpserver.asynserver.AsynSocketServer;

public class AsynSocketListener  implements ServletContextListener {

	private  Logger  logger = Logger.getLogger(AsynSocketListener.class.getName());
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		logger.info("初始化异步I/O服务端。。。开始。。。");
		 AsynSocketServer server = new AsynSocketServer();  
	     server.startServer();  
	     logger.info("初始化异步I/O服务端。。。结束。。。");
	}

}
