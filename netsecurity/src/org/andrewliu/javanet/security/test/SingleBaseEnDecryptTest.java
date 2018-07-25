package org.andrewliu.javanet.security.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import org.andrewliu.javanet.security.AbstractSingleBaseEnDecryptCoder;
import org.junit.Test;

public class SingleBaseEnDecryptTest {

	@Test
	public void  endecryptTest() throws IOException, NoSuchAlgorithmException, InvalidKeyException{
		
		String inputStr = "简单加密";
		System.err.println("原文：\n"+ inputStr);
		
		byte[] inputData = inputStr.getBytes();
		
		String code = AbstractSingleBaseEnDecryptCoder.encryptBASE64(inputData);
		
		System.err.println("BASE64 加密后：\n"+ code);
		
		byte[]  output = AbstractSingleBaseEnDecryptCoder.decryptBASE64(code);
		
		String outputStr = new String(output);
		
		System.err.println("BASE64 解密后：\n"+ outputStr);
		
		//验证BASE64加密解密的一致性
		assertEquals(inputStr,outputStr);
		
		//验证MD5对于同一内容加密是否一致
		byte[]  first = AbstractSingleBaseEnDecryptCoder.encryptMD5(inputData);
		byte[]  second = AbstractSingleBaseEnDecryptCoder.encryptMD5(inputData);
		
		assertArrayEquals(first,second);
		
		//验证SHA对于同一内容加密是否一致
		assertArrayEquals(AbstractSingleBaseEnDecryptCoder.encryptSHA(inputData),AbstractSingleBaseEnDecryptCoder.encryptSHA(inputData));
		
		//初始化HMAC密钥
		String key = AbstractSingleBaseEnDecryptCoder.initMacKey();
		System.err.println("Mac 密钥:\n"+ key);
		
		//验证HMAC对于同一内容，同一密钥加密是否一致
		assertArrayEquals(AbstractSingleBaseEnDecryptCoder.encryptHMAC(inputData,key),AbstractSingleBaseEnDecryptCoder.encryptHMAC(inputData,key));
		
		BigInteger md5 = new BigInteger(AbstractSingleBaseEnDecryptCoder.encryptMD5(inputData));
		System.out.println("MD5:\n"+ md5.toString(16));
		
		BigInteger sha = new BigInteger(AbstractSingleBaseEnDecryptCoder.encryptSHA(inputData));
		System.out.println("SHA:\n"+ sha.toString(32));
		
		BigInteger mac = new BigInteger(AbstractSingleBaseEnDecryptCoder.encryptHMAC(inputData,inputStr));
		System.out.println("HMAC:\n"+ mac.toString(16));
		
	}
	
}
