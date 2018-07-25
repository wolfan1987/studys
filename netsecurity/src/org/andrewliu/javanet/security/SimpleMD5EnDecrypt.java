package org.andrewliu.javanet.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MessageDigest md5 = MessageDigest.getInstance( "md5" );  
2.  进行加密操作：

Java代码
byte [] cipherData = md5.digest(plainText.getBytes());  
3.  将其中的每个字节转成十六进制字符串：byte类型的数据最高位是符号位，通过和0xff进行与操作，转换为int类型的正整数。

Java代码
String toHexStr = Integer.toHexString(cipher &  0xff );  
4. 如果该正数小于16(长度为1个字符)，前面拼接0占位：确保最后生成的是32位字符串。

Java代码
builder.append(toHexStr.length() ==  1  ?  "0"  + toHexStr : toHexStr);  
5. 加密转换之后的字符串为：c0bb4f54f1d8b14caf6fe1069e5f93ad

6. 完整的MD5算法应用如下所示：
 * @author de
 *
 */
public class SimpleMD5EnDecrypt {

	public static void main(String[] args) throws NoSuchAlgorithmException {
		String plainText =  "Hello , world !" ;  
		//得到md5類型的信息-摘要加密算法
	    MessageDigest md5 = MessageDigest.getInstance( "md5" );  
	    //對數據進行加密 
	     byte [] cipherData = md5.digest(plainText.getBytes());  
	    StringBuilder builder =  new  StringBuilder();  
	     for ( byte  cipher : cipherData) {  
	    	 //将其中的每个字节转成十六进制字符串：byte类型的数据最高位是符号位，通过和0xff进行与操作，转换为int类型的正整数
	        String toHexStr = Integer.toHexString(cipher &  0xff );  
	        //如果该正数小于16(长度为1个字符)，前面拼接0占位：确保最后生成的是32位字符串。
	        builder.append(toHexStr.length() ==  1  ?  "0"  + toHexStr : toHexStr);  
	    }  
	     // 加密转换之后的字符串为：c0bb4f54f1d8b14caf6fe1069e5f93ad
	    System.out.println(builder.toString());  
	     //c0bb4f54f1d8b14caf6fe1069e5f93ad   
	}
	
}
