package org.andrewliu.javanet.security.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.andrewliu.javanet.security.despbe.CertificateEnDecryptCoder;
import org.junit.Test;

import com.sun.net.ssl.HttpsURLConnection;

/**
 * 通过数字证书来加解密
 * @author de
 *
 */
public class CertificateEnDecryptTest {

	private String password = "123456";
	private String alias = "liuDAServer";
	private String clientAlias = "liuDA";
	private String certificatePath = "F:/PosWorkspace/netsecurity/mykeystores/server.cer";
	private String clientCertificatePath = "F:/PosWorkspace/netsecurity/mykeystores/client.cer";
	private String keyStorePath = "F:/PosWorkspace/netsecurity/mykeystores/server.keystore";
	private String clientKeyStorePath = "F:/PosWorkspace/netsecurity/mykeystores/client.keystore";
	
	
	@Test
	public void test() throws InvalidKeyException, CertificateException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, IOException, UnrecoverableKeyException, KeyStoreException{
		System.err.println("公钥加密---私钥解密");
		String inputStr = "Certificate";
		byte[] data = inputStr.getBytes();
		
		byte[] encrypt = CertificateEnDecryptCoder.encryptByPublicKey(data, certificatePath);
		byte[] decrypt = CertificateEnDecryptCoder.decryptByPrivateKey(encrypt, keyStorePath, alias, password);
		String outputStr = new String(decrypt);
		
		System.err.println("加密前："+inputStr+"\n\r"+"解密后："+outputStr);
		
		//验证数据一致
		assertArrayEquals(data,decrypt);
		//验证证书有效
		assertTrue(CertificateEnDecryptCoder.verifyCertificate(certificatePath));
	}
	
	
	@Test
	public void testSign() throws UnrecoverableKeyException, InvalidKeyException, KeyStoreException, NoSuchAlgorithmException, CertificateException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, IOException, SignatureException{
		System.err.println("私钥加密---公钥解密");
		String inputStr = "sign";
		byte[] data = inputStr.getBytes();
		
		byte[]  encodedData = CertificateEnDecryptCoder.encryptByPrivateKey(data, keyStorePath, alias, password);
		byte[] decodedData = CertificateEnDecryptCoder.decryptByPublicKey(encodedData, certificatePath);
		
		String outputStr = new String(decodedData);
		System.err.println("加密前: "+ inputStr + "\n\r" + "解密后："+outputStr);
		assertEquals(inputStr,outputStr);
		
		System.err.println("私钥签名---公钥验证签名");
		//产生签名
		String sign = CertificateEnDecryptCoder.sign(encodedData, keyStorePath, alias, password);
		
		System.err.println("签名：\r"+sign);
		//验证签名
		boolean status = CertificateEnDecryptCoder.verify(encodedData, sign, certificatePath);
		System.err.println("状态：\r"+status);
		assertTrue(status);
	}
	
	/**
	 * 模拟Https请求，验证证书是否有效
	 * @throws UnrecoverableKeyException
	 * @throws KeyManagementException
	 * @throws NoSuchAlgorithmException
	 * @throws KeyStoreException
	 * @throws CertificateException
	 * @throws IOException
	 */
	@Test
	public void testHttps() throws UnrecoverableKeyException, KeyManagementException, NoSuchAlgorithmException, KeyStoreException, CertificateException, IOException{
		URL url = new URL("https://www.youdomain.org/examples");
		HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
		conn.setDoInput(true);
		conn.setDoOutput(true);
		
		CertificateEnDecryptCoder.configSSLSocketFactory(conn, password, clientKeyStorePath, clientKeyStorePath);
		
		InputStream is = conn.getInputStream();
		int length = conn.getContentLength();
		DataInputStream dis = new DataInputStream(is);
		byte[] data = new byte[length];
		dis.readFully(data);
		
		dis.close();
		System.err.println(new String(data));
		conn.disconnect();
		
		
	}
	
}
