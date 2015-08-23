

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Properties;

/**
 * ���ڶ�ȡ�����ļ��������ļ�·��Ϊ��jar��·��\config.properties��
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
			System.err.println("�ļ�δ�ҵ���������Ϣ��" + e.getMessage());
		}
	}
	
	/**
	 * ���ݼ����ֵ
	 * @param key
	 * @return
	 */
	public static String getString(String key) {
		return properties.getProperty(key);
	}
	
	/**
	 * ���ݼ���ö���
	 * @param key
	 * @return
	 */
	public static Object get(String key) {
		return properties.getProperty(key);
	}

}
