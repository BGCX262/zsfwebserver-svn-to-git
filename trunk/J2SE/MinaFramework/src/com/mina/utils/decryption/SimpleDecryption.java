package com.mina.utils.decryption;


/**
 * 简单数据解密
 * @author zsf
 * 2011-4-12 上午11:03:57
 */
public class SimpleDecryption implements IDecryptionable {

	@Override
	public Object decryption(Object str) {
		System.out.println("SimpleDecryption.decryption()");
		return str;
	}

}
