package com.mina.utils.encryption;


/**
 * 加密接口
 * @author zsf
 * 2011-4-12 上午10:59:59
 */
public interface IEncryptionable {
	
	/**
	 * 数据加密
	 * @param str
	 * @return
	 */
	public Object encryption(Object str);

}
