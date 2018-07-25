package org.andrewliu.javanet.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5算法：
 * MD5 -- message-digest algorithm 5 （信息-摘要算法）缩写，
 * 广泛用于加密和解密技术，常用于文件校验。校验？不管文件多大，
 * 经过MD5后都能生成唯一的MD5值。好比现在的ISO校验，都是MD5校验。怎么用？当然是把ISO经过MD5后产生MD5的值。
 * 一般下载linux-ISO的朋友都见过下载链接旁边放着MD5的串。就是用来验证文件是否一致的。
 * 一般步驟為：
 * 1、甲方公布信息摘要算法；
 * 2、對數據做信息摘要處理；
 * 3、發送原始數據、信息摘要；
 * 4、乙方對原始數據做摘要處理；
 * 5、校驗摘要信息是否一致
 * @author de
 *
 */
public class MD5EnDecrypt {

	
	/**
	 * 用md5將數據加密
	 * 通常將md5產生的byte字節，再用BASE64加密一次
	 * @param data
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	public static byte[]  encyptMD5(byte[] data) throws NoSuchAlgorithmException{
		MessageDigest md5 = MessageDigest.getInstance(CryptInstanceType.KEYMD5.name());
		md5.update(data);
		return md5.digest();
	}
}
