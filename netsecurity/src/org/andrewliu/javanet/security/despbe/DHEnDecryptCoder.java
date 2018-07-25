package org.andrewliu.javanet.security.despbe;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyAgreement;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.interfaces.DHPrivateKey;
import javax.crypto.interfaces.DHPublicKey;
import javax.crypto.spec.DHParameterSpec;

import org.andrewliu.javanet.security.AbstractSingleBaseEnDecryptCoder;

/**
 * 基于DH非对称加密算法对数据进行加解密.
 * DH加密算法，一种适基于密钥一致协议的加密算法
   Diffie-Hellman算法(D-H算法)，密钥一致协议。是由公开密钥密码体制的奠基人Diffie和Hellman所提出的一种思想。简单的说就是允许两名用户
     在公开媒体上交换信息以生成"一致"的、可以共享的密钥。换句话说，就是由甲方产出一对密钥（公钥、私钥），乙方依照甲方公钥产生乙方密钥
     对（公钥、私钥）。以此为基线，作为数据传输保密基础，同时双方使用同一种对称加密算法构建本地密钥（SecretKey）对数据加密。这样，在
     互通了本地密钥（SecretKey）算法后，甲乙双方公开自己的公钥，使用对方的公钥和刚才产生的私钥加密数据，同时可以使用对方的公钥和自己
      的私钥对数据解密。不单单是甲乙双方两方，可以扩展为多方共享数据通讯，这样就完成了网络交互数据的安全通讯！该算法源于中国的同余定理
   ——中国馀数定理
 处理流程：
 1、甲方构造公私密钥，并公布公钥给乙方；
 2、甲乙双方约定数据加密算法；
 3、				乙方使用甲方公钥构造自己的公钥和私钥；
 4、				乙方公布公钥给甲方；
 5、甲方使用自己的私钥、乙方的公钥、双方约定的加密算法构建本地密钥；
 6、甲方使用5中生成的本地密钥对数据加密；
 7、甲方向乙方发送加密后的数据；
 8、				乙方使用私钥、甲方公钥、双方约定的数据加密算法构建本地密钥；
 9、				乙方使用8中生成的本地密钥对甲方发来的数据解密；
 10、			乙方使用8中生成的本地密钥对要发送到甲方的数据加密
 11、			向甲方发送加密数据；
 12、甲方使用5生成的本地密钥对乙方传来的数据解密；
 * @author de
 *
 */
public abstract class DHEnDecryptCoder  extends AbstractSingleBaseEnDecryptCoder {

	/**
	 * 指定算法类型
	 */
	public static final String ALGORITHM="DH";
	/**
	 * 默认密钥字节数
	 * DH
	 * Default Keysize 1024
	 * Keysize must be a multiple of 64, ranging from 512 to 1024 (inclusive).
	 */
	private static final int KEY_SIZE = 1024;
	
	/**
	 * DH加密下需要一种对称加密算法对数据加密，这里我们使用DES，也可以使用其他对称加密算法 。
	 */
	public  static final String SECRET_ALGORITHM = "DES";
	private static final String PUBLIC_KEY="DHPublicKey";
	private static final String PRIVATE_KEY="DHPrivateKey";
	
	/**
	 * 初始化甲方密钥
	 * @return
	 * @throws NoSuchAlgorithmException 
	 */
	public static Map<String,Object> initKey() throws NoSuchAlgorithmException{
		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(ALGORITHM);
		keyPairGenerator.initialize(KEY_SIZE);
		KeyPair keyPair = keyPairGenerator.generateKeyPair();
		//甲方公钥
		DHPublicKey  publicKey = (DHPublicKey)keyPair.getPublic();
		//甲方私钥
		DHPrivateKey privateKey = (DHPrivateKey)keyPair.getPrivate();
		Map<String,Object>  keyMap = new HashMap<String,Object>();
		keyMap.put(PUBLIC_KEY, publicKey);
		keyMap.put(PRIVATE_KEY, privateKey);
		return keyMap;
	}
	
	/**
	 * 初始化乙方密钥
	 * @param key  甲方公钥
	 * @return
	 * @throws IOException 
	 * @throws NoSuchAlgorithmException 
	 * @throws InvalidKeySpecException 
	 * @throws InvalidAlgorithmParameterException 
	 */
	public static Map<String,Object> initKey(String key) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException, InvalidAlgorithmParameterException{
		//解析甲方公钥
		byte[]  keyBytes = decryptBASE64(key);
		X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
		PublicKey pubKey = keyFactory.generatePublic(x509KeySpec);
		
		//由甲方公钥构建乙方密钥
		DHParameterSpec dhParamSpec = ((DHPublicKey)pubKey).getParams();
		
		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(keyFactory.getAlgorithm());
		keyPairGenerator.initialize(dhParamSpec);
		KeyPair keyPair = keyPairGenerator.generateKeyPair();
		//乙方公钥
		DHPublicKey publicKey = (DHPublicKey) keyPair.getPublic();
		//乙方私钥
		DHPrivateKey privateKey = (DHPrivateKey)keyPair.getPrivate();
		Map<String,Object>  keyMap = new HashMap<String,Object>(2);
		keyMap.put(PUBLIC_KEY, publicKey);
		keyMap.put(PRIVATE_KEY, privateKey);
		return keyMap;
		
	}
	/**
	 * 乙方加密
	 * @param data  待加密数据
	 * @param publicKey   甲方公钥
	 * @param privateKey   乙方私钥
	 * @return
	 * @throws IOException 
	 * @throws InvalidKeySpecException 
	 * @throws NoSuchAlgorithmException 
	 * @throws InvalidKeyException 
	 * @throws NoSuchPaddingException 
	 * @throws BadPaddingException 
	 * @throws IllegalBlockSizeException 
	 */
	public static byte[]  encrypt(byte[] data,String publicKey,String privateKey) throws InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException, IOException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException{
		//生成本地密钥
		SecretKey secretKey = getSecretKey(publicKey,privateKey);
		//数据加密
		Cipher cipher = Cipher.getInstance(secretKey.getAlgorithm());
		cipher.init(Cipher.ENCRYPT_MODE, secretKey);
		return cipher.doFinal(data);
	}
	
	
//	public static byte[] decrypt(byte[] data,String publicKey,String privateKey){
//		//生成本地密钥
//		SecretKey secretKey = getSecretKey(publicKey,privateKey);
//		//数据解密
//		Cipher cipher = Cipher.getInstance(secretKey.getAlgorithm());
//		cipher.init(Cipher.DECRYPT_MODE, secretKey);
//		return cipher.doFinal(data);
//	}
	
	/**
	 * 解密  
	 * @param data  待解密数据  
	 * @param publicKey  乙方公钥
	 * @param privateKey  乙方私钥
	 * @return
	 * @throws IOException 
	 * @throws InvalidKeySpecException 
	 * @throws NoSuchAlgorithmException 
	 * @throws InvalidKeyException 
	 * @throws NoSuchPaddingException 
	 * @throws BadPaddingException 
	 * @throws IllegalBlockSizeException 
	 */
	public static byte[] decrypt(byte[] data,String publicKey,String privateKey) throws InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException, IOException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException{
	
		//生成本地密钥
		SecretKey secretKey = getSecretKey(publicKey,privateKey);
		//数据解密
		Cipher cipher = Cipher.getInstance(secretKey.getAlgorithm());
		cipher.init(Cipher.DECRYPT_MODE, secretKey);
		return cipher.doFinal(data);
	}
	
	/**
	 * 构建密钥
	 * @param publicKey  公钥
	 * @param privateKey  私钥
	 * @return
	 * @throws IOException 
	 * @throws NoSuchAlgorithmException 
	 * @throws InvalidKeySpecException 
	 * @throws InvalidKeyException 
	 */
	private static SecretKey getSecretKey(String publicKey,String privateKey) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException{
		byte[]  pubKeyBytes = decryptBASE64(publicKey);
		KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
		X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(pubKeyBytes);
		
		PublicKey pubKey = keyFactory.generatePublic(x509KeySpec);
		//初始化私钥
		byte[] priKeyBytes = decryptBASE64(privateKey);
		PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(priKeyBytes);
		Key priKey = keyFactory.generatePrivate(pkcs8KeySpec);
		KeyAgreement keyAgree = KeyAgreement.getInstance(keyFactory.getAlgorithm());
		keyAgree.init(priKey);
//		SecureRandom secureRandom  = new SecureRandom();
//		keyAgree.init(priKey, secureRandom);
		keyAgree.doPhase(pubKey, true);
		//生成本地密钥
		SecretKey secretKey = keyAgree.generateSecret(SECRET_ALGORITHM);
		return secretKey;
	}
	
	/**
	 * 取得私钥
	 * @param keyMap
	 * @return
	 */
	public static String  getPrivateKey(Map<String,Object> keyMap){
		Key key = (Key)keyMap.get(PRIVATE_KEY);
		return encryptBASE64(key.getEncoded());
	}
	
	/**
	 * 取得公钥
	 * @param keyMap
	 * @return
	 */
	public static String getPublicKey(Map<String,Object> keyMap){
		Key key = (Key)keyMap.get(PUBLIC_KEY);
		return encryptBASE64(key.getEncoded());
	}
	
	
}
