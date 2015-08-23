package com.mina.utils.encryption;


/**
 * 简单加密
 * @author zsf
 * 2011-4-12 上午11:03:22
 */
public class SimpleEncryption implements IEncryptionable {

	@Override
	public Object encryption(Object str) {
		System.out.println("SimpleEncryption.encryption()");
		return str;
	}

}
