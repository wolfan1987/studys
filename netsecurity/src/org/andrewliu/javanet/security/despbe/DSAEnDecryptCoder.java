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
import java.security.SecureRandom;
import java.security.Signature;
import java.security.SignatureException;
import java.security.interfaces.DSAPrivateKey;
import java.security.interfaces.DSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

import org.andrewliu.javanet.security.AbstractSingleBaseEnDecryptCoder;

/**
 * RSA 非对称加密算法,一般用来对进行数字签名
 * DSA   DSA-Digital Signature Algorithm 是Schnorr和ElGamal签名算法的变种，
 * 被美国NIST作为 DSS(DigitalSignature Standard)。简单的说，这是一种更高级的验证方式，
 * 用作数字签名。不单单只有公钥、私钥，还有数字签名。私钥加密生成数字签名，
 * 公钥验证数据及签名。如果数据和签名不匹配则认为验证失败！
 * 数字签名的作用就是校验数据在传输过程中不被修改。数字签名，是单向加密的升级！
 * 1、甲方生成密钥对（公钥/私钥）
 * 2、公布公钥给乙方
 * 3、甲方使用私钥对数据签名
 * 4、甲方发送签名、数据给乙方
 * 5、			乙方使用甲方的公钥及甲方发来的数字签名对数据进行验证
 * @author de
 *
 */
public abstract class DSAEnDecryptCoder extends AbstractSingleBaseEnDecryptCoder{

	public static final String ALGORITHM="DSA";
	/**
	 * 默认密钥字节数
	 * DSA
	 * Default Keysize 1024
	 * Keysize must be a multiple of 64, ranging from 512 to 1024 (inclusive).
	 */
	private static final int  KEY_SIZE = 1024;
	
	/**
	 * 默认种子
	 */
	private static final String DEFAULT_SEED="0f22507a10bbddd07d8a3082122966e3";
	private static final String PUBLIC_KEY="DSAPublicKey";
	private static final String PRIVATE_KEY="DSAPrivateKey";
	
	/**
	 * 用私钥对信息生成数字签名
	 * @param data  加密数据
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
		byte[]  keyBytes = decryptBASE64(privateKey);
		//构造PKCS8EncodedKeySpec对象
		PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
		//取私钥对象
		PrivateKey priKey = keyFactory.generatePrivate(pkcs8KeySpec);
		//用私钥对信息生成数字签名
		Signature signature = Signature.getInstance(keyFactory.getAlgorithm());
		signature.initSign(priKey);
		signature.update(data);
		
		return encryptBASE64(signature.sign());
	}
	
	
	/**
	 * 校验数字签名
	 * 
	 * @param data  加密数据
	 * @param publicKey  公钥
	 * @param sign   签名
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
		
		//构造x509EncodedKeySpec对象
		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
		
		//ALGORIHT 指定加密算法
		KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
		//取得公钥对象
		PublicKey pubKey = keyFactory.generatePublic(keySpec);
		Signature signature = Signature.getInstance(keyFactory.getAlgorithm());
		signature.initVerify(pubKey);
		signature.update(data);
		
		//验证签名是否正常
		return signature.verify(decryptBASE64(sign));
	}
	
	
	public static Map<String,Object>  initKey(String seed) throws NoSuchAlgorithmException{
		KeyPairGenerator keygen = KeyPairGenerator.getInstance(ALGORITHM);
		//初始化随机产生器
		SecureRandom secureRandom = new SecureRandom();
		secureRandom.setSeed(seed.getBytes());
		keygen.initialize(KEY_SIZE,secureRandom);
		
		KeyPair keys = keygen.genKeyPair();
		DSAPublicKey publicKey = (DSAPublicKey)keys.getPublic();
		DSAPrivateKey privateKey = (DSAPrivateKey)keys.getPrivate();
		Map<String,Object> map = new HashMap<String,Object>(2);
		map.put(PUBLIC_KEY, publicKey);
		map.put(PRIVATE_KEY, privateKey);
		return map;
	}
	/**
	 * 默认生成密钥：
	 * @return 密钥对象
	 * @throws NoSuchAlgorithmException 
	 */
	public static Map<String,Object> initKey() throws NoSuchAlgorithmException{
		return initKey(DEFAULT_SEED);
	}
	
	/**
	 * 取得私钥
	 * @param keyMap
	 * @return
	 */
	public static String getPrivateKey(Map<String,Object> keyMap){
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
