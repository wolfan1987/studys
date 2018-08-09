package com.zjht.hchpclient;

import java.io.ByteArrayInputStream;
import java.net.Socket;

import org.apache.http.ConnectionReuseStrategy;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.protocol.RequestExpectContinue;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.DefaultBHttpClientConnection;
import org.apache.http.impl.DefaultConnectionReuseStrategy;
import org.apache.http.message.BasicHttpEntityEnclosingRequest;
import org.apache.http.protocol.HttpCoreContext;
import org.apache.http.protocol.HttpProcessor;
import org.apache.http.protocol.HttpProcessorBuilder;
import org.apache.http.protocol.HttpRequestExecutor;
import org.apache.http.protocol.RequestConnControl;
import org.apache.http.protocol.RequestContent;
import org.apache.http.protocol.RequestTargetHost;
import org.apache.http.protocol.RequestUserAgent;
import org.apache.http.util.EntityUtils;

public class HttpPost {

	 public static void main(String[] args) throws Exception {
	        HttpProcessor httpproc = HttpProcessorBuilder.create()
	            .add(new RequestContent())
	            .add(new RequestTargetHost())
	            .add(new RequestConnControl())
	            .add(new RequestUserAgent("Test/1.1"))
	            .add(new RequestExpectContinue()).build();
	        

	        HttpRequestExecutor httpexecutor = new HttpRequestExecutor();

	        HttpCoreContext coreContext = HttpCoreContext.create();
	        HttpHost host = new HttpHost("localhost", 8080);
	        coreContext.setTargetHost(host);

	        DefaultBHttpClientConnection conn = new DefaultBHttpClientConnection(8 * 1024);
	        ConnectionReuseStrategy connStrategy = DefaultConnectionReuseStrategy.INSTANCE;

	        try {

	            HttpEntity[] requestBodies = new HttpEntity[2];
	            for(int j = 0; j < 2; j++){
	            	if(j%2==0){
	            		requestBodies[j] =  new StringEntity(
	                            "This is the first test request",
	                            ContentType.create("text/plain", Consts.UTF_8));
	            	}else{
	            		requestBodies[j]= new ByteArrayEntity(
	                             "This is the second test request".getBytes(Consts.UTF_8),
	                             ContentType.APPLICATION_OCTET_STREAM);
	            	}
	            	
	            }
	            long startTime = System.currentTimeMillis();
	            for (int i = 0; i < requestBodies.length; i++) {
	                if (!conn.isOpen()) {
	                    Socket socket = new Socket(host.getHostName(), host.getPort());
	                    conn.bind(socket);
	                }
	                BasicHttpEntityEnclosingRequest request = new BasicHttpEntityEnclosingRequest("POST",
	                        "/hchp_server/servlet/MockHttpServerServlet");
	                request.setEntity(requestBodies[i]);
	                System.out.println(">> Request URI: " + request.getRequestLine().getUri());

	                httpexecutor.preProcess(request, httpproc, coreContext);
	                HttpResponse response = httpexecutor.execute(request, conn, coreContext);
	                httpexecutor.postProcess(response, httpproc, coreContext);

	                System.out.println("<< Response: " + response.getStatusLine());
	                System.out.println(EntityUtils.toString(response.getEntity()));
	                System.out.println("==============");
	                if (!connStrategy.keepAlive(response, coreContext)) {
	                    conn.close();
	                } else {
	                    System.out.println("Connection kept alive...");
	                }
	                System.out.println("第  "+i+"  个Http请求！");
	            }
	            long endTime = System.currentTimeMillis();
	            System.out.println("time.count="+(endTime - startTime));
	        } finally {
	            conn.close();
	        }
	    }
}
