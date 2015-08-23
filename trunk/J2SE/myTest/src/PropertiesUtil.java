

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Properties;

/**
 * 用于读取配置文件，配置文件路径为【jar包路径\config.properties】
 * @author D-io
 *
 */
public class PropertiesUtil {
	
	public static Properties properties = null;
	
	public static String path = "config.properties";
	
	static {
		properties = new Properties();
		try {
			String str = System.getProperty("user.dir");
			str = str.substring(0, str.lastIndexOf("\\") + 1);
			str += path;
			properties.load(new BufferedReader(new FileReader(str)));
		} catch (Exception e) {
			System.err.println("文件未找到，错误信息：" + e.getMessage());
		}
	}
	
	/**
	 * 根据键获得值
	 * @param key
	 * @return
	 */
	public static String getString(String key) {
		return properties.getProperty(key);
	}
	
	/**
	 * 根据键获得对象
	 * @param key
	 * @return
	 */
	public static Object get(String key) {
		return properties.getProperty(key);
	}

}
