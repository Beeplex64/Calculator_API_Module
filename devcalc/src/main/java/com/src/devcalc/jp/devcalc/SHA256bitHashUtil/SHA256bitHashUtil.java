package com.src.devcalc.jp.devcalc.SHA256bitHashUtil;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class SHA256bitHashUtil {
	
	private static final String ALGORITHM = "PBKDF2WithHmacSHA256";
	private static final int ITERATION_COUNT = 10000;
	private static final int KEY_LENGTH = 256;
	
	public static String getHashInsertDBString(String target, String salt) {
		char[] targetCharAry1 = target.toCharArray();
		byte[] hashedSalt = F_GetHashedSalt(salt);
		
		PBEKeySpec keySpec = new PBEKeySpec(targetCharAry1, hashedSalt, ITERATION_COUNT, KEY_LENGTH);
		SecretKeyFactory skf;
		
		try {
			skf = SecretKeyFactory.getInstance(ALGORITHM);
		}catch(NoSuchAlgorithmException noSuchAlgorithmException) {
			throw new RuntimeException(noSuchAlgorithmException);
		}
		
		SecretKey secretKey;
		try {
			secretKey = skf.generateSecret(keySpec);
		}catch(InvalidKeySpecException invalidKeySpecException) {
			throw new RuntimeException(invalidKeySpecException);
		}
		byte[] targetCharAry2 = secretKey.getEncoded();
		StringBuilder base64 = new StringBuilder(64);
		for(byte Byte:targetCharAry2) {
			base64.append(String.format("%02x", Byte & 0xff));
		}
		return base64.toString();
	}
	
	private static byte[] F_GetHashedSalt(String salt) {
		MessageDigest messageDigest;
		try {
			messageDigest = MessageDigest.getInstance("SHA-256");
		}catch(NoSuchAlgorithmException noSuchAlgorithmException) {
			throw new RuntimeException(noSuchAlgorithmException);
		}
		messageDigest.update(salt.getBytes());
		return messageDigest.digest();
	}

}
