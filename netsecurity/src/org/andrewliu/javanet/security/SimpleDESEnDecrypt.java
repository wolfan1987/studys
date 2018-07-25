package org.andrewliu.javanet.security;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import sun.misc.BASE64Encoder;

/**
 * 使用DES对称加密/解密：

数据加密标准算法(Data Encryption Standard)，和BASE64最明显的区别就是有一个工作密钥，该密钥既用于加密、也用于解密，并且要求密钥是一个长度至少大于8位的字符串。使用DES加密、解密的核心是确保工作密钥的安全性。 叫喊

1. 根据key生成密钥：

Java代码
DESKeySpec keySpec =  new  DESKeySpec(key.getBytes());  
SecretKeyFactory keyFactory = SecretKeyFactory.getInstance( "des" );  
SecretKey secretKey = keyFactory.generateSecret(keySpec);  
2. 加密操作：

Java代码
Cipher cipher = Cipher.getInstance( "des" );  
cipher.init(Cipher.ENCRYPT_MODE, secretKey,  new  SecureRandom());  
byte [] cipherData = cipher.doFinal(plainText.getBytes());  
3. 为了便于观察生成的加密数据，使用BASE64再次加密：

Java代码
String cipherText =  new  BASE64Encoder().encode(cipherData);  
生成密文如下：PtRYi3sp7TOR69UrKEIicA==

4. 解密操作：

Java代码
cipher.init(Cipher.DECRYPT_MODE, secretKey,  new  SecureRandom());  
byte [] plainData = cipher.doFinal(cipherData);  
String plainText =  new  String(plainData);  
 * @author de
 *
 */
public class SimpleDESEnDecrypt {

	public static void main(String[] args) throws InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
		String plainText =  "Hello , world !" ;  
	    String key =  "12345678" ;     //要求key至少长度为8个字符   
	      
	    SecureRandom random =  new  SecureRandom();  
	    DESKeySpec keySpec =  new  DESKeySpec(key.getBytes());  
	    SecretKeyFactory keyFactory = SecretKeyFactory.getInstance( "des" );  
	    SecretKey secretKey = keyFactory.generateSecret(keySpec);  
	      
	    Cipher cipher = Cipher.getInstance( "des" );  
	    cipher.init(Cipher.ENCRYPT_MODE, secretKey, random);  
	     byte [] cipherData = cipher.doFinal(plainText.getBytes());  
	    System.out.println( "cipherText : "  +  new  BASE64Encoder().encode(cipherData));  
	     //PtRYi3sp7TOR69UrKEIicA==   
	      
	    cipher.init(Cipher.DECRYPT_MODE, secretKey, random);  
	     byte [] plainData = cipher.doFinal(cipherData);  
	    System.out.println( "plainText : "  +  new  String(plainData));  
	     //Hello , world !   
	}
}
