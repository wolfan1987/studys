package org.andrewliu.javanet.security.ssl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;

/**
 * 单向SSL验证，服务端只要加载自己的相关密钥即可，即key开头的相关变量和文件,它不需要信任客户端，而是客户端要确认服务端
 * @author de
 *
 */
public class SingleSSLServerApp {

	/**
	 * 服务器商授权的用户登录的身份帐号和对应的密码，实际系统则来自于数据库系统
	 */
	private String loginName = "andrewliu";
	private String loginPassWord = "123456";
	/**
	 * 服务器端监听的TCP端口号
	 */
	private int serverPort = 8888;
	/**
	 * 服务器端保密内容
	 */
	private static final String messageToClient = "您好的银行卡号为：6225112378911234";
	private SSLServerSocket serverSocket = null;
	
	private SingleSSLServerApp(){
		/**
		 * 清除以前的java系统的环境变量的值
		 */
		System.clearProperty("javax.net.ssl.keyStore");
		System.clearProperty("javax.net.ssl.keyStorePassword");
		System.clearProperty("javax.net.ssl.keyStoreType");
//		System.clearProperty("javax.net.ssl.trustStore");
//		System.clearProperty("javax.net.ssl.trustStorePassword");
//		System.clearProperty("javax.net.ssl.trustStoreType");
		/**
		 * 重新设置java系统的环境变量的值，其中的keyStore存储服务器端程序本身的私钥，用于表明自己的
		 * 身份，而trustStore存储服务器端程序可以信任的其它的人或者组织的公钥--本示例为客户端程序的公钥）
		 */
		System.setProperty("javax.net.ssl.keyStore","F:\\PosWorkspace\\netsecurity\\mykeystores\\server.keystore");
		System.setProperty("javax.net.ssl.keyStorePassword","123456");
//		System.setProperty("javax.net.ssl.trustStore","F:\\PosWorkspace\\netsecurity\\mykeystores\\servertrust.keystore");
//		System.setProperty("javax.net.ssl.trustStorePassword","123456");
		/**
		 * 本示例的数字证书库的类型为JKS,如果为PKCS#12，则设置为PKCS12
		 */
		System.setProperty("javax.net.ssl.keyStoreType","JKS"); //PKCS12
		//System.setProperty("javax.net.ssl.trustStoreType","JKS");
		
		/**
		 * 通过套接字工厂，获取一个服务器端套接字
		 */
		SSLServerSocketFactory socketFactory = (SSLServerSocketFactory)SSLServerSocketFactory.getDefault();
		
		/**
		 * 下面的serverPort变量代表服务器端监听的TCP端口号（本示例的服务器的TCP端口号为1234)；如果连接
		 * Tomcat,则为8443
		 */
		
		try {
			serverSocket = (SSLServerSocket)socketFactory.createServerSocket(serverPort);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void listenClientRequest(){
		while(true){
			try {
			System.out.println("服务器已经准备完毕，可以接收客户端的连接请求！");
			/**
			 * 服务器端套接字进入阻塞状态，等待来自客户端的连接请求
			 */
				SSLSocket oneClientSSLSocket = (SSLSocket)serverSocket.accept();
				/**
				 * 获取服务器端套接字的输入流，以便能够获得客户羰请求的数据
				 */
				BufferedReader oneBufferedReader = new BufferedReader(new InputStreamReader(oneClientSSLSocket.getInputStream()));
				/**
				 * 从输入流中读取客户端请求中的登录的帐号和密码
				 */
				String requestLoginName = oneBufferedReader.readLine();
				String requestPassWord = oneBufferedReader.readLine();
				/**
				 * 获取服务器端套接字的输出流，以便能够向客户端发送数据
				 */
				PrintWriter onePrintWriter = new PrintWriter(new OutputStreamWriter(oneClientSSLSocket.getOutputStream()));
				/**
				 * 对请求的客户羰的用户进行应用的身份检查，如果通过刚向客户羰程序发送信息
				 * 该信息将应用SSL加密处理
				 */
				if(requestLoginName.equals(loginName) && requestPassWord.equals(loginPassWord)){
					onePrintWriter.println("欢迎，"+requestLoginName+" ! 下面为来自服务器通过SSL加密的信息：");
					onePrintWriter.println(messageToClient);
				}else{
					onePrintWriter.println(requestLoginName+", 您的身份检查没有通过，请重新进行身份验证。");
				}
				/**
				 * 关闭输入和输出流对象和相关的资源，套接字资源
				 */
				onePrintWriter.close();
				oneBufferedReader.close();
				oneClientSSLSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
	}
	
	
	public static void main(String[] args) {
		SingleSSLServerApp server = new SingleSSLServerApp();
		server.listenClientRequest();
	}
	
}
