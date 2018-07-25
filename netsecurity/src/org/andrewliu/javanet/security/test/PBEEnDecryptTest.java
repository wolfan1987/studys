package org.andrewliu.javanet.security.test;

import static org.junit.Assert.*;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.andrewliu.javanet.security.despbe.PBEEnDecryptCoder;
import org.junit.Test;


public class PBEEnDecryptTest {

	@Test
	public void test() throws InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		String inputStr = "abcdefg";
		System.err.println("原文："+inputStr);
		byte[] input = inputStr.getBytes();
		String pwd = "efg";
		System.err.println("密码："+pwd);
		//初始化盐值
		byte[] salt = PBEEnDecryptCoder.initSalt();
		byte[] data = PBEEnDecryptCoder.encrypt(input, pwd, salt);
		System.err.println("加密后："+PBEEnDecryptCoder.encryptBASE64(data));
		
		byte[] output = PBEEnDecryptCoder.decrypt(data, pwd, salt);
		String outputStr = new String(output);
		
		System.err.println("解密后: "+outputStr);
		assertEquals(inputStr,outputStr);
	}
}
