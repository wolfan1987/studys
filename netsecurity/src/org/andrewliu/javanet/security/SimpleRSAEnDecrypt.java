package org.andrewliu.javanet.security;

import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.SignatureException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import sun.misc.BASE64Encoder;

/**
 * 使用RSA非对称加密/解密：
 * 
 * RSA算法是非对称加密算法的典型代表，既能加密、又能解密。和对称加密算法比如DES的明显区别在于用于加密、
 * 解密的密钥是不同的。使用RSA算法，
 * 只要密钥足够长(一般要求1024bit)，加密的信息是不能被破解的。 天真
 * 用户通过https协议访问服务器时，就是使用非对称加密算法进行数据的加密、解密操作的。
 * 
 * 服务器发送数据给客户端时使用私钥（private key）进行加密，并且使用加密之后的数据和私钥生成数字签名（digital
 * signature）并发送给客户端。客户端接收到服务器发送的数据会使用公钥（public
 * key）对数据来进行解密，并且根据加密数据和公钥验证数字签名的有效性，防止加密数据在传输过程中被第三方进行了修改。
 * 
 * 客户端发送数据给服务器时使用公钥进行加密，服务器接收到加密数据之后使用私钥进行解密。
 * 
 * 
 * 
 * 1. 创建密钥对KeyPair：

Java代码
KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance( "rsa" );  
keyPairGenerator.initialize( 1024 );   //密钥长度推荐为1024位.   
KeyPair keyPair = keyPairGenerator.generateKeyPair();  
2. 获取公钥/私钥：

Java代码
PublicKey publicKey = keyPair.getPublic();  
PrivateKey privateKey = keyPair.getPrivate();  
3. 服务器数据使用私钥加密：

Java代码
Cipher cipher = Cipher.getInstance( "rsa" );  
cipher.init(Cipher.ENCRYPT_MODE, privateKey,  new  SecureRandom());  
byte [] cipherData = cipher.doFinal(plainText.getBytes());  
4. 用户使用公钥解密：

Java代码
cipher.init(Cipher.DECRYPT_MODE, publicKey,  new  SecureRandom());  
byte [] plainData = cipher.doFinal(cipherData);  
5. 服务器根据私钥和加密数据生成数字签名：

Java代码
Signature signature  = Signature.getInstance( "MD5withRSA" );  
signature.initSign(privateKey);  
signature.update(cipherData);  
byte [] signData = signature.sign();  
6. 用户根据公钥、加密数据验证数据是否被修改过：

Java代码
signature.initVerify(publicKey);  
signature.update(cipherData);  
boolean  status = signature.verify(signData);  
 * 
 * @author de
 *
 */
public class SimpleRSAEnDecrypt {

	public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, SignatureException {
		String plainText =  "Hello , world !" ;  
	      
	    KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance( "rsa" );  
	    keyPairGenerator.initialize( 1024 );  
	    KeyPair keyPair = keyPairGenerator.generateKeyPair();  
	      
	    PublicKey publicKey = keyPair.getPublic();  
	    PrivateKey privateKey = keyPair.getPrivate();  
	      
	    Cipher cipher = Cipher.getInstance( "rsa" );  
	    SecureRandom random =  new  SecureRandom();  
	      
	    cipher.init(Cipher.ENCRYPT_MODE, privateKey, random);  
	     byte [] cipherData = cipher.doFinal(plainText.getBytes());  
	    System.out.println( "cipherText : "  +  new  BASE64Encoder().encode(cipherData));  
	     //yQ+vHwHqXhuzZ/N8iNg=   
	  
	    cipher.init(Cipher.DECRYPT_MODE, publicKey, random);  
	     byte [] plainData = cipher.doFinal(cipherData);  
	    System.out.println( "plainText : "  +  new  String(plainData));  
	     //Hello , world !   
	      
	    Signature signature  = Signature.getInstance( "MD5withRSA" );  
	    signature.initSign(privateKey);  
	    signature.update(cipherData);  
	     byte [] signData = signature.sign();  
	    System.out.println( "signature : "  +  new  BASE64Encoder().encode(signData));  
	     ///t9ewo+KYCWKOgvu5QQ=   
	  
	    signature.initVerify(publicKey);  
	    signature.update(cipherData);  
	     boolean  status = signature.verify(signData);  
	    System.out.println( "status : "  + status);  
	     //true   
	}
}
