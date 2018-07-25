package org.andrewliu.javanet.security.despbe;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Date;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.net.ssl.SSLSocketFactory;

import org.andrewliu.javanet.security.AbstractSingleBaseEnDecryptCoder;

import com.sun.net.ssl.HttpsURLConnection;
import com.sun.net.ssl.KeyManagerFactory;
import com.sun.net.ssl.SSLContext;
import com.sun.net.ssl.TrustManagerFactory;

/**
 * 证书认证组件（非对称加密实现）：
 * 1、先用java自带的keytool工具生成数字证书库文件(.keystore文件)，再从数据证书库文件中导出数据证书文件.cer文件。
 * 2、再用此程序操作cer文件来进行数据的加解密
 * @author de
 *
 */
public abstract class CertificateEnDecryptCoder  extends  AbstractSingleBaseEnDecryptCoder{

	/**
	 * Java 密钥库(java Key Store,JKS) ---KEY_STORE
	 */
	public static final String KEY_STORE = "JKS";
	public static final String X509 = "X.509";
	public static final String SunX509 = "SunX509";
	public static final String SSL = "SSL";
	
	
	/**
	 * 从keyStore中得到私钥
	 * @param keyStorePath
	 * @param alias
	 * @param password
	 * @return
	 * @throws IOException 
	 * @throws CertificateException 
	 * @throws NoSuchAlgorithmException 
	 * @throws KeyStoreException 
	 * @throws UnrecoverableKeyException 
	 */
	private static PrivateKey getPrivateKey(String keyStorePath,String alias,String password) throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException, UnrecoverableKeyException{
		KeyStore ks = getKeyStore(keyStorePath,password);
		PrivateKey key = (PrivateKey)ks.getKey(alias, password.toCharArray());
		return key;
	}
	
	/**
	 * 从Certificate(证书）中获得公钥
	 * @param certificatePath
	 * @return
	 * @throws IOException 
	 * @throws CertificateException 
	 */
	private static PublicKey getPublicKey(String certificatePath) throws CertificateException, IOException{
		Certificate certificate = getCertificate(certificatePath);
		PublicKey key = certificate.getPublicKey();
		return key;
	}
	
	/**
	 * 从数字证书文件(Certificate)中获得数字证书对象
	 * @param certificatePath
	 * @return
	 * @throws CertificateException 
	 * @throws IOException 
	 */
	private static Certificate getCertificate(String certificatePath) throws CertificateException, IOException{
		CertificateFactory certificateFactory = CertificateFactory.getInstance(X509);
		FileInputStream in = new FileInputStream(certificatePath);
		Certificate certificate = certificateFactory.generateCertificate(in);
		in.close();
		return certificate;
	}
	/**
	 * 从keystore文件中得到别名为alias的证书信息，并从证书库中返回证书对象
	 * @param keyStorePath
	 * @param alias
	 * @param password
	 * @return
	 * @throws IOException 
	 * @throws CertificateException 
	 * @throws NoSuchAlgorithmException 
	 * @throws KeyStoreException 
	 */
	private static Certificate getCertificate(String keyStorePath,String alias,String password) throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException{
		KeyStore ks = getKeyStore(keyStorePath,password);
		Certificate certificate = ks.getCertificate(alias);
		return certificate;
	}
	
	/**
	 * 加元KeyStore文件(数字证书库文件)，并返回KeyStore对象
	 * @param keyStorePath
	 * @param password
	 * @return
	 * @throws KeyStoreException
	 * @throws NoSuchAlgorithmException
	 * @throws CertificateException
	 * @throws IOException
	 */
	private static KeyStore getKeyStore(String keyStorePath,String password) throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException{
		FileInputStream is = new FileInputStream(keyStorePath);
		KeyStore ks = KeyStore.getInstance(KEY_STORE);
		ks.load(is, password.toCharArray());
		is.close();
		return ks;
	}
	
	/**
	 * 用私钥对数据加密
	 * @param data
	 * @param keyStorePath
	 * @param alias
	 * @param password
	 * @return
	 * @throws UnrecoverableKeyException
	 * @throws KeyStoreException
	 * @throws NoSuchAlgorithmException
	 * @throws CertificateException
	 * @throws IOException
	 * @throws NoSuchPaddingException
	 * @throws InvalidKeyException
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 */
	public static byte[] encryptByPrivateKey(byte[] data,String keyStorePath,String alias,String password) throws UnrecoverableKeyException, KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException{
		//取得私钥
		PrivateKey privateKey = getPrivateKey(keyStorePath, alias, password);
		//对数据加密
		Cipher cipher = Cipher.getInstance(privateKey.getAlgorithm());
		cipher.init(Cipher.ENCRYPT_MODE, privateKey);
		return cipher.doFinal(data);
	}
	
	/**
	 * 用私钥对数据解密
	 * @param data
	 * @param keyStorePath
	 * @param alias
	 * @param password
	 * @return
	 * @throws UnrecoverableKeyException
	 * @throws KeyStoreException
	 * @throws NoSuchAlgorithmException
	 * @throws CertificateException
	 * @throws IOException
	 * @throws NoSuchPaddingException
	 * @throws InvalidKeyException
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 */
	public static byte[] decryptByPrivateKey(byte[] data,String keyStorePath,String alias,String password) throws UnrecoverableKeyException, KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException{
		//取得私钥
		PrivateKey privateKey = getPrivateKey(keyStorePath,alias,password);
		//对数据解密
		Cipher cipher = Cipher.getInstance(privateKey.getAlgorithm());
		cipher.init(Cipher.DECRYPT_MODE, privateKey);
		return cipher.doFinal(data);
	}
	
	/**
	 * 用公钥对数据加密
	 * @param data
	 * @param certificatePath
	 * @return
	 * @throws CertificateException
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchPaddingException
	 * @throws InvalidKeyException
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 */
	public static byte[]  encryptByPublicKey(byte[] data,String certificatePath) throws CertificateException, IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException{
		//取得公钥
		PublicKey publicKey = getPublicKey(certificatePath);
		//对数据加密
		Cipher cipher = Cipher.getInstance(publicKey.getAlgorithm());
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);
		return cipher.doFinal(data);
	}
	/**
	 * 用公钥对数据解密
	 * @param data
	 * @param certificatePath
	 * @return
	 * @throws CertificateException
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchPaddingException
	 * @throws InvalidKeyException
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 */
	public static byte[]  decryptByPublicKey(byte[] data,String certificatePath) throws CertificateException, IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException{
		//取得公钥
		PublicKey publicKey = getPublicKey(certificatePath);
		//对数据解密 
		Cipher cipher = Cipher.getInstance(publicKey.getAlgorithm());
		cipher.init(Cipher.DECRYPT_MODE, publicKey);
		return cipher.doFinal(data);
	}
	
	/**
	 * 加载数字证书文件，并验证其有效性
	 * @param certificatePath
	 * @return
	 */
	public static boolean verifyCertificate(String certificatePath){
		return verifyCertificate(new Date(),certificatePath);
	}
	
	/**
	 * 验证Certificate是否过期或无效
	 * @param date
	 * @param certificatePath
	 * @return
	 */
	public static boolean verifyCertificate(Date date,String certificatePath){
		boolean status = true;
		try {
			//取得证书
			 Certificate	certificate = getCertificate(certificatePath);
			//验证证书是否过期或无效
			status = verifyCertificate(date,certificate);
		} catch (CertificateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return status;
		
	}
	/**
	 * 验证证书是否过期或无效
	 * @param date
	 * @param certificate
	 * @return
	 */
	private static boolean verifyCertificate(Date date,Certificate certificate){
		boolean status = true;
		try{
			X509Certificate x509Certificate = (X509Certificate)certificate;
			x509Certificate.checkValidity(date);
		}catch(Exception e){
			status = false;
		}
		return status;
	}
	/**
	 * 对数据进行签名
	 * @param sign
	 * @param keyStorePath
	 * @param alias
	 * @param password
	 * @return
	 * @throws KeyStoreException
	 * @throws NoSuchAlgorithmException
	 * @throws CertificateException
	 * @throws IOException
	 * @throws UnrecoverableKeyException
	 * @throws InvalidKeyException
	 * @throws SignatureException
	 */
	public static String sign(byte[] sign,String keyStorePath,String alias,String password) throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException, UnrecoverableKeyException, InvalidKeyException, SignatureException{
		//获得证书
		X509Certificate x509Certificate = (X509Certificate)getCertificate(keyStorePath,alias,password);
		//获得私钥
		KeyStore ks = getKeyStore(keyStorePath,password);
		//取得私钥
		PrivateKey privateKey = (PrivateKey) ks.getKey(alias, password.toCharArray());
		//构建签名
		Signature signature = Signature.getInstance(x509Certificate.getSigAlgName());
		signature.initSign(privateKey);
		signature.update(sign);;
		return encryptBASE64(signature.sign());
	}
	/**
	 * 验证数字签名
	 * @param data
	 * @param sign
	 * @param certificatePath
	 * @return
	 * @throws CertificateException
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeyException
	 * @throws SignatureException
	 */
	public static boolean verify(byte[] data,String sign,String certificatePath) throws CertificateException, IOException, NoSuchAlgorithmException, InvalidKeyException, SignatureException{
		//获得证书
		X509Certificate x509Certificate = (X509Certificate)getCertificate(certificatePath);
		//获得公钥
		PublicKey publicKey = x509Certificate.getPublicKey();
		//构建签名
		Signature signature = Signature.getInstance(x509Certificate.getSigAlgName());
		signature.initVerify(publicKey);
		signature.update(data);
		return signature.verify(decryptBASE64(sign));
	}
	/**
	 * 验证数据证书文件
	 * @param date
	 * @param keyStorePath
	 * @param alias
	 * @param password
	 * @return
	 */
	public static boolean verifyCertificate(Date date,String keyStorePath,String alias,String password){
		boolean status = true;
		try{
			Certificate certificate = getCertificate(keyStorePath,alias,password);
			status = verifyCertificate(date,certificate);
		}catch(Exception e){
			status = false;
		}
		return status;
	}
	/**
	 * 验证Certificate
	 * @param keyStorePath
	 * @param alias
	 * @param password
	 * @return
	 */
	public static boolean verifyCertificate(String keyStorePath,String alias,String password){
		return verifyCertificate(new Date(),keyStorePath, alias, password);
	}
	
	/**
	 * 初始化SSLSocketFactory
	 * @param password
	 * @param keyStorePath
	 * @param trustKeyStorePath
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws KeyStoreException
	 * @throws CertificateException
	 * @throws IOException
	 * @throws UnrecoverableKeyException
	 * @throws KeyManagementException
	 */
	private static SSLSocketFactory getSSLSocketFactory(String password,String keyStorePath,String trustKeyStorePath) throws NoSuchAlgorithmException, KeyStoreException, CertificateException, IOException, UnrecoverableKeyException, KeyManagementException{
		//初始化密钥库
		@SuppressWarnings("deprecation")
		KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(SunX509);
		KeyStore keyStore = getKeyStore(keyStorePath,password);
		keyManagerFactory.init(keyStore, password.toCharArray());
		//初始化信任库
		TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(SunX509);
		
		KeyStore trustKeyStore = getKeyStore(trustKeyStorePath,password);
		trustManagerFactory.init(trustKeyStore);
		
		//初始化SSL上下文
		SSLContext ctx = SSLContext.getInstance(SSL);
		ctx.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), null);
		SSLSocketFactory sf = ctx.getSocketFactory();
		return sf;
	}
	
	/**
	 * 为HttpsURLConnection配置SSLSocketFactory
	 * @param conn  HttpsURLConnection
	 * @param password  密码
	 * @param keyStorePath  证书库文件路径
	 * @param trustKeyStorePath  信息证书库文件路径 
	 * @throws UnrecoverableKeyException
	 * @throws KeyManagementException
	 * @throws NoSuchAlgorithmException
	 * @throws KeyStoreException
	 * @throws CertificateException
	 * @throws IOException
	 */
	public static void configSSLSocketFactory(@SuppressWarnings("deprecation") HttpsURLConnection conn,String password,String keyStorePath,String trustKeyStorePath) throws UnrecoverableKeyException, KeyManagementException, NoSuchAlgorithmException, KeyStoreException, CertificateException, IOException{
		conn.setSSLSocketFactory(getSSLSocketFactory(password, keyStorePath, trustKeyStorePath));
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
