package com.github.hualuomoli.tool;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * 配置文件加载器
 * @author lbq
 *
 */
public final class PropertiesLoader {

	private static final Logger logger = LoggerFactory.getLogger(PropertiesLoader.class);

	/**
	 * 加载Properties配置文件,如果指定的运行环境文件存在,使用指定运行环境的配置,否则使用默认配置
	 * @param filename 文件
	 * @return 配置信息
	 */
	public static Properties loadProperties(String filename, String environment) {
		int index = filename.indexOf(".");
		String envFilename = filename.substring(0, index) + "-" + environment + filename.substring(index);
		Properties prop = null;
		prop = loadProperties(envFilename);
		if (prop == null) {
			prop = loadProperties(filename);
		}
		return prop;
	}

	/**
	 * 加载Properties配置文件
	 * @param filename 文件
	 * @return 配置信息
	 */
	public static Properties loadProperties(String filename) {
		return loadProperties(filename, new Properties());
	}

	/**
	 * 加载Properties配置文件
	 * @param filename 文件
	 * @param prop 配置信息
	 * @return 配置信息
	 */
	public static Properties loadProperties(String filename, Properties prop) {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

		if (filename.startsWith("classpath:")) {
			prop = loadProperties(prop, classLoader.getResource(filename.substring("classpath:".length())));
		} else if (filename.startsWith("classpath*:")) {
			try {
				Enumeration<URL> rs = classLoader.getResources(filename.substring("classpath*:".length()));
				List<URL> urlList = Lists.newArrayList();
				while (rs.hasMoreElements()) {
					urlList.add(rs.nextElement());
				}
				// sort
				Collections.sort(urlList, new Comparator<URL>() {
					private String prefix = "file:/";

					@Override
					public int compare(URL o1, URL o2) {
						String s1 = o1.getPath();
						String s2 = o2.getPath();
						if (s1.startsWith(prefix) && s2.startsWith(prefix)) {
							return this.compare(s1, s2);
						}
						if (!s1.startsWith(prefix) && !s2.startsWith(prefix)) {
							return this.compare(s1, s2);
						}
						return s1.startsWith(prefix) ? -1 : 1;
					}

					private int compare(String s1, String s2) {
						int len1 = s1.length();
						int len2 = s2.length();
						int len = len1 > len2 ? len2 : len1;
						for (int i = 0; i < len; i++) {
							int c = s1.charAt(i) - s2.charAt(i);
							if (c == 0) {
								continue;
							}
							return c;
						}
						return len1 - len2;
					}
				});

				for (URL url : urlList) {
					prop = loadProperties(prop, url);
				}
			} catch (IOException e) {
				logger.warn("", e);
			}
		} else {
			prop = loadProperties(prop, classLoader.getResource(filename));
		}

		return prop;
	}

	/**
	 * 加载Properties配置文件
	 * @param prop 配置信息
	 * @param url 配置文件资源
	 * @param 配置信息,如果无法加载返回null
	 */
	private static Properties loadProperties(Properties prop, URL url) {
		if (url == null || prop == null) {
			return null;
		}

		InputStream is = null;

		try {
			logger.info("load properties file {}", url.getPath());
			is = url.openStream();
			prop.load(is);
			return prop;
		} catch (Exception e) {
			logger.warn("can not load property {}", url.getPath(), e);
			return null;
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
				}
			}
		}
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

		Map<String, String> dataMap = Maps.newHashMap();

		Set<Entry<Object, Object>> entrySet = prop.entrySet();
		for (Entry<Object, Object> entry : entrySet) {
			String key = String.valueOf(entry.getKey());
			String value = String.valueOf(entry.getValue());
			dataMap.put(key, value);
		}

		return dataMap;
	}

}
