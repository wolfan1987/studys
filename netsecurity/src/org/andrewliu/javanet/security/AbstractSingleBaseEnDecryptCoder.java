package org.andrewliu.javanet.security;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * 单向加密算法： BASE64的加密解密是双向的，可以求反解。    
 * MD5、SHA以及HMAC是单向加密，任何数据加密后只会产生唯一的一个加密串，
 * 通常用来校验数据在传输过程中是否被修改。 其中HMAC算法有一个密钥，
 * 增强了数据传输过程中的安全性，强化了算法外的不可控因素
 * 
 * 单向加密的用途主要是为了校验数据在传输过程中是否被修改
 * 
 *  MD5\SHA\HMAC实现实例
 * 
 * @author de
 *
 */
public abstract class AbstractSingleBaseEnDecryptCoder {
	public static final String KEY_SHA = "SHA";
	public static final String KEY_MD5 = "MD5";
	/**
	 * MAC算法可选以下多种算法 HmacMD5 HmacSHA1 HmacSHA256 HmacSHA384 HmacSHA512
	 */
	public static final String KEY_MAC = "HmacMD5";

	/**
	 * BASE64解密
	 * 
	 * @param key
	 * @return
	 * @throws IOException
	 */
	public static byte[] decryptBASE64(String key) throws IOException {
		return (new BASE64Decoder()).decodeBuffer(key);
	}

	/**
	 * BASE64 加密
	 * 
	 * @param key
	 * @return
	 */
	public static String encryptBASE64(byte[] key) {
		return (new BASE64Encoder()).encodeBuffer(key);
	}

	/**
	 * MD5 加密
	 * 
	 * @param data
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	public static byte[] encryptMD5(byte[] data)
			throws NoSuchAlgorithmException {
		MessageDigest md5 = MessageDigest.getInstance(KEY_MD5);
		md5.update(data);
		return md5.digest();
	}

	/**
	 * SHA 加密
	 * 
	 * @param data
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	public static byte[] encryptSHA(byte[] data)
			throws NoSuchAlgorithmException {
		MessageDigest sha = MessageDigest.getInstance(KEY_SHA);
		sha.update(data);
		return sha.digest();
	}

	/**
	 * 初始化HMAC密钥
	 * 
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	public static String initMacKey() throws NoSuchAlgorithmException {
		KeyGenerator keyGenerator = KeyGenerator.getInstance(KEY_MAC);
		SecretKey secretKey = keyGenerator.generateKey();
		return encryptBASE64(secretKey.getEncoded());
	}

	/**
	 * HMAC加密
	 * 
	 * @param data
	 * @param key
	 * @return
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeyException
	 */
	public static byte[] encryptHMAC(byte[] data, String key)
			throws IOException, NoSuchAlgorithmException, InvalidKeyException {
		SecretKey secretKey = new SecretKeySpec(decryptBASE64(key), KEY_MAC);
		Mac mac = Mac.getInstance(secretKey.getAlgorithm());
		mac.init(secretKey);
		return mac.doFinal(data);
	}

}
