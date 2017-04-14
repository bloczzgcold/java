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

import org.apache.commons.lang3.Validate;
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
	 * 加载指定运行环境的配置文件
	 * @param filenames 资源文件
	 * @param environment 运行环境
	 * @return 配置文件信息
	 */
	public static Properties loadEnvironmentProperties(String filename, String environment) {
		int index = filename.lastIndexOf(".properties");
		Validate.isTrue(index > 0);

		String envFilename = filename.substring(0, index) + "-" + environment + filename.substring(index);

		return PropertiesLoader.loadProperties(filename, envFilename);
	}

	/**
	 * 加载配置文件
	 * @param filenames 资源文件
	 * @return 配置文件信息
	 */
	public static Properties loadProperties(String filename) {
		return PropertiesLoader.load(filename, null);
	}

	/**
	 * 加载配置文件
	 * @param filenames 资源文件
	 * @return 配置文件信息
	 */
	public static Properties loadProperties(String... filenames) {
		Validate.notEmpty(filenames, "filenames is emtpy.");

		Properties prop = new Properties();

		for (String filename : filenames) {
			PropertiesLoader.load(filename, prop);
		}
		return prop;
	}

	/**
	 * 加载配置文件
	 * @param filename 文件名
	 * @param prop 加载器
	 * @return 加载后的资源
	 */
	public static Properties load(String filename, Properties prop) {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

		if (prop == null) {
			prop = new Properties();
		}

		if (filename.startsWith("classpath:")) {
			PropertiesLoader.loadResource(prop, classLoader.getResource(filename.substring("classpath:".length())));
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
						return s1.compareTo(s2);
					}
				});

				for (URL url : urlList) {
					PropertiesLoader.loadResource(prop, url);
				}
			} catch (IOException e) {
				logger.warn("", e);
			}
		} else {
			PropertiesLoader.loadResource(prop, classLoader.getResource(filename));
		}

		return prop;
	}

	/**
	 * 加载配置文件
	 * @param prop 加载器
	 * @param url 资源URL
	 */
	private static void loadResource(Properties prop, URL url) {
		Validate.notNull(prop, "prop is null.");
		Validate.notNull(url, "url is null.");

		InputStream is = null;

		try {
			logger.info("load properties file {}", url.getPath());
			is = url.openStream();
			prop.load(is);
		} catch (Exception e) {
			logger.warn("can not load property {}", url.getPath(), e);
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
				}
			}
		}
		// end
	}

	/**
	 * 转换properties为map
	 * @param prop Properties
	 * @return map
	 */
	public static Map<String, String> parse2Map(Properties prop) {
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
