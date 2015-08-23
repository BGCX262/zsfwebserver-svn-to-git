package com.mina.utils;

/**
 * 字符串数据包工具
 * @author zsf
 * 2011-4-11 下午02:23:49
 */
public class StringPackageUtil {
	
	/**
	 * 给字符串添加前缀（长度）
	 * @param str
	 * @return
	 */
	public static String addLength(String str) {
		return str.length() + "," + str;
	}
	
	public static String removeLength(String str) {
		return str.substring(str.indexOf(",") + 1);
	}
	
	public static String[] getArgs(String args, String spe, int paramCount) {
		String[] arr = args.split(spe);
		
		if (arr.length != paramCount) {
			arr = new String[paramCount];
			int len = spe.length();
			for (int i = 0; i < paramCount; i++) {
				int index = args.indexOf(spe);
				arr[i] = args.substring(0, Math.max(0, index));
				args = args.substring(Math.min(index + len, args.length()));
			}
		}
		
		return arr;
	}

}
