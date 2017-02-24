package com.github.hualuomoli.tool;

import java.io.InputStream;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;

/**
 * 配置文件加载器
 * @author lbq
 *
 */
public final class PropertiesLoader {

	private static final Logger logger = LoggerFactory.getLogger(PropertiesLoader.class);

	/**
	 * 加载Properties配置文件
	 * @param filename 文件
	 * @return 配置信息
	 */
	public static Properties load(String filename) {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		InputStream is = classLoader.getResourceAsStream(filename);

		// 文件未找到
		if (is == null) {
			//			if (logger.isDebugEnabled()) {
			//				logger.debug("can not load file {}", filename);
			//			}
			return null;
		}

		if (logger.isInfoEnabled()) {
			logger.info("load properties file {}", classLoader.getResource(filename).getPath());
		}

		Properties prop = new Properties();
		try {
			prop.load(is);
			return prop;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 加载Properties配置文件
	 * @param filename 文件
	 * @return 配置信息
	 */
	public static Map<String, String> loadMap(String filename) {
		return parse(load(filename));
	}

	/**
	 * 加载Properties配置文件,如果指定的运行环境文件存在,使用指定运行环境的配置,否则使用默认配置
	 * @param filename 文件
	 * @return 配置信息
	 */
	public static Properties load(String filename, String environment) {
		int index = filename.indexOf(".");
		String envFilename = filename.substring(0, index) + "-" + environment + filename.substring(index);
		Properties prop = null;
		prop = load(envFilename);
		if (prop == null) {
			prop = load(filename);
		}
		return prop;
	}

	/**
	 * 加载Properties配置文件,如果指定的运行环境文件存在,使用指定运行环境的配置,否则使用默认配置
	 * @param filename 文件
	 * @return 配置信息
	 */
	public static Map<String, String> loadMap(String filename, String environment) {
		return parse(load(filename, environment));
	}

	/**
	 * 转换prop为Map
	 * @param prop Properties
	 * @return map
	 */
	public static Map<String, String> parse(Properties prop) {
		if (prop == null) {
			return Maps.newHashMap();
		}

		Set<Object> keys = prop.keySet();
		if (keys == null || keys.size() == 0) {
			return Maps.newHashMap();
		}

		Map<String, String> dataMap = Maps.newHashMap();
		for (Object key : keys) {
			String k = String.valueOf(key);
			dataMap.put(k, prop.getProperty(k));
		}

		return dataMap;
	}

}
