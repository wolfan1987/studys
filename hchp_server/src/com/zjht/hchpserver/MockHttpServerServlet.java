package com.zjht.hchpserver;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import com.zjht.asyniobiframework.config.AsynIoClientConfig;
import com.zjht.asyniobiframework.context.AsynBIProcessContext;
import com.zjht.asyniobiframework.context.DataTypeEnum;
import com.zjht.asyniobiframework.exception.BIHandlerException;
import com.zjht.asyniobiframework.exception.HandlerRequestException;
import com.zjht.asyniobiframework.exception.MessageTypeRegisterException;
import com.zjht.asyniobiframework.handler.impl.JSONEnDeCoderSendHandler;
import com.zjht.asyniobiframework.message.BaseJsonString;

public class MockHttpServerServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 183659278232916143L;
	
	//private  static  AsynSocketClient   asynSocketClient = new AsynSocketClient();
	
	private  static AsynBIProcessContext  asynContext = null;

	/**
	 * Constructor of the object.
	 */
	public MockHttpServerServlet() {
		super();
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

	/**
	 * The doGet method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
//		System.out.println("进入 doget 方法，处理请求！");
//		response.setContentType("text/html");
//		PrintWriter out = response.getWriter();
//		asynSocketClient.process(request);
//		//asynSocketClient.processRequest(request, response);
//		
//		out.print(" this is  reponse info from doget!");
//		out.flush();   
//		out.close();
	}

	/**
	 * The doPost method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to post.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("进入 dopost 方法 处理请求....");
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();   
		//request.setAttribute("fromServlet", 33333);
		///asynSocketClient.processRequest(request, response);
		try {
			BaseJsonString  json = new BaseJsonString();
			String jsonData = JSONObject.fromObject(json).toString();
			System.out.println("产生的json字符串为:"+jsonData);
			asynContext.acceptRequest(jsonData, BaseJsonString.class);
		} catch (HandlerRequestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("process.last = "+request.getAttribute("fromServlet"));
		out.print("this is response info  from  dopost!");
		
		out.flush();
		out.close();
	}

	/**
	 * Initialization of the servlet. <br>  
	 *
	 * @throws ServletException if an error occurs
	 */
	public void init() throws ServletException {  
		System.out.println("初始化模拟请求Servelt...........");
		try {  
			//初始化异步IO客户端,注册处理的消息类型
			asynContext = AsynBIProcessContext.newAsynBIContext().registerInit(BaseJsonString.class,DataTypeEnum.JSON_DATA);
			//注册的相关handler  
			asynContext.addHandler(BaseJsonString.class,  JSONEnDeCoderSendHandler.class);
			//启动线程        
			asynContext.start();
		} catch (MessageTypeRegisterException e) {
			e.printStackTrace();      
		} catch (BIHandlerException e) {
			e.printStackTrace();
		}
	}
	
	public static void myInit(){
		try {     
			AsynIoClientConfig config = new AsynIoClientConfig(100);
			//初始化异步IO客户端,注册处理的消息类型
			asynContext = AsynBIProcessContext.newAsynBIContext().bindingConfig(config).registerInit(BaseJsonString.class,DataTypeEnum.JSON_DATA);
			//注册的相关handler
			asynContext.addHandler(BaseJsonString.class,JSONEnDeCoderSendHandler.class);
			asynContext.start();      
		} catch (MessageTypeRegisterException e) {
			e.printStackTrace();
		} catch (BIHandlerException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		myInit();
	 new Thread(new Runnable() {
		 BaseJsonString  json = null;
		@Override
		public void run() {
			int i = 0;
			while(i<5000){
			try {   
				  json = new BaseJsonString();
				String jsonData = JSONObject.fromObject(json).toString();
				asynContext.acceptRequest(jsonData, BaseJsonString.class);
				Thread.sleep(1);
				json = null;
			} catch (HandlerRequestException e) {
				e.printStackTrace();
			} 
			catch (InterruptedException e) {
				e.printStackTrace();
			}
			i++;
			}
		}
		}).start();
	 
	 
	 new Thread(new Runnable() {
		 BaseJsonString  json = null;
			@Override
			public void run() {
				int i = 0;
				while(i<5000){
				try {   
					  json = new BaseJsonString();
					String jsonData = JSONObject.fromObject(json).toString();
					asynContext.acceptRequest(jsonData, BaseJsonString.class);
					Thread.sleep(1);
					json = null;
				} catch (HandlerRequestException e) {
					e.printStackTrace();
				} 
				catch (InterruptedException e) {
					e.printStackTrace();
				}
				i++;
				}
			}
		}).start();
	 
	 
	 new Thread(new Runnable() {
		 BaseJsonString  json = null;
			@Override
			public void run() {
				int i = 0;
				while(i<5000){
				try {   
					  json = new BaseJsonString();
					String jsonData = JSONObject.fromObject(json).toString();
					asynContext.acceptRequest(jsonData, BaseJsonString.class);
					Thread.sleep(1);
					json = null;
				} catch (HandlerRequestException e) {
					e.printStackTrace();
				} 
				catch (InterruptedException e) {
					e.printStackTrace();
				}
				i++;
				}
			}
		}).start();
	 
	 
	 
	 new Thread(new Runnable() {
		 BaseJsonString  json = null;
			@Override
			public void run() {
				int i = 0;
				while(i<5000){
				try {   
					  json = new BaseJsonString();
					String jsonData = JSONObject.fromObject(json).toString();
					asynContext.acceptRequest(jsonData, BaseJsonString.class);
					Thread.sleep(1);
					json = null;
				} catch (HandlerRequestException e) {
					e.printStackTrace();
				} 
				catch (InterruptedException e) {
					e.printStackTrace();
				}
				i++;
				}
			}
			}).start();
	 
	 new Thread(new Runnable() {
		 BaseJsonString  json = null;
			@Override
			public void run() {
				int i = 0;
				while(i<5000){
				try {   
					  json = new BaseJsonString();
					String jsonData = JSONObject.fromObject(json).toString();
					asynContext.acceptRequest(jsonData, BaseJsonString.class);
					Thread.sleep(1);
					json = null;
				} catch (HandlerRequestException e) {
					e.printStackTrace();
				} 
				catch (InterruptedException e) {
					e.printStackTrace();
				}
				i++;
				}
			}
			}).start();
	 
	 
	 new Thread(new Runnable() {
		 BaseJsonString  json = null;
			@Override
			public void run() {
				int i = 0;
				while(i<5000){
				try {   
					  json = new BaseJsonString();
					String jsonData = JSONObject.fromObject(json).toString();
					asynContext.acceptRequest(jsonData, BaseJsonString.class);
					Thread.sleep(1);
					json = null;
				} catch (HandlerRequestException e) {
					e.printStackTrace();
				} 
				catch (InterruptedException e) {
					e.printStackTrace();
				}
				i++;
				}
			}
			}).start();
	 
	 
	 new Thread(new Runnable() {
		 BaseJsonString  json = null;
			@Override
			public void run() {
				int i = 0;
				while(i<5000){
				try {   
					  json = new BaseJsonString();
					String jsonData = JSONObject.fromObject(json).toString();
					asynContext.acceptRequest(jsonData, BaseJsonString.class);
					Thread.sleep(1);
					json = null;
				} catch (HandlerRequestException e) {
					e.printStackTrace();
				}    
				catch (InterruptedException e) {
					e.printStackTrace();
				}
				i++;
				}
			}
			}).start();
	}	
	
}
