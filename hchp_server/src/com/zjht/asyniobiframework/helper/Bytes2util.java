package com.zjht.asyniobiframework.helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Bytes2util {

	private static Logger logger = LoggerFactory.getLogger(Bytes2util.class);  
	  
    public static byte[] biginttobytes(int value) {  
        byte[] stream = new byte[4];  
        for (int i = 0; i < 4; i++) {  
            stream[i] = (byte) ((value & (0xFF << (4 - i - 1) * 8)) >> ((4 - i - 1) * 8));  
        }  
        outputHex(stream, 16);  
        return stream;  
    }  
  
    public static byte[] bigshorttobytes(short value) {  
        byte stream[] = new byte[2];  
        for (int i = 0; i < 2; i++) {  
            stream[i] = (byte) ((value & (0xFF << (2 - i - 1) * 8)) >> ((2 - i - 1) * 8));  
        }  
        outputHex(stream, 16);  
        return stream;  
    }  
  
    public static byte[] smallinttobytes(int value) {  
        byte stream[] = new byte[4];  
        for (int i = 0; i < 4; i++) {  
            stream[4 - i - 1] = (byte) ((value & (0xFF << (4 - i - 1) * 8)) >> ((4 - i - 1) * 8));  
        }  
        outputHex(stream, 16);  
        return stream;  
    }  
  
    public static byte[] smallshorttobytes(short value) {  
        byte stream[] = new byte[2];  
        for (int i = 0; i < 2; i++) {  
            stream[2 - i - 1] = (byte) ((value & (0xFF << (2 - i - 1) * 8)) >> ((2 - i - 1) * 8));  
        }  
        outputHex(stream, 16);  
        return stream;  
    }  
  
    public static void outputHex(byte[] stream, int number) {  
        String content = "stream display, length=" + stream.length + "\n";  
        for (int i = 0; i < stream.length; i++) {  
            if (i / number != 0 && i % number == 0) {  
                content += "\n";  
            }  
            String tempstr = Integer.toHexString(stream[i] & 0xFF)  
                    .toUpperCase();  
            if (tempstr.length() == 1)  
                tempstr = "0" + tempstr;  
            content += tempstr + " ";  
        }  
        logger.debug(content);  
    }  
}
