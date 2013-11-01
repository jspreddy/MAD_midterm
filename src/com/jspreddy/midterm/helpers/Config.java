/**
 * Midterm
 * Filename: Config.java
 * Sai Phaninder Reddy Jonnala
 */
package com.jspreddy.midterm.helpers;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class Config {
	public static final String SEED = "127302222936259572345";
	
	public static final String getUid(String username){
		return Config.md5(username + SEED);	
	}
	
	public static final String md5(final String s) {
	    try {
	        // Create MD5 Hash
	        MessageDigest digest = java.security.MessageDigest
	                .getInstance("MD5");
	        digest.update(s.getBytes());
	        byte messageDigest[] = digest.digest();
	 
	        // Create Hex String
	        StringBuffer hexString = new StringBuffer();
	        for (int i = 0; i < messageDigest.length; i++) {
	            String h = Integer.toHexString(0xFF & messageDigest[i]);
	            while (h.length() < 2)
	                h = "0" + h;
	            hexString.append(h);
	        }
	        return hexString.toString();
	 
	    } catch (NoSuchAlgorithmException e) {
	        e.printStackTrace();
	    }
	    return "";
	}
}