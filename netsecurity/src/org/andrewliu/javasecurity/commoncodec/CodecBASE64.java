package org.andrewliu.javasecurity.commoncodec;

import org.apache.commons.codec.binary.Base64;

public class CodecBASE64 {

	/** 

     *  

     * 创建日期2011-4-25上午10:12:38 

     * 修改日期 

     * 作者：dh *TODO 使用Base64加密算法加密字符串 

     *return 

     */  

    public static String encodeStr(String plainText){  

        byte[] b=plainText.getBytes();  

        Base64 base64=new Base64();  

        b=base64.encode(b);  

        String s=new String(b);  

        return s;  

    }  

      

    /** 

     *  

     * 创建日期2011-4-25上午10:15:11 

     * 修改日期 

     * 作者：dh     *TODO 使用Base64加密 

     *return 

     */  

    public static String decodeStr(String encodeStr){  

        byte[] b=encodeStr.getBytes();  

        Base64 base64=new Base64();  

        b=base64.decode(b);  

        String s=new String(b);  

        return s;  

    }  
    
    public static void main(String[] args) {
    	String result = CodecBASE64.encodeStr("andrewliu");
    	System.out.println("用Base64加密后="+result);
    	result = CodecBASE64.decodeStr(result);
    	System.out.println("解密后="+result);
    	
	}
}
