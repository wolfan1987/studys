package org.andrewliu.javanet.security.despbe;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Random;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

import org.andrewliu.javanet.security.AbstractSingleBaseEnDecryptCoder;

/**
 * 对称加密的另一个算法——PBE
 * 大白+大白=白胖胖   大=6，白=1，白胖胖=122
 * PBE——Password-based encryption（基于密码加密）。其特点在于口令由用户自己掌管，
 * 不借助任何物理媒体；采用随机数（这里我们叫做盐）杂凑多重加密等方法保证数据的安全性。是一种简便的加密方式。  
 * 甲方：
 * 1、公布密码；
 * 2、构建盐; 
 * 3、使用密码、盐对数据加密；
 * 4、发送盐、加密数据；
 * 乙方：
 * 5、使用盐、密码对数据解密
 * @author de
 *
 */
public  abstract class PBEEnDecryptCoder  extends AbstractSingleBaseEnDecryptCoder{

	/**
	 * 支持以下任意一种算法
	 * PBEWithMD5AndDES
	 * PBEWithMD5AndTripleDES 
	 * PBEWithSHA1AndDESede
	 * PBEWithSHA1AndRC2_40
	 */
	public static final String ALGORITHM = "PBEWITHMD5andDES";
	
	/**
	 * 盐初始化
	 * @return
	 */
	public static byte[]  initSalt(){
		byte[] salt = new byte[8];
		Random random = new Random();
		random.nextBytes(salt);
		return salt;
	}
		
	/**
	 * 转换密钥
	 * @param password
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 */
	private static Key toKey(String password) throws NoSuchAlgorithmException, InvalidKeySpecException{
		PBEKeySpec keySpec = new PBEKeySpec(password.toCharArray());
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM);
		SecretKey secretKey = keyFactory.generateSecret(keySpec);
		return secretKey;
	}
	
	/**
	 * 
	 * @param data  要加密的数据
	 * @param password   密码
	 * @param salt     盐值
	 * @return
	 * @throws InvalidKeySpecException 
	 * @throws NoSuchAlgorithmException 
	 * @throws NoSuchPaddingException 
	 * @throws InvalidAlgorithmParameterException 
	 * @throws InvalidKeyException 
	 * @throws BadPaddingException 
	 * @throws IllegalBlockSizeException 
	 */
	public static byte[] encrypt(byte[] data,String password,byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException{
		Key key = toKey(password);
		PBEParameterSpec paramSpec = new PBEParameterSpec(salt,100);
		Cipher cipher = Cipher.getInstance(ALGORITHM);
		cipher.init(Cipher.ENCRYPT_MODE, key,paramSpec);
		return cipher.doFinal(data);
	}
	
	/**
	 * 解密
	 * @param data  需要解密的数据
	 * @param password  密码
	 * @param salt     盐值
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 * @throws NoSuchPaddingException
	 * @throws InvalidKeyException
	 * @throws InvalidAlgorithmParameterException
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 */
	public static byte[]  decrypt(byte[] data,String password,byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException{
		Key key = toKey(password);
		PBEParameterSpec paramSpec = new PBEParameterSpec(salt,100);
		Cipher cipher = Cipher.getInstance(ALGORITHM);
		cipher.init(Cipher.DECRYPT_MODE, key,paramSpec);
		return cipher.doFinal(data);
	}
	
}
