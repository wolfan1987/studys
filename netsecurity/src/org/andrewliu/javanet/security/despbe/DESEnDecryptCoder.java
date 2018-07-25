package org.andrewliu.javanet.security.despbe;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import org.andrewliu.javanet.security.AbstractSingleBaseEnDecryptCoder;


/**
 * DES安全编码套件
 * 支持 DES、DESede(TripleDES,就是3DES)、AES、Blowfish、RC2、RC4(ARCFOUR)
 * DES                  key size must be equal to 56
 * DESede(TripleDES)    key size must be equal to 112 or 168
 * AES                  key size must be equal to 128, 192 or 256,bu t 192 and 256 bits may not be available
 * Blowfish             key size must be multiple of 8, and can only range from 32 to 448 (inclusive)
 * RC2                  key size must be between 40 and 1024 bits
 * RC4(ARCFOUR)         key size must be between 40 and 1024 bits
 * 具体内容 需要关注 JDK Document http://.../docs/technotes/guides/security/SunProviders.html
 * 甲方：
 * 1、构建密钥
 * 2、公布密钥
 * 3、使用密钥对数据加密
 * 4、发送加密数据
 * 乙方：
 * 5、使用密钥对数据解密
 * @author de
 *
 */
public abstract class DESEnDecryptCoder extends AbstractSingleBaseEnDecryptCoder {
	
	/**
	 * ALGORITHM 算法:
	 * 可替换为以下任意一种算法，同时key值的size相应改变:
	* DES                  key size must be equal to 56
	* DESede(TripleDES)    key size must be equal to 112 or 168
	* AES                  key size must be equal to 128, 192 or 256,bu t 192 and 256 bits may not be available
	* Blowfish             key size must be multiple of 8, and can only range from 32 to 448 (inclusive)
	* RC2                  key size must be between 40 and 1024 bits
	* RC4(ARCFOUR)         key size must be between 40 and 1024 bits
	* 
	* 在Key toKey(byte[] key)方法中使用下述代码
	* SecretKey secretKey = new SecretKeySpec(key, ALGORITHM); 替换
	* DESKeySpec dks = new DESKeySpec(key);
	* SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM)
	* SecretKey secretKey = keyFactory.generateSecret(dks);
	* 
	* DES有很多同胞兄弟，如DESede(TripleDES)、AES、Blowfish、RC2、RC4(ARCFOUR)。
	* 这里就不过多阐述了，大同小异，只要换掉ALGORITHM换成对应的值，
	* 同时做一个代码替换SecretKey secretKey = new SecretKeySpec(key, ALGORITHM);就可以了，此外就是密钥长度不同
	 */
	public static final  String ALGORITHM = "DES";
	
	/**
	 * 转换密钥
	 * @param key
	 * @return
	 * @throws InvalidKeyException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 */
	private static Key toKey(byte[] key) throws InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException{
		DESKeySpec dks = new DESKeySpec(key);
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM);
		SecretKey secretKey = keyFactory.generateSecret(dks);
		// 当使用其他对称加密算法时，如AES、Blowfish等算法时，用下述代码替换上 述三行代码
		// SecretKey secretKey = new SecretKeySpec(key, ALGORITHM);
		return  secretKey;
	}
	/**
	 * 解密
	 * @param data
	 * @param key
	 * @return
	 * @throws InvalidKeyException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 * @throws IOException
	 * @throws NoSuchPaddingException
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 */
	public static byte[]  decrypt(byte[] data,String key) throws InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException, IOException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException{
		Key k = toKey(decryptBASE64(key));
		Cipher cipher = Cipher.getInstance(ALGORITHM);
		cipher.init(Cipher.DECRYPT_MODE, k);
		return cipher.doFinal(data);
		
	}
	
	/**
	 * 加密
	 * @param data
	 * @param key
	 * @return
	 * @throws InvalidKeyException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 * @throws IOException
	 * @throws NoSuchPaddingException
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 */
	public static byte[] encrypt(byte[] data,String key) throws InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException, IOException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException{
		Key k = toKey(decryptBASE64(key));
		Cipher cipher = Cipher.getInstance(ALGORITHM);
		cipher.init(Cipher.ENCRYPT_MODE, k);
		return cipher.doFinal(data);
	}
	/**
	 * 生成密钥
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws IOException
	 */
	public static String initKey() throws NoSuchAlgorithmException, IOException{
		return initKey(null);
	}
	
	/**
	 * 生成密钥
	 * @param seed
	 * @return
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 */
	public static String initKey(String seed) throws IOException, NoSuchAlgorithmException{
		SecureRandom secureRandom = null;
		if(seed != null){
			secureRandom = new SecureRandom(decryptBASE64(seed));
		}else{
			secureRandom = new SecureRandom();
		}
		
		KeyGenerator kg = KeyGenerator.getInstance(ALGORITHM);
		kg.init(secureRandom);
		
		SecretKey secretKey = kg.generateKey();
		//对字符串加密生成密钥，这是比较常见的密钥生成方式
		return encryptBASE64(secretKey.getEncoded());
	}
	
	
	

}
