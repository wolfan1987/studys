package org.andrewliu.javanet.security;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * HMAC   HMAC(Hash Message Authentication Code，散列消息鉴别码，基于密钥的Hash算法的认证协议。
 * 消息鉴别码实现鉴别的原理是，用公开函数和密钥产生一个固定长度的值作为认证标识，
 * 用这个标识鉴别消息的完整性。使用一个密钥生成一个固定大小的小数据块，即MAC，
 *  并将其加入到消息中，然后传输。接收方利用与发送方共享的密钥进行鉴别认证等
 * 基本步驟：
 * 甲方：
 * 1、构建密钥；
 * 2、发送密钥；
 * 3、使用密钥对数据摘要；
 * 4、发送原始数据、信息摘要；
 * 乙方：
 * 5、使用密钥对数据摘要；
 * 6、验证摘要信息是否一致；
 * @author de
 *
 */
public class HMACEnDecrypt {

	
	public static String initHmacKey() throws NoSuchAlgorithmException{
		KeyGenerator keyGenerator = KeyGenerator.getInstance(CryptInstanceType.KEYHMAC.name());
		SecretKey secretKey = keyGenerator.generateKey();
		//在此处可返回经BASE64加密后的数据
		return BASE64EnDecrypt.encryptBASE64(secretKey.getEncoded());
	}
	
	public static byte[]  encryptHMAC(byte[] data, String key) throws IOException, NoSuchAlgorithmException, InvalidKeyException{
		SecretKey secretKey = new SecretKeySpec(BASE64EnDecrypt.decryptBASE64(key),CryptInstanceType.KEYHMAC.name());
		Mac mac = Mac.getInstance(secretKey.getAlgorithm());
		mac.init(secretKey);
		return mac.doFinal(data);
		
	}
	
}
