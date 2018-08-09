package com.zjht.hchpclient;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.util.concurrent.CountDownLatch;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.protocol.RequestExpectContinue;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.impl.nio.DefaultHttpClientIODispatch;
import org.apache.http.impl.nio.pool.BasicNIOConnPool;
import org.apache.http.impl.nio.reactor.DefaultConnectingIOReactor;
import org.apache.http.message.BasicHttpRequest;
import org.apache.http.nio.protocol.BasicAsyncRequestProducer;
import org.apache.http.nio.protocol.BasicAsyncResponseConsumer;
import org.apache.http.nio.protocol.HttpAsyncRequestExecutor;
import org.apache.http.nio.protocol.HttpAsyncRequester;
import org.apache.http.nio.reactor.ConnectingIOReactor;
import org.apache.http.nio.reactor.IOEventDispatch;
import org.apache.http.protocol.HttpCoreContext;
import org.apache.http.protocol.HttpProcessor;
import org.apache.http.protocol.HttpProcessorBuilder;
import org.apache.http.protocol.RequestConnControl;
import org.apache.http.protocol.RequestContent;
import org.apache.http.protocol.RequestTargetHost;
import org.apache.http.protocol.RequestUserAgent;
import org.apache.http.util.EntityUtils;

/**
 * 异步Http请求
 * @author de
 *
 */
public class NHttpClient {

	public static void main(String[] args) throws Exception {
        // Create HTTP protocol processing chain
        HttpProcessor httpproc = HttpProcessorBuilder.create()
                // Use standard client-side protocol interceptors
                .add(new RequestContent())
                .add(new RequestTargetHost())
                .add(new RequestConnControl())
                .add(new RequestUserAgent("Test/1.1"))
                .add(new RequestExpectContinue()).build();
        // Create client-side HTTP protocol handler
        HttpAsyncRequestExecutor protocolHandler = new HttpAsyncRequestExecutor();
        // Create client-side I/O event dispatch
        final IOEventDispatch ioEventDispatch = new DefaultHttpClientIODispatch(protocolHandler,
                ConnectionConfig.DEFAULT);
        // Create client-side I/O reactor
        final ConnectingIOReactor ioReactor = new DefaultConnectingIOReactor();
        // Create HTTP connection pool
        BasicNIOConnPool pool = new BasicNIOConnPool(ioReactor, ConnectionConfig.DEFAULT);
        // Limit total number of connections to just two
        pool.setDefaultMaxPerRoute(2);
        pool.setMaxTotal(2);
        // Run the I/O reactor in a separate thread
        Thread t = new Thread(new Runnable() {

            public void run() {
                try {
                    // Ready to go!
                    ioReactor.execute(ioEventDispatch);
                } catch (InterruptedIOException ex) {
                    System.err.println("Interrupted");
                } catch (IOException e) {
                    System.err.println("I/O error: " + e.getMessage());
                }
                System.out.println("Shutdown");
            }

        });
        // Start the client thread
        t.start();
        // Create HTTP requester
        HttpAsyncRequester requester = new HttpAsyncRequester(httpproc);
        // Execute HTTP GETs to the following hosts and
        HttpHost[] targets = new HttpHost[] {
                new HttpHost("localhost", 8080, "http"),
                new HttpHost("localhost", 8080, "http"),
                new HttpHost("localhost", 8080, "http")
        };
        final CountDownLatch latch = new CountDownLatch(targets.length);
        for (final HttpHost target: targets) {
            BasicHttpRequest request = new BasicHttpRequest("GET", "/hchp_server/servlet/MockHttpServerServlet");
            HttpCoreContext coreContext = HttpCoreContext.create();
            requester.execute(
                    new BasicAsyncRequestProducer(target, request),
                    new BasicAsyncResponseConsumer(),
                    pool,
                    coreContext,
                    // Handle HTTP response from a callback
                    new FutureCallback<HttpResponse>() {

                public void completed(final HttpResponse response) {
                    latch.countDown();
                    System.out.println(target + "->" + response.getStatusLine());
                    try {
						System.out.println("返回信息:"+EntityUtils.toString(response.getEntity()));
					} catch (ParseException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
                }

                public void failed(final Exception ex) {
                    latch.countDown();
                    System.out.println(target + "->" + ex);
                }

                public void cancelled() {
                    latch.countDown();
                    System.out.println(target + " cancelled");
                }

            });
        }
        latch.await();
        System.out.println("Shutting down I/O reactor");
        ioReactor.shutdown();
        System.out.println("Done");
    }
}
