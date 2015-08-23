package com.ttt.util;

import java.math.BigInteger;
import java.security.Key;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;

public class CryptFactory {

	private BlowfishAlgorithmEncryptor blowfish;
	private boolean enableBlowfish = false;
	private boolean RSAFinished = false;
	private KeyPair key;

	/**
	 * Constructor function for this class,it will product a RSAKeyPair for
	 * every object
	 */
	public CryptFactory() {
		try {
			/* Create RSAKeyPair for client and send public key to client */
			key = RSAEncryptUtil.generateKey();
			blowfish = new BlowfishAlgorithmEncryptor();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Initial blowfish key
	 * 
	 * @param key
	 * @return key.getEncoded(),format by RAW;
	 */
	public byte[] initialBlowfishKey(byte[] key) {
		if (key == null) {
			key = blowfish.initialKey(true, null);
		} else {
			key = blowfish.initialKey(false, key);
		}
		return key;
	}

	/**
	 * Blowfish decrypt a package,if enableblowfish==true then decrypt
	 * information with out length, else return information with out length.
	 * 
	 * @param receiveArray
	 * @return
	 */
	public byte[] blowfishDecrypt(byte[] receiveArray) {
		if (enableBlowfish) {
			/* exception length bytes */
			return blowfish.decrypt(receiveArray);
		} else {
			return receiveArray;
		}
	}

	/**
	 * Blowfish decrypt
	 * 
	 * @param receiveArray
	 * @return
	 */
	public byte[] blowfishEncrypt(byte[] receiveArray) {
		if (enableBlowfish) {
			return blowfish.encrypt(receiveArray);
		} else {
			return receiveArray;
		}
	}

	/**
	 * 
	 * @return pubKey.getModulus()(65 bytes) +
	 *         pubKey.getPublicExponent().intValue() (4 bytes), total 69 bytes
	 */
	public byte[] getRSAPublickey() {
		RSAPublicKey pubKey = (RSAPublicKey) key.getPublic();
		BigInteger Modulus = pubKey.getModulus();
		BigInteger exponent = pubKey.getPublicExponent();
		byte[] bytesModulus = Modulus.toByteArray();
		byte[] bytesExponent = DataFactory.getbyte(exponent.intValue());
		int n = bytesModulus.length + bytesExponent.length;
		byte[] bytePublicKey = new byte[n];
		for (int i = 0; i < n; i++) {
			if (i < bytesModulus.length) {
				bytePublicKey[i] = bytesModulus[i];
			} else {
				bytePublicKey[i] = bytesExponent[i - bytesModulus.length];
			}
		}
		return bytePublicKey;

	}

	/**
	 * Decrypt information uses to RSA algorithm when first time,and then using
	 * blowfish algorithm
	 * 
	 * @param disposeInfo
	 * @return
	 * @throws Exception
	 */
	public byte[] decryptInformation(byte[] disposeInfo) throws Exception {
		if (!RSAFinished) {
			return RSADecrypt(disposeInfo, true);
		} else {
			return blowfishDecrypt(disposeInfo);
		}
	}

	/**
	 * Decrypt information uses to RSA algorithm when first time,and then using
	 * blowfish algorithm. the blowfish key is encrypted by RSA algorithm and
	 * sending to clients,the default value equal false for RSAFinished.
	 * 
	 * @param disposeInfo
	 * @return
	 * @throws Exception
	 */
	public byte[] decryptInformation(boolean isRSAFinished, byte[] disposeInfo) throws Exception {
		RSAFinished = isRSAFinished;
		if (!RSAFinished) {
			return RSADecrypt(disposeInfo, true);
		} else {
			return blowfishDecrypt(disposeInfo);
		}
	}

	/**
	 * Decrypt information thought by RSA algorithm,and using result initial
	 * blowfish key if isBlowfishKey is true
	 * 
	 * @param receiveArray
	 * @param isBlowfishKey
	 * @return
	 * @throws Exception
	 */
	public byte[] RSADecrypt(byte[] receiveArray, boolean isBlowfishKey) throws Exception {
		byte[] decryptInfo = RSAEncryptUtil.decrypt(receiveArray, key.getPrivate());
		if (isBlowfishKey) {
			initialBlowfishKey(decryptInfo);
		}
		return decryptInfo;
	}

	/**
	 * 
	 * @param receiveArray
	 * @return
	 * @throws Exception
	 */
	public byte[] RSAEncrypt(byte[] receiveArray) throws Exception {
		byte[] encryptInfo = RSAEncryptUtil.encrypt(receiveArray, key.getPublic());
		return encryptInfo;
	}

	public boolean isEnableBlowfish() {
		return enableBlowfish;
	}

	public void changeModel() {
		enableBlowfish = !enableBlowfish;
		RSAFinished = !RSAFinished;
	}

	public void changeTrueModel() {
		enableBlowfish = true;
		RSAFinished = true;
	}

	public void changeFalseModel() {
		enableBlowfish = false;
		RSAFinished = false;
	}

	public byte[] getBlowfishKey() {
		Key blowfishKey = blowfish.getKey();
		if (blowfishKey != null) {
			return blowfishKey.getEncoded();
		} else {
			return null;
		}

	}
}
