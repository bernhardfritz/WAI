package controllers;

import org.apache.commons.codec.binary.Hex;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class HashManager {
	
	private static MessageDigest algorythm;
	
	private static class SingletonHelper {
		private static final HashManager INSTANCE = new HashManager();
	}

	public static HashManager getInstance() {
		return SingletonHelper.INSTANCE;
	}
	
	private HashManager() { // creates a MessageDigest with MD5
		try {
			algorythm = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException nsae) {
			nsae.printStackTrace();
		}	
		
	}
	
	public String codeString(String x) { //encodes the given String with MessageDigest
		algorythm.update(x.getBytes());
		byte[] digest = algorythm.digest();
		String output = Hex.encodeHexString(digest);
		return output;
	}
}