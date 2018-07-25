package org.andrewliu.javanet.security;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.security.KeyStore;
import java.util.Iterator;
import java.util.logging.Logger;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLEngineResult;
import javax.net.ssl.SSLEngineResult.HandshakeStatus;
import javax.net.ssl.SSLEngineResult.Status;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManagerFactory;

/**
 * SSLEngine + NIO 实现SSL握手协议 　　Java 提供了阻塞和非阻塞I/O。非阻塞的I/O,大大提高了服务器的扩展性和伸缩性。
 * SSLServerSocket和SSLSocket是阻塞的socket调用。这连个类分别继承自ServerSocket和Socket,
 * 封装了SSL(Secure Socket Layer)和TLS(Transport Layer Security),
 * 提供了安全套接字。ServerSocket和Socket是阻塞的现实，
 * 因此SSLServerSocket和SSLSocket也是阻塞的实现。阻塞实现的套接字的好处的是编程简单
 * ，学习成本低；缺点是扩展性和并发性较差。没法满足大并发服务器的实现要求。
 * 如果要实现非阻塞的安全套接字，需要将SSLEngine和SocketChannel结合使用。 安全通信模型包括：
 * 
 * 1）数据一致性保护。数据一致性通过加密套件和信息摘要套件实现
 * 
 * 2）认证。Client和Server互相认证
 * 
 * 3）机密性。 数据被加密后传输，没有明文数据（plain text）在网络上传播。
 * SSLEngine负责入栈和出栈数据加密和解密以及加密解密算法的协商。加密解密的算法是通过握手协议协商完成的。 握手过程如下：
 * 
 * 1. 客户端想服务器端发送信息。信息包括支持的最高版本的SSL和加密套件列表。
 * 
 * 2. 服务器发送支持的SSL和加密套件。完成加密套件的协商。
 * 
 * 3.服务器端生成密钥并用公钥加密发送给服务器，服务器用私钥解密。
 * 
 * 4.客户端和服务器用第三步生成的密钥加密数据进行传输
 * 
 * @author de
 *
 */
public class SSLHandshakeClient {

	private static Logger logger = Logger.getLogger(SSLHandshakeClient.class
			.getName());
	private SocketChannel sc;

	private SSLEngine sslEngine;
	private Selector selector;
	private HandshakeStatus hsStatus;
	private Status status;
	private ByteBuffer myNetData;
	private ByteBuffer myAppData;
	private ByteBuffer peerNetData;
	private ByteBuffer peerAppData;

	private ByteBuffer dummy = ByteBuffer.allocate(0);

	public void run() throws Exception {

		char[] password = "123456".toCharArray();
		KeyStore trustStore = KeyStore.getInstance("JKS");
		InputStream in = this.getClass().getResourceAsStream(
				"clienttruststore.jks");
		trustStore.load(in, password);

		TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
		tmf.init(trustStore);

		SSLContext sslContext = SSLContext.getInstance("SSL");
		sslContext.init(null, tmf.getTrustManagers(), null);

		sslEngine = sslContext.createSSLEngine();
		sslEngine.setUseClientMode(true);

		SSLSession session = sslEngine.getSession();
		myAppData = ByteBuffer.allocate(session.getApplicationBufferSize());
		myNetData = ByteBuffer.allocate(session.getPacketBufferSize());
		peerAppData = ByteBuffer.allocate(session.getApplicationBufferSize());
		peerNetData = ByteBuffer.allocate(session.getPacketBufferSize());

		peerNetData.clear();

		SocketChannel channel = SocketChannel.open();
		channel.configureBlocking(false);
		selector = Selector.open();
		channel.register(selector, SelectionKey.OP_CONNECT);
		channel.connect(new InetSocketAddress("localhost", 443));
		sslEngine.beginHandshake();
		hsStatus = sslEngine.getHandshakeStatus();

		while (true) {
			selector.select();
			Iterator<SelectionKey> it = selector.selectedKeys().iterator();
			while (it.hasNext()) {
				SelectionKey selectionKey = it.next();
				it.remove();
				handleSocketEvent(selectionKey);
			}
		}
	}

	private void handleSocketEvent(SelectionKey key) throws IOException {
		if (key.isConnectable()) {
			sc = (SocketChannel) key.channel();
			if (sc.isConnectionPending()) {
				sc.finishConnect();
			}
			doHandshake();
			sc.register(selector, SelectionKey.OP_READ);
		}

		if (key.isReadable()) {

			sc = (SocketChannel) key.channel();
			doHandshake();
			if (hsStatus == HandshakeStatus.FINISHED) {
				logger.info("Client handshake completes... ...");
				key.cancel();
				sc.close();
			}
		}
	}

	private void doHandshake() throws IOException {

		SSLEngineResult result;
		int count = 0;

		while (hsStatus != HandshakeStatus.FINISHED) {

			logger.info("handshake status: " + hsStatus);
			switch (hsStatus) {
			case NEED_TASK:
				Runnable runnable;
				while ((runnable = sslEngine.getDelegatedTask()) != null) {
					runnable.run();
				}
				hsStatus = sslEngine.getHandshakeStatus();
				break;

			case NEED_UNWRAP:

				count = sc.read(peerNetData);
				if (count < 0) {
					logger.info("no data is read for unwrap.");
					break;
				} else {
					logger.info("data read: " + count);
				}
				peerNetData.flip();
				peerAppData.clear();

				do {
					result = sslEngine.unwrap(peerNetData, peerAppData);
					logger.info("Unwrapping:\n" + result);
					// During an handshake renegotiation we might need to
					// perform
					// several unwraps to consume the handshake data.
				} while (result.getStatus() == SSLEngineResult.Status.OK
						&& result.getHandshakeStatus() == SSLEngineResult.HandshakeStatus.NEED_UNWRAP
						&& result.bytesProduced() == 0);

				if (peerAppData.position() == 0
						&& result.getStatus() == SSLEngineResult.Status.OK
						&& peerNetData.hasRemaining()) {

					result = sslEngine.unwrap(peerNetData, peerAppData);
					logger.info("Unwrapping:\n" + result);

				}

				hsStatus = result.getHandshakeStatus();
				status = result.getStatus();

				assert status != status.BUFFER_OVERFLOW : "buffer not overflow."
						+ status.toString();

				// Prepare the buffer to be written again.
				peerNetData.compact();
				// And the app buffer to be read.
				peerAppData.flip();

				break;

			case NEED_WRAP:

				myNetData.clear();
				result = sslEngine.wrap(dummy, myNetData);
				hsStatus = result.getHandshakeStatus();
				status = result.getStatus();

				while (status != Status.OK) {
					logger.info("status: " + status);
					switch (status) {

					case BUFFER_OVERFLOW:
						break;

					case BUFFER_UNDERFLOW:
						break;
					}
				}
				myNetData.flip();
				count = sc.write(myNetData);
				if (count <= 0) {
					logger.info("No data is written.");
				} else {
					logger.info("Written data: " + count);
				}
				break;
			}
		}
	}

	public static void main(String[] args) throws Exception {
		new SSLHandshakeClient().run();
	}

}
