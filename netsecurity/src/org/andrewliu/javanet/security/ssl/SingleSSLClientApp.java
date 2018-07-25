package org.andrewliu.javanet.security.ssl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.UnknownHostException;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

/**
 * 单向SSL验证实现,客户端只要添加受信任的证书库即可，即客户端client.trust...的key和文件即可,
 * 这样客户端就可拿出相关密钥发送到服务端去进行认证
 * @author de
 *
 */
public class SingleSSLClientApp {
	private SSLSocket clientSSLSocket = null;
	private int serverPort = 8888;
	public SingleSSLClientApp(){
		/**
		 * 清除以前的Java系统的环境变量
		 */
//		System.clearProperty("javax.net.ssl.keyStore");
//		System.clearProperty("javax.net.ssl.keyStorePassword");
//		System.clearProperty("javax.net.ssl.keyStoreType");
		System.clearProperty("javax.net.ssl.trustStore");
		System.clearProperty("javax.net.ssl.trustStorePassword");
		System.clearProperty("javax.net.ssl.trustStoreType");
		
		/**
		 * 设置Java系统的环境变量的值，其中的keyStore存储客户端程序本身的私钥，用于表明自己的身份，
		 * 而trustStore存储客户端程序可以信任的其它的人或者组织的公钥---本示例为服务器端程序的公钥）；
		 */
//		System.setProperty("javax.net.ssl.keyStore", "F:\\PosWorkspace\\netsecurity\\mykeystores\\client.keystore");
//		System.setProperty("javax.net.ssl.keyStorePassword", "123456");
		System.setProperty("javax.net.ssl.trustStore", "F:\\PosWorkspace\\netsecurity\\mykeystores\\clienttrust.keystore");
		System.setProperty("javax.net.ssl.trustStorePassword", "123456");
		/**  
		 * 本示例的数字证书库的类型为JKS，如果为PKCS#12，则设置为PKCS12
		 */
	//	System.setProperty("javax.net.ssl.keyStoreType", "JKS"); //PKCS12
		System.setProperty("javax.net.ssl.trustStoreType", "JKS");
		
		/**
		 * 通过套接字工厂，获取一个客户端套接字
		 */
		SSLSocketFactory sslSocketFactory = (SSLSocketFactory)SSLSocketFactory.getDefault();
		
		/**
		 * 下面的serverPort变量代表服务器监听的TCP端口号（本示例的服务器的TCP端口号为1234，如果连接
		 * tomcat服务器，则为8443
		 */
		try {
			clientSSLSocket = (SSLSocket)sslSocketFactory.createSocket("172.16.104.15", serverPort);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void connectToServer(){
		try{
			/**
			 * 获取客户端套接字输出流，从而可以向服务器发送请求信息
			 */
			PrintWriter onePrintWriter = new PrintWriter(new OutputStreamWriter(clientSSLSocket.getOutputStream()));
			
			/**
			 * 将登录身份帐号和对应的密码通过客户端的输出流发送到服务器端
			 */
			String loginName = "andrewliu";
			String loginPassWord = "123456";
			onePrintWriter.println(loginName);
			onePrintWriter.println(loginPassWord);
			onePrintWriter.flush();
			/**
			 * 获取客户端套接字输入流，从而可以接收来自于服务器发送返回的请求响应处理的结果信息
			 */
			BufferedReader oneBufferedReader = new BufferedReader(new InputStreamReader(clientSSLSocket.getInputStream()));
			/**
			 * 从输入流中读取服务器端发送返回的每条结果信息，并识别信息是否已经接收完毕
			 */
			String oneResponseResultFromServer = "";
			String totalResponseResultFromServer = "";
			while((oneResponseResultFromServer = oneBufferedReader.readLine())!=null){
				/**
				 * 将所获得的来自服务器端发送返回的每条结果信息组合为一个完整的信息字符串，每行添加控制
				 * 换行 符
				 */
				totalResponseResultFromServer = totalResponseResultFromServer+oneResponseResultFromServer+"\n";
			}
			/**
			 * 在控制台中打印显示出所获得的各个结果信息单
			 */
			System.out.println(totalResponseResultFromServer);
			/**
			 * 关闭输入和输出流对象和相关的资源、套接字资源
			 */
			onePrintWriter.close();
			oneBufferedReader.close();
			clientSSLSocket.close();
		}catch(IOException exception){
			exception.printStackTrace();
		}
	}
	
	
	
	public static void main(String[] args) {
		SingleSSLClientApp sslClientApp = new SingleSSLClientApp();
		sslClientApp.connectToServer();
	}
	
	
	
}
