package org.andrewliu.javanet.security.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.util.Map;

import org.andrewliu.javanet.security.despbe.DSAEnDecryptCoder;
import org.junit.Test;


public class DSAEnDecryptTest {

	@Test
	public void test() throws NoSuchAlgorithmException, InvalidKeyException, InvalidKeySpecException, SignatureException, IOException{
		String inputStr = "abc";
		byte[] data = inputStr.getBytes();
		//构建密钥
		Map<String,Object> keyMap = DSAEnDecryptCoder.initKey();
		
		//获得密钥
		String publicKey = DSAEnDecryptCoder.getPublicKey(keyMap);
		String privateKey = DSAEnDecryptCoder.getPrivateKey(keyMap);
		
		System.err.println("公钥：\r"+publicKey);
		System.err.println("私钥:\r"+privateKey);
		
		//产生签名
		String sign = DSAEnDecryptCoder.sign(data, privateKey);
		System.err.println("签名：\r"+sign);
		//验证签名
		boolean status = DSAEnDecryptCoder.verify(data, publicKey, sign);
		System.err.println("状态:\r"+status);
		assertTrue(status);
	}
}
