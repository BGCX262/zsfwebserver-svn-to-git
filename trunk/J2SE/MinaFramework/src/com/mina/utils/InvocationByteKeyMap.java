package com.mina.utils;

import java.util.HashMap;


/**
 * byte映射map
 * @author zsf
 * 2011-4-18 下午04:40:54
 */
public class InvocationByteKeyMap {
	
	private HashMap<Integer, String> invocation;
	
	public InvocationByteKeyMap() {
		invocation = new HashMap<Integer, String>();
	}
	
	public String put(byte[] key, String value) {
		if (key.length > 4) {
			LogUtil.error("key length must be less than 4", new ArrayIndexOutOfBoundsException(key.length));
			return null;
		}
		return invocation.put(DataFactory.getInt(key), value);
		
	}
	
	public String get(byte[] key) {
		return invocation.get(DataFactory.getInt(key));
	}
	
}
