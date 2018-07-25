package org.andrewliu.javanet.security.despbe;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.andrewliu.javanet.security.AbstractSingleBaseEnDecryptCoder;

/**
 * 非对称加密算法:RSA
 * 这种加密算法的特点主要是密钥的变化，上文我们看到DES只有一个密钥。相当于只有一把钥匙，
 * 如果这把钥匙丢了，数据也就不安全了。RSA同时有两把钥匙，公钥与私钥。同时支持数字签名。数字签名的意义在于，
 * 对传输过来的数据进行校验。确保数据在传输工程中不被修改。
 * 流程分析：  1. 甲方构建密钥对儿，将公钥公布给乙方，将私钥保留。 
 *            2. 甲方使用私钥加密数据，然后用私钥对加密后的数据签名，发送给乙方签名以及加密后的数据；
 *              乙方使用公钥、签名来验证待解密数据是否有效，如果有效使用公钥对数据解密。
 *            3. 乙方使用公钥加密数据，向甲方发送经过加密后的数据；甲方获得加密数据，通过私钥解密
 * 加密过程：
 * 1、甲方构建密钥对；
 * 2、                             公布公钥给乙方；
 * 3、甲方使用密钥加密数据；
 * 4、使用私钥对加密后的数据签名；
 * 5、发送加密数据、数字签名；
 * 6、		                乙方使用公钥、签名验证等待解密的数据；
 * 7、				乙方使用公钥解密数据；
 * 8、                             乙方使用公钥加密数据；
 * 9、				乙方发送加密数据给甲方；
 * 10、甲方使用私钥解密数据;
 * 
 * RSA 安全编码组件
 * 
 * 数字信封  
  数字信封用加密技术来保证只有特定的收信人才能阅读信的内容。 
  流程：  
  信息发送方采用对称密钥来加密信息，然后再用接收方的公钥来加密此对称密钥（这部分称为数字信封），
    再将它和信息一起发送给接收方；接收方先用相应的私钥打开数字信封，得到对称密钥，然后使用对称密钥再解开信息
 * @author de
 *
 */
public abstract class RSAEnDecryptCoder extends AbstractSingleBaseEnDecryptCoder{

	public static final String 	KEY_ALGORITHM = "RSA";
	/**
	 * 指定数字签名算法 
	 */
	public static final String SIGNATURE_ALGORITHM="MD5withRSA";
	/**
	 * 公钥
	 */
	private static final String PUBLIC_KEY="RSAPublicKey";
	/**
	 * 私钥
	 */
	private static final String PRIVATE_KEY="RSAPrivateKey";
	
	/**
	 * 用私钥对信息生成数字签名
	 * @param data  要签名的数据
	 * @param privateKey  私钥
	 * @return
	 * @throws IOException 
	 * @throws NoSuchAlgorithmException 
	 * @throws InvalidKeySpecException 
	 * @throws InvalidKeyException 
	 * @throws SignatureException 
	 */
	public static String sign(byte[] data,String privateKey) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, SignatureException{
		//解密由base64编码的私钥
		byte[] keyBytes = decryptBASE64(privateKey);
		//构造PKCS8EncodedKeySpec对象
		PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
		//KEY_ALGORITHM 指定加密算法
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		
		//取得钥匙对象
		PrivateKey priKey = keyFactory.generatePrivate(pkcs8KeySpec);
		//用私钥对信息生成数字签名
		Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
		signature.initSign(priKey);
		signature.update(data);
		
		//返回用BASE64再次加密后的已签名的信息
		return encryptBASE64(signature.sign());
	}
	
	/**
	 * 校验数字签名
	 * @param data  加密数据
	 * @param publicKey  公钥
	 * @param sign    数字签名信息
	 * @return  校验成功返回true,失败返回false
	 * @throws IOException 
	 * @throws NoSuchAlgorithmException 
	 * @throws InvalidKeySpecException 
	 * @throws InvalidKeyException 
	 * @throws SignatureException 
	 */
	public static boolean verify(byte[] data,String publicKey,String sign) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, SignatureException{
		//解密由base64编码的公钥
		byte[]  keyBytes = decryptBASE64(publicKey);
		//构造X509EncodedKeySpec对象
		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
		//KEY_ALGORITHM 指定的加密算法
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		//取得公钥匙对象
		PublicKey pubKey = keyFactory.generatePublic(keySpec);
		//得到签名实例
		Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
		signature.initVerify(pubKey);
		signature.update(data);
		//将用BASE64解密后的签名进行验证
		return signature.verify(decryptBASE64(sign));
	}
	
	/**
	 * 解密：
	 * 用私钥解密
	 * @param data
	 * @param key
	 * @return
	 * @throws IOException 
	 * @throws NoSuchAlgorithmException 
	 * @throws InvalidKeySpecException 
	 * @throws NoSuchPaddingException 
	 * @throws InvalidKeyException 
	 * @throws BadPaddingException 
	 * @throws IllegalBlockSizeException 
	 */
	public static byte[]  decryptByPrivateKey(byte[] data,String key) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException{
	
		//对密钥解密
		byte[]  keyBytes = decryptBASE64(key);
		//取得私钥
		PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		Key privateKey = keyFactory.generatePrivate(pkcs8KeySpec);
		
		//对数据解密
		Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
		cipher.init(Cipher.DECRYPT_MODE, privateKey);
		return cipher.doFinal(data);
	}
	
	/**
	 * 解密，用公钥解密
	 * @param data
	 * @param key
	 * @return
	 * @throws IOException 
	 * @throws NoSuchAlgorithmException 
	 * @throws InvalidKeySpecException 
	 * @throws NoSuchPaddingException 
	 * @throws InvalidKeyException 
	 * @throws BadPaddingException 
	 * @throws IllegalBlockSizeException 
	 */
	public static byte[] decryptByPublicKey(byte[] data,String key) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException{
		//对密钥解密
		byte[]  keyBytes = decryptBASE64(key);
		//取得公钥
		X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		Key publicKey = keyFactory.generatePublic(x509KeySpec);
		//对数据解密
		Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
		cipher.init(Cipher.DECRYPT_MODE, publicKey);
		return cipher.doFinal(data);
	}
	
	/**
	 * 加密：用公钥加密
	 * @param data
	 * @param key
	 * @return
	 * @throws IOException 
	 * @throws NoSuchAlgorithmException 
	 * @throws InvalidKeySpecException 
	 * @throws NoSuchPaddingException 
	 * @throws InvalidKeyException 
	 * @throws BadPaddingException 
	 * @throws IllegalBlockSizeException 
	 */
	public static byte[]  encryptByPublicKey(byte[] data,String key) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException{
		
		//对公钥解密
		byte[] keyBytes = decryptBASE64(key);
		//取得公钥
		X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
		
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		Key publicKey = keyFactory.generatePublic(x509KeySpec);
		//对数据加密
		Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);;
		return cipher.doFinal(data);
	}
	
	/**
	 * 加密： 用私钥加密
	 * @param data
	 * @param key
	 * @return
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 * @throws NoSuchPaddingException
	 * @throws InvalidKeyException
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 */
	public static byte[]  encryptByPrivateKey(byte[] data,String key) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException{
		//对密钥解密
		byte[] keyBytes = decryptBASE64(key);
		//取得私钥
		PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		Key privateKey = keyFactory.generatePrivate(pkcs8KeySpec);
		//对数据加密
		Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
		cipher.init(Cipher.ENCRYPT_MODE, privateKey);
		return cipher.doFinal(data);
	}
	/**
	 * 取得私钥
	 * @param keyMap
	 * @return
	 */
	public static String getPrivateKey(Map<String,Object> keyMap){
		Key key = (Key) keyMap.get(PRIVATE_KEY);
		return encryptBASE64(key.getEncoded());
	}
	
	/**
	 * 取得公钥
	 * @param keyMap
	 * @return
	 */
	public static String getPublicKey(Map<String,Object> keyMap){
		Key key = (Key) keyMap.get(PUBLIC_KEY);
		return encryptBASE64(key.getEncoded());
	}
	/**
	 * 初始化密钥
	 * @return
	 * @throws NoSuchAlgorithmException 
	 */
	public static Map<String,Object>  initKey() throws NoSuchAlgorithmException{
		KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
		keyPairGen.initialize(1024);
		KeyPair keyPair = keyPairGen.generateKeyPair();
		//公钥
		RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
		//私钥
		RSAPrivateKey privateKey = (RSAPrivateKey)keyPair.getPrivate();
		Map<String,Object> keyMap = new HashMap<String,Object>();
		keyMap.put(PUBLIC_KEY, publicKey);
		keyMap.put(PRIVATE_KEY, privateKey);
		return keyMap;
	}
}
