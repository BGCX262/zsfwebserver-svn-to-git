package com.ttt.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.EncodedKeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPrivateCrtKeySpec;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * 
 * <p>
 * Title: RSAEncryptUtil
 * </p>
 * <p>
 * Description: Utility class that helps encrypt and decrypt strings using RSA
 * algorithm
 * </p>
 * 
 * @author Aviran Mordo http://aviran.mordos.com
 * @version 1.0
 */
public class RSAEncryptUtil {

	protected static final String ALGORITHM = "RSA";
	private static int KeyPairSize = 512;
	private static final int blockSize = KeyPairSize / 8;

	/**
	 * 
	 * @param KeyPairSize
	 *            set key pair initial sized
	 */
	private RSAEncryptUtil(int KeyPairSize) {
		RSAEncryptUtil.KeyPairSize = KeyPairSize;
	}

	/**
     * 
     */
	private RSAEncryptUtil() {
	}

	/**
	 * Generate key which contains a pair of private and public key using 1024
	 * bytes
	 * 
	 * @return key pair
	 * @throws NoSuchAlgorithmException
	 */
	public static KeyPair generateKey() throws NoSuchAlgorithmException {
		KeyPairGenerator keyGen = KeyPairGenerator.getInstance(ALGORITHM);
		keyGen.initialize(KeyPairSize);
		KeyPair key = keyGen.generateKeyPair();
		return key;
	}

	/**
	 * Encrypt a text using public key.
	 * 
	 * @param text
	 *            The original unencrypted text
	 * @param key
	 *            The public key
	 * @return Encrypted text
	 * @throws java.lang.Exception
	 */
	public static byte[] encrypt(byte[] text, PublicKey key) throws Exception {
		byte[] cipherText = null;
		/* 11 is fill content. */
		int encryptBlockSize = blockSize - 11;
		try {
			// get an RSA cipher object and print the provider
			Cipher cipher = Cipher.getInstance("RSA");
			// encrypt the plaintext using the public key
			cipher.init(Cipher.ENCRYPT_MODE, key);
			cipherText = crypt(text, cipher, encryptBlockSize);
		} catch (Exception e) {
			throw e;
		}
		return cipherText;
	}

	/**
	 * Encrypt a text using public key. The result is enctypted BASE64 encoded
	 * text
	 * 
	 * @param text
	 *            The original unencrypted text
	 * @param key
	 *            The public key
	 * @return Encrypted text encoded as BASE64
	 * @throws java.lang.Exception
	 */
	public static String encrypt(String text, PublicKey key) throws Exception {
		String encryptedText;
		try {
			byte[] cipherText = encrypt(text.getBytes("UTF8"), key);
			encryptedText = encodeBASE64(cipherText);
		} catch (Exception e) {
			throw e;
		}
		return encryptedText;
	}

	/**
	 * Decrypt text using private key
	 * 
	 * @param text
	 *            The encrypted text
	 * @param key
	 *            The private key
	 * @return The unencrypted text
	 * @throws java.lang.Exception
	 */
	public static byte[] decrypt(byte[] text, PrivateKey key) throws Exception {
		byte[] dectyptedText = null;
		if (text.length % blockSize != 0) {
			throw new javax.crypto.IllegalBlockSizeException(" Data must not be longer than 64 bytes.");
		}
		try {
			// decrypt the text using the private key
			Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.DECRYPT_MODE, key);
			dectyptedText = crypt(text, cipher, blockSize);
		} catch (Exception e) {
			throw e;
		}
		return dectyptedText;

	}

	/**
	 * Decrypt or encrypt text using cipher, decrypt information length must be
	 * (length % 64 == 0); encrypt information length must be ((length + 11) %
	 * 64 == 0)
	 * 
	 * @param text
	 * @param cipher
	 * @param cryptBlockSize
	 * @return
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 */
	private static byte[] crypt(byte[] text, Cipher cipher, int cryptBlockSize) throws IllegalBlockSizeException, BadPaddingException {
		int cryptCount = text.length / cryptBlockSize;
		int length = blockSize * (cryptCount + 1);
		ByteBuffer buffer = ByteBuffer.allocate(length);
		for (int i = 0; i < cryptCount; i++) {
			buffer.put(cipher.doFinal(text, i * cryptBlockSize, cryptBlockSize));
		}
		if (text.length % cryptBlockSize != 0) {
			int offset = cryptCount * cryptBlockSize;
			byte[] doFinal = cipher.doFinal(text, offset, text.length - offset);
			buffer.put(doFinal);
		}
		buffer.flip();
		byte[] retByte = new byte[buffer.limit()];
		for (int i = 0; i < buffer.limit(); i++) {
			retByte[i] = buffer.array()[i];
		}
		return retByte;
	}

	/**
	 * Decrypt BASE64 encoded text using private key
	 * 
	 * @param text
	 *            The encrypted text, encoded as BASE64
	 * @param key
	 *            The private key
	 * @return The unencrypted text encoded as UTF8
	 * @throws java.lang.Exception
	 */
	public static String decrypt(String text, PrivateKey key) throws Exception {
		String result;
		try {
			// decrypt the text using the private key
			byte[] dectyptedText = decrypt(decodeBASE64(text), key);
			result = new String(dectyptedText, "UTF8");
		} catch (Exception e) {
			throw e;
		}
		return result;

	}

	/**
	 * Convert a Key to string encoded as BASE64
	 * 
	 * @param key
	 *            The key (private or public)
	 * @return A string representation of the key
	 */
	public static String getKeyAsString(Key key) {
		// Get the bytes of the key
		byte[] keyBytes = key.getEncoded();
		// Convert key to BASE64 encoded string
		BASE64Encoder b64 = new BASE64Encoder();
		return b64.encode(keyBytes);
	}

	/**
	 * Generates Private Key from BASE64 encoded string
	 * 
	 * @param key
	 *            BASE64 encoded string which represents the key
	 * @return The PrivateKey
	 * @throws java.lang.Exception
	 */
	public static PrivateKey getPrivateKeyFromString(String key) throws Exception {
		KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
		BASE64Decoder b64 = new BASE64Decoder();
		EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(b64.decodeBuffer(key));
		PrivateKey privateKey = keyFactory.generatePrivate(privateKeySpec);
		return privateKey;
	}

	/**
	 * Generates Public Key from BASE64 encoded string
	 * 
	 * @param key
	 *            BASE64 encoded string which represents the key
	 * @return The PublicKey
	 * @throws java.lang.Exception
	 */
	public static PublicKey getPublicKeyFromString(String key) throws Exception {
		BASE64Decoder b64 = new BASE64Decoder();
		KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
		EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(b64.decodeBuffer(key));
		PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);
		return publicKey;
	}

	/**
	 * Encode bytes array to BASE64 string
	 * 
	 * @param bytes
	 * @return Encoded string
	 */
	private static String encodeBASE64(byte[] bytes) {
		BASE64Encoder b64 = new BASE64Encoder();
		return b64.encode(bytes);
	}

	/**
	 * Decode BASE64 encoded string to bytes array
	 * 
	 * @param text
	 *            The string
	 * @return Bytes array
	 * @throws IOException
	 */
	private static byte[] decodeBASE64(String text) throws IOException {
		BASE64Decoder b64 = new BASE64Decoder();
		return b64.decodeBuffer(text);
	}

	/**
	 * Encrypt file using 1024 RSA encryption
	 * 
	 * @param srcFileName
	 *            Source file name
	 * @param destFileName
	 *            Destination file name
	 * @param key
	 *            The key. For encryption this is the Private Key and for
	 *            decryption this is the public key
	 * @param cipherMode
	 *            Cipher Mode
	 * @throws Exception
	 */
	public static void encryptFile(String srcFileName, String destFileName, PublicKey key) throws Exception {
		encryptDecryptFile(srcFileName, destFileName, key, Cipher.ENCRYPT_MODE);
	}

	/**
	 * Decrypt file using 1024 RSA encryption
	 * 
	 * @param srcFileName
	 *            Source file name
	 * @param destFileName
	 *            Destination file name
	 * @param key
	 *            The key. For encryption this is the Private Key and for
	 *            decryption this is the public key
	 * @param cipherMode
	 *            Cipher Mode
	 * @throws Exception
	 */
	public static void decryptFile(String srcFileName, String destFileName, PrivateKey key) throws Exception {
		encryptDecryptFile(srcFileName, destFileName, key, Cipher.DECRYPT_MODE);
	}

	/**
	 * Encrypt and Decrypt files using 1024 RSA encryption
	 * 
	 * @param srcFileName
	 *            Source file name
	 * @param destFileName
	 *            Destination file name
	 * @param key
	 *            The key. For encryption this is the Private Key and for
	 *            decryption this is the public key
	 * @param cipherMode
	 *            Cipher Mode
	 * @throws Exception
	 */
	public static void encryptDecryptFile(String srcFileName, String destFileName, Key key, int cipherMode) throws Exception {
		OutputStream outputWriter = null;
		InputStream inputReader = null;
		try {
			Cipher cipher = Cipher.getInstance("RSA");
			// String textLine = null;
			// RSA encryption data size limitations are slightly less than the
			// key modulus size,
			// depending on the actual padding scheme used (e.g. with 1024 bit
			// (128 byte) RSA key,
			// the size limit is 117 bytes for PKCS#1 v 1.5 padding.
			// (http://www.jensign.com/JavaScience/dotnet/RSAEncrypt/)
			byte[] buf = cipherMode == Cipher.ENCRYPT_MODE ? new byte[100] : new byte[128];
			int bufl;
			// init the Cipher object for Encryption...
			cipher.init(cipherMode, key);

			// start FileIO
			outputWriter = new FileOutputStream(destFileName);
			inputReader = new FileInputStream(srcFileName);
			while ((bufl = inputReader.read(buf)) != -1) {
				byte[] encText = null;
				if (cipherMode == Cipher.ENCRYPT_MODE) {
					encText = encrypt(copyBytes(buf, bufl), (PublicKey) key);
				} else {
					encText = decrypt(copyBytes(buf, bufl), (PrivateKey) key);
				}
				outputWriter.write(encText);
			}
			outputWriter.flush();

		} catch (Exception e) {
			throw e;
		} finally {
			try {
				if (outputWriter != null) {
					outputWriter.close();
				}
				if (inputReader != null) {
					inputReader.close();
				}
			} catch (Exception e) {
				// do nothing...
			} // end of inner try, catch (Exception)...
		}
	}

	private static byte[] copyBytes(byte[] arr, int length) {
		byte[] newArr = null;
		if (arr.length == length) {
			newArr = arr;
		} else {
			newArr = new byte[length];
			for (int i = 0; i < length; i++) {
				newArr[i] = (byte) arr[i];
			}
		}
		return newArr;
	}

	/**
	 * static String[] keyss = new String[]{"6917843815699762775508362712708527856124150659065781133033958724570715081995696290391598178746298117466269302569917767092075252851826749483190409073102503"
	 * ,"65537","1736084612307765054378229725741446170097409187018858084669873798663572500321657687033502500776562851884993157725212488121993662871393241985986190606798201"
	 * ,
	 * "85610980711689699668490870110512374452254983419188760165546077611852488049077"
	 * ,
	 * "80805566741453879511026001108229130392803769682471120146708358726427090450539"
	 * ,
	 * "67433816718782453824356252145274727161363912813607003330421282916200758931129"
	 * ,
	 * "38786967950206084222009795121269999906874299810354097648277332633717494108565"
	 * ,
	 * "71992502418753989837103313077149813949514599551063532037774448943823656153677"
	 * };
	 * 
	 * @param ss
	 * @return
	 */
	public static KeyPair genKeysFromString(String[] ss) {
		try {
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			RSAPublicKeySpec pubKeySpec = new RSAPublicKeySpec(new BigInteger(ss[0]), new BigInteger(ss[1]));
			RSAPrivateKeySpec prikeySpec = new RSAPrivateCrtKeySpec(new BigInteger(ss[0]), new BigInteger(ss[1]), new BigInteger(ss[2]), new BigInteger(ss[3]), new BigInteger(ss[4]), new BigInteger(
					ss[5]), new BigInteger(ss[6]), new BigInteger(ss[7]));
			PublicKey pubKey = keyFactory.generatePublic(pubKeySpec);
			PrivateKey priKey = keyFactory.generatePrivate(prikeySpec);

			return new KeyPair(pubKey, priKey);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}

}
