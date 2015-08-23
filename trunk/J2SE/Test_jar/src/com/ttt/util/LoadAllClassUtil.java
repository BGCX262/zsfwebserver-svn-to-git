package com.ttt.util;

import java.io.File;
import java.net.URL;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * 将指定文件夹所有类加载到invoke中
 * @author zsf 2011-12-27 下午05:59:48
 */
public class LoadAllClassUtil {

	/**
	 * 将指定文件夹所有类加载到invoke中
	 * @param map
	 *            如：callbackInvocationMap
	 * @param path
	 *            如："/com/ttt/handlers/callback"
	 */
	public static void load(ByteArrayMap map, String path) {
		try {
			URL url = LoadAllClassUtil.class.getResource(path);
			File filePath = new File(url.getPath());
			if (url.getPath().toLowerCase().indexOf(".jar") == -1) {
				File[] files = filePath.listFiles();
				for (File file : files) {
					String p = file.getAbsolutePath();
					map.put(p.substring(p.indexOf("com"), p.lastIndexOf(".class")).replaceAll("\\\\", "."));
				}
			} else {
				path = path.substring(1);
				String jarPath = url.getPath().substring(0, url.getFile().toLowerCase().lastIndexOf(".jar") + 4).replace("file:/", "");
				JarFile jar = new JarFile(jarPath); 
				Enumeration<JarEntry> enumeration = jar.entries(); 
				while(enumeration.hasMoreElements()){ 
					JarEntry next = enumeration.nextElement();
					if (next.getName().startsWith(path) && next.getName().endsWith(".class")) {
						String p = next.getName();
						map.put(p.substring(p.indexOf("com"), p.lastIndexOf(".class")).replaceAll("/", "."));
					}
				} 
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
