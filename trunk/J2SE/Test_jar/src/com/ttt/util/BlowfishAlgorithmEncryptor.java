package com.ttt.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class BlowfishAlgorithmEncryptor {
	public static final Integer BLOWFISH_IVLENGTH = 8;
	public static final Integer BLOWFISH_KEYSIZE = 128;
	public static final String BLOWFISH_ALGORITHM = "Blowfish";
	public static final String BLOWFISH_CBC_PKCS5PADDING = "Blowfish/CBC/PKCS5Padding";
	private Key key;

	/**
	 * Key size is 0-16 byte
	 * 
	 * @param enableInitiative
	 * @param keyValue
	 * @return
	 */
	public byte[] initialKey(boolean enableInitiative, byte[] keyValue) {
		if (enableInitiative) {
			try {
				KeyGenerator keyGenerator = KeyGenerator.getInstance(BLOWFISH_ALGORITHM);
				keyGenerator.init(BLOWFISH_KEYSIZE.intValue());
				setKey(keyGenerator.generateKey());
				return getKey().getEncoded();
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
		} else {
			setKey(new SecretKeySpec(keyValue, "Blowfish"));
			return getKey().getEncoded();
		}
	}

	public byte[] encrypt(byte[] plainBytes) {
		try {
			ByteArrayInputStream inputStream = new ByteArrayInputStream(plainBytes);
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			SecureRandom random = new SecureRandom();
			byte[] iv = new byte[BLOWFISH_IVLENGTH];
			random.nextBytes(iv);
			Cipher cipher = getCipher(Cipher.ENCRYPT_MODE, iv);
			outputStream.write(iv);
			CipherOutputStream cipherStream = new CipherOutputStream(outputStream, cipher);
			for (int i = inputStream.read(); i != -1; i = inputStream.read()) {
				cipherStream.write(i);
			}
			inputStream.close();
			cipherStream.close();
			outputStream.close();

			return outputStream.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public byte[] decrypt(byte[] cipherBytes) {
		try {
			ByteArrayInputStream inputStream = new ByteArrayInputStream(cipherBytes);
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			byte[] iv = new byte[BLOWFISH_IVLENGTH];
			inputStream.read(iv);
			Cipher cipher = getCipher(Cipher.DECRYPT_MODE, iv);
			CipherInputStream cipherStream = new CipherInputStream(inputStream, cipher);
			for (int i = cipherStream.read(); i != -1; i = cipherStream.read()) {
				outputStream.write(i);
			}
			inputStream.close();
			cipherStream.close();
			outputStream.close();

			return outputStream.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private Cipher getCipher(int mode, byte[] iv) {
		IvParameterSpec spec = new IvParameterSpec(iv);

		try {
			Cipher cipher = Cipher.getInstance(BLOWFISH_CBC_PKCS5PADDING);
			cipher.init(mode, getKey(), spec);

			return cipher;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * @param key
	 *            the key to set
	 */
	public void setKey(Key key) {
		this.key = key;
	}

	/**
	 * @return the key
	 */
	public Key getKey() {
		return key;
	}
	// public static void main(String[] args) throws InvalidKeyException,
	// NoSuchAlgorithmException, NoSuchPaddingException,
	// InvalidAlgorithmParameterException, IOException,
	// IllegalBlockSizeException, BadPaddingException {
	// BlowfishAlgorithmEncryptor blowfish = new BlowfishAlgorithmEncryptor();
	// blowfish.initialKey(true, null);
	// byte[] input = new byte[] { 1, 2, 3, 4, 5, 6 };
	// byte[] encryptInfo = blowfish.encrypt(input);
	// System.out.println("information.length: " + input.length);
	// System.out.println("encryptInfo.length: " + encryptInfo.length);
	// for (int i = 0; i < encryptInfo.length; i++) {
	// System.out.print((encryptInfo[i] & 0xff) + ", ");
	// }
	// System.out.println();
	// byte[] decryptInfo = blowfish.decrypt(encryptInfo);
	// for (int i = 0; i < decryptInfo.length; i++) {
	// System.out.print((decryptInfo[i] & 0xff) + ", ");
	// }
	//
	// }
}
