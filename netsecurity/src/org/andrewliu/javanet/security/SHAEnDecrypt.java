package org.andrewliu.javanet.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * SHA(Secure Hash Algorithm，安全散列算法），数字签名等密码学应用中重要的工具，
 * 被广泛地应用于电子商务等信息安全领域。虽然，SHA与MD5通过碰撞法都被破解了， 
 * 但是SHA仍然是公认的安全 加密算法，较之MD5更为安全
 * 甲方:
 * 1、公布信息摘要算法；
 * 2、對數據做信息摘要處理；
 * 3、發送原始數據、信息摘要；
 * 乙方：
 * 4、對原始數據做摘要處理；
 * 5、校驗摘要信息是否一致
 * @author de
 *
 */
public class SHAEnDecrypt {

	
	public static byte[]  encryptSHA(byte[] data) throws NoSuchAlgorithmException{
		MessageDigest sha = MessageDigest.getInstance(CryptInstanceType.KEYSHA.name());
		sha.update(data);
		return sha.digest();
	}
}
