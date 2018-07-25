package org.andrewliu.javanet.security.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.andrewliu.javanet.security.despbe.DHEnDecryptCoder;
import org.junit.Test;

public class DHEnDecryptTest  {

	@Test
	public void test(){
		try{
		//生成甲方密钥对儿
		Map<String,Object> akeyMap = DHEnDecryptCoder.initKey();
		String aPublicKey = DHEnDecryptCoder.getPublicKey(akeyMap);
		String aPrivateKey = DHEnDecryptCoder.getPrivateKey(akeyMap);
		System.err.println("甲方公钥：\r"+aPublicKey);
		System.err.println("甲方密钥：\r"+aPrivateKey);
		
		//由甲方公钥产生本地密钥对儿
		Map<String,Object> bKeyMap = DHEnDecryptCoder.initKey(aPublicKey);
		String bPublicKey = DHEnDecryptCoder.getPublicKey(bKeyMap);
		String bPrivateKey = DHEnDecryptCoder.getPrivateKey(bKeyMap);
		
		System.err.println("乙方公钥：\r"+bPublicKey);
		System.err.println("乙方私钥：\r"+bPrivateKey);
		
		String aInput = "abc";
		System.err.println("原文:"+aInput);
		
		//由甲方公钥，乙方私钥构建密文
		byte[] aCode = DHEnDecryptCoder.encrypt(aInput.getBytes(), aPublicKey, aPrivateKey);
		//由乙方公钥、甲方私钥解密
		byte[] aDecode = DHEnDecryptCoder.decrypt(aCode, bPublicKey, aPrivateKey);
		
		String aOutput = (new String(aDecode));
		
		System.err.println("解密："+aOutput);
		
		assertEquals(aInput,aOutput);
		System.err.println("============反过来加密解密============");
		String bInput = "def";
		System.err.println("原文："+bInput);
		
		//由乙方公钥，甲方私钥构建密文
		byte[] bCode = DHEnDecryptCoder.encrypt(bInput.getBytes(), bPublicKey, aPrivateKey);
		
		//同甲方公钥，乙方私钥解密
		byte[]  bDecode = DHEnDecryptCoder.decrypt(bCode, aPublicKey, bPrivateKey);
		String bOutput = (new String(bDecode));
		System.err.println("解密："+bOutput);
		assertEquals(bInput,bOutput);
		
		}catch(NoSuchAlgorithmException e){
			e.printStackTrace();
		}catch(InvalidKeySpecException e){
			e.printStackTrace();
		}catch(InvalidAlgorithmParameterException e){
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}catch(InvalidKeyException e){
			e.printStackTrace();
		}catch(NoSuchPaddingException e){
			e.printStackTrace();
		}catch(IllegalBlockSizeException e){
			e.printStackTrace();
		}catch(BadPaddingException e){
			e.printStackTrace();
		}
		
	}
}
