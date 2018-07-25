package org.andrewliu.javanet.security.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.andrewliu.javanet.security.despbe.DESEnDecryptCoder;
import org.junit.Test;

public class DESEnDecryptTest {

	@Test
	public void test() throws NoSuchAlgorithmException, IOException, InvalidKeyException, InvalidKeySpecException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
		
		String inputStr = "DES";
		String key = DESEnDecryptCoder.initKey();
		System.err.println("原文：\t" + inputStr);

		System.err.println("密钥：\t" + key);
		byte[] inputData = inputStr.getBytes();
		inputData = DESEnDecryptCoder.encrypt(inputData, key);
		System.err.println("加密后：\t"
				+ DESEnDecryptCoder.encryptBASE64(inputData));

		byte[] outputData = DESEnDecryptCoder.decrypt(inputData, key);
		String outputStr = new String(outputData);
		System.err.println("解密后:\t" + outputStr);

		assertEquals(inputStr, outputStr);

	}
}
