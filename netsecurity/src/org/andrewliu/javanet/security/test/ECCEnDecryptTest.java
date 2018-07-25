package org.andrewliu.javanet.security.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.spec.InvalidKeySpecException;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;

import org.andrewliu.javanet.security.despbe.ECCEnDecryptCoder;
import org.junit.Test;

public class ECCEnDecryptTest {
	
	@Test
	public void test() throws InvalidKeyException, InvalidKeySpecException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, IOException{
		String inputStr = "abc";
		byte[] data = inputStr.getBytes();
		
		Map<String,Object> keyMap = ECCEnDecryptCoder.initKey();
		
		String publicKey = ECCEnDecryptCoder.getPublicKey(keyMap);
		String privateKey = ECCEnDecryptCoder.getPrivateKey(keyMap);
		
		System.err.println("公钥：\n"+publicKey);
		System.err.println("私钥：\n"+privateKey);
		
		byte[] encodedData = ECCEnDecryptCoder.encrypt(data, publicKey);
		byte[] decodedData = ECCEnDecryptCoder.decrypt(encodedData, privateKey);
		String outputStr = new String(decodedData);
		System.err.println("加密前："+inputStr+"\n\r"+"解密后："+outputStr);
		assertEquals(inputStr,outputStr);
	}

}
