package com.server.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Util {
	public static String md5(String encryptKey, String para){
		String key = para + encryptKey;
		return getMD5(key);
	}

	 public static String getMD5(String message){
	        try {
	            MessageDigest md = MessageDigest.getInstance("MD5");
	            byte[] b = md.digest(message.getBytes());
	            return byteToHexString(b);
	        } catch (NoSuchAlgorithmException e) {
	            e.printStackTrace();
	            return null;
	        }
	    }
	 
	    private static String byteToHexString(byte[] tmp) {
			char hexDigits[] = { 
				     '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd',  'e', 'f'};    
		    String s;
		    char str[] = new char[16 * 2]; 
		    int k = 0;
		    for (int i = 0; i < 16; i++) { 
		        byte byte0 = tmp[i];
		        str[k++] = hexDigits[byte0 >>> 4 & 0xf];
		        str[k++] = hexDigits[byte0 & 0xf];
		    }
		    s = new String(str); 
		    return s;
	    }
	    
}
