package org.andrewliu.javanet.security;

import java.io.IOException;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * 用Base64算法对信息进行加解密
 * Base64内容传送编码被设计用来把任意序列的8位字节描述为一种不易被人直接识别的形式
 * 常见于邮件、http加密，截取http信息，你就会发现登录操作的用户名、密码字段通过BASE64加密的
 * @author de
 *
 */
public class BASE64EnDecrypt {

	/**
	 * BASE64 解密
	 * @param key
	 * @return
	 * @throws IOException
	 */
	public static byte[]  decryptBASE64(String key) throws IOException{
		return  (new BASE64Decoder()).decodeBuffer(key);
	}
	
	/**
	 * BASE64 加密
	 * @param key
	 * @return
	 */
	public static String encryptBASE64(byte[] key){
		return (new BASE64Encoder()).encodeBuffer(key);
	}

	
	public static void main(String[] args) throws IOException {
		String key = "liudaan";
		byte[]  keyBytes = key.getBytes();
		String result = BASE64EnDecrypt.encryptBASE64(keyBytes);
		System.out.println("liudaan: 用Base64加密后的结果是:"+result);
		result = new String(BASE64EnDecrypt.decryptBASE64(result));
		System.out.println(result+"用Base64解密后的结果是:"+result.toString());
	}
	
}
