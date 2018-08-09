package com.zjht.hchpclient;

import java.io.IOException;
import java.util.concurrent.Future;

import org.apache.http.ConnectionReuseStrategy;
import org.apache.http.HttpClientConnection;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.protocol.RequestExpectContinue;
import org.apache.http.impl.DefaultConnectionReuseStrategy;
import org.apache.http.impl.pool.BasicConnFactory;
import org.apache.http.impl.pool.BasicConnPool;
import org.apache.http.impl.pool.BasicPoolEntry;
import org.apache.http.message.BasicHttpRequest;
import org.apache.http.protocol.HttpCoreContext;
import org.apache.http.protocol.HttpProcessor;
import org.apache.http.protocol.HttpProcessorBuilder;
import org.apache.http.protocol.HttpRequestExecutor;
import org.apache.http.protocol.RequestConnControl;
import org.apache.http.protocol.RequestContent;
import org.apache.http.protocol.RequestTargetHost;
import org.apache.http.protocol.RequestUserAgent;
import org.apache.http.util.EntityUtils;

public class PoolingHttpGet {

	 public static void main(String[] args) throws Exception {
	        final HttpProcessor httpproc = HttpProcessorBuilder.create()
	            .add(new RequestContent())
	            .add(new RequestTargetHost())
	            .add(new RequestConnControl())
	            .add(new RequestUserAgent("Test/1.1"))
	            .add(new RequestExpectContinue()).build();

	        final HttpRequestExecutor httpexecutor = new HttpRequestExecutor();

	        final BasicConnPool pool = new BasicConnPool(new BasicConnFactory());
	        pool.setDefaultMaxPerRoute(60);
	        pool.setMaxTotal(50);

	        HttpHost[] targets = new HttpHost[10000];
	        
	        for(int i = 0; i <10000; i++){
	        targets[i] = new HttpHost("localhost", 8080, "http");	
	        }   
	        class WorkerThread extends Thread {
   
	            private final HttpHost target;

	            WorkerThread(final HttpHost target) {
	                super();
	                this.target = target;
	            }

	            @Override
	            public void run() {
	                ConnectionReuseStrategy connStrategy = DefaultConnectionReuseStrategy.INSTANCE;
	                try {   
	                	
	                    Future<BasicPoolEntry> future = pool.lease(this.target, null);

	                    boolean reusable = false;
	                    BasicPoolEntry entry = future.get();
	                    try {
	                        HttpClientConnection conn = entry.getConnection();
	                        HttpCoreContext coreContext = HttpCoreContext.create();
	                        coreContext.setTargetHost(this.target);

	                        BasicHttpRequest request = new BasicHttpRequest("GET", "/hchp_server/servlet/MockHttpServerServlet");
	                        System.out.println(">> Request URI: " + request.getRequestLine().getUri());
   
	                        httpexecutor.preProcess(request, httpproc, coreContext);
	                        HttpResponse response = httpexecutor.execute(request, conn, coreContext);
	                        httpexecutor.postProcess(response, httpproc, coreContext);

	                        System.out.println("<< Response: " + response.getStatusLine());
	                        System.out.println(EntityUtils.toString(response.getEntity()));

	                        reusable = connStrategy.keepAlive(response, coreContext);
	                    } catch (IOException ex) {
	                        throw ex;
	                    } catch (HttpException ex) {
	                        throw ex;
	                    } finally {
	                        if (reusable) {
	                            System.out.println("Connection kept alive...");
	                        }
	                        pool.release(entry, reusable);
	                    }
	                } catch (Exception ex) {
	                    System.out.println("Request to " + this.target + " failed: " + ex.getMessage());
	                }
	            }   

	        };

	        WorkerThread[] workers = new WorkerThread[targets.length];
	        for (int i = 0; i < workers.length; i++) {
	            workers[i] = new WorkerThread(targets[i]);
	        }
	        for (int i = 0; i < workers.length; i++) {
	            workers[i].start();
	        }
	        for (int i = 0; i < workers.length; i++) {
	            workers[i].join();
	        }
	    }
	
}
