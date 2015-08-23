package com.server.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.cindy.run.util.InternalCindyTimer;

public class FileListener implements Runnable {
	private static ConcurrentMap<String, FileListener> instancesMap = new ConcurrentHashMap<String, FileListener>();
	private static ConcurrentMap<String, Properties> propertiesMap = new ConcurrentHashMap<String, Properties>();
	private static Map<String, Long> lastModified = new HashMap<String, Long>();
	private static final Log log = LogFactory.getLog(FileListener.class);
	private static String defaultConfigName = "config/config.properties";
	private static FileListener defaultInstance = new FileListener();
	private static final long PATROL_INTERVAL = 30;
	private static ScheduledFuture<?> future;
	private String configName;
	static {
		defaultConfigName = System.getProperty("default-file", defaultConfigName);
		defaultInstance.setConfigName(defaultConfigName);
		instancesMap.put(defaultConfigName, defaultInstance);
		defaultInstance.run();
	}

	public synchronized static void startListening() {
		if (future == null) {
			InternalCindyTimer internalCindyTimer = InternalCindyTimer.getInstance();
			future = internalCindyTimer.schedule(defaultInstance, PATROL_INTERVAL, PATROL_INTERVAL, TimeUnit.SECONDS);
		}
	}
	
	private FileListener() {
	}

	public void run() {
		try {
			Properties property = reload(configName);
			if (property != null) {
				String temp = property.getProperty("listening.files.name");
				if (temp != null) {
					String[] listeningFiles = temp.split(",");
					for (String path : listeningFiles) {
						reload(path);
					}
				}
			}
		} catch (Exception e) {
			log.error(e, e);
		}
	}

	private Properties reload(String path) {
		try {
			File file = findFile(path);
			if (file != null) {
				if (isChanged(file)) {
					InputStream in = new FileInputStream(file);
					String fileName = file.getName().toLowerCase();
					if (in != null) {
						Properties property = new Properties();
						if (fileName.endsWith("xml")) {
							property.loadFromXML(in);
						} else {
							property.load(in);
						}
						propertiesMap.put(path, property);
						printProperties(property);
						in.close();
						return property;
					}
				}
			}
		} catch (Exception e) {
			log.error(e, e);
		}
		return null;
	}

	private boolean isChanged(File file) {
		long modified = file.lastModified();
		String absoluteFile = file.getAbsolutePath();
		if (lastModified.containsKey(absoluteFile)) {
			Long previous = lastModified.get(absoluteFile);
			if (previous != modified) {
				lastModified.put(absoluteFile, modified);
				return true;
			}
		} else {
			lastModified.put(absoluteFile, modified);
			return true;
		}
		return false;
	}

	private File findFile(String name) {
		ClassLoader classLoader = FileListener.class.getClassLoader();
		File file = null;
		if (classLoader != null) {
			URL resource = classLoader.getResource(name);
			if (resource != null) {
				String path = resource.getFile();
				file = new File(path);
			}
		}
		if (file == null) {
			file = new File(name);
		}
		if (file == null || !file.isFile()) {
			log.error("Listening file [" + name + "] is not found");
			return null;
		}
		return file;
	}

	private static void printProperties(Properties property) {
		StringBuffer buffer = new StringBuffer();
		for (Entry<Object, Object> entry : property.entrySet()) {
			buffer.append(entry.getKey() + " = " + entry.getValue() + ", ");
		}
		log.info(buffer.toString());
	}

	public static String getIgnoreOverwrite(String key, String defaultValue) {
		for (String propertyKey : propertiesMap.keySet()) {
			Properties propertie = propertiesMap.get(propertyKey);
			if (propertie.containsKey(propertyKey)) {
				return propertie.getProperty(key, defaultValue);
			}
		}
		return defaultValue;
	}

	public static Properties getProperties(String fileName) {
		return propertiesMap.get(fileName);
	}

	public static FileListener getDefaultInstance() {
		return defaultInstance;
	}

	public String getConfigName() {
		return configName;
	}

	public void setConfigName(String configName) {
		this.configName = configName;
	}

	public String getProperty(String key, String defaultValue) {
		Properties properties = getProperties(configName);
		if (properties != null) {
			return properties.getProperty(key, defaultValue);
		}
		return defaultValue;
	}

	public void setProperty(String key, String value) {
		Properties properties = getProperties(configName);
		if (properties != null) {
			properties.setProperty(key, value);
		}
	}

	public static FileListener getInstanceByName(String configName) {
		if (instancesMap.containsKey(configName)) {
			return instancesMap.get(configName);
		} else {
			FileListener instance = new FileListener();
			if (getProperties(configName) == null) {
				instance.reload(configName);
			}
			instance.setConfigName(configName);
			FileListener previous = instancesMap.putIfAbsent(configName, instance);
			return previous != null ? previous : instance;
		}
	}
}
