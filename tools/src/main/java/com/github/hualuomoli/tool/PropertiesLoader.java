package com.github.hualuomoli.tool;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

/**
 * 配置文件加载器
 */
public final class PropertiesLoader {

	private static final Logger logger = LoggerFactory.getLogger(PropertiesLoader.class);

	/**
	 * 加载第一个可加载的配置文件
	 * classpath: 当前项目的路径
	 * classpath*: 当前项目及jar包的路径
	 * @param filenames 资源文件
	 * @return 配置信息
	 */
	public static Properties loadFirst(String... filenames) {
		Validate.notEmpty(filenames, "filenames is emtpy.");

		for (String filename : filenames) {
			Properties prop = PropertiesLoader.load(filename);
			if (prop != null) {
				return prop;
			}
		}
		return null;
	}

	/**
	 * 加载配置文件,后面的配置文件覆盖前面的
	 * classpath: 当前项目的路径
	 * classpath*: 当前项目及jar包的路径
	 * @param filenames 资源文件
	 * @return 配置信息
	 */
	public static Properties loadCover(String... filenames) {
		Validate.notEmpty(filenames, "filenames is emtpy.");

		Properties p = null;

		for (String filename : filenames) {
			Properties prop = PropertiesLoader.load(filename);
			p = PropertiesLoader.copy(prop, p);
		}
		return p;
	}

	/**
	 * 加载配置文件
	 * classpath: 当前项目的路径
	 * classpath*: 当前项目及jar包的路径
	 * @param filename 文件名
	 * @return 配置信息
	 */
	public static Properties load(String filename) {
		Validate.notBlank(filename, "filename is blank.");

		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

		if (filename.startsWith("classpath:")) {
			return PropertiesLoader._loadClasspathFile(filename, classLoader);
		}
		if (filename.startsWith("classpath*:")) {
			return PropertiesLoader._loadClasspathsFile(filename, classLoader);
		}

		return PropertiesLoader._loadFile(filename, classLoader);
	}

	/**
	 * 复制
	 * @param src 原
	 * @param dest 目的
	 * @return 复制后的配置信息
	 */
	public static Properties copy(Properties src, Properties dest) {
		if (src == null) {
			return dest;
		}
		if (dest == null) {
			return src;
		}
		// set src configure to dest
		Set<Entry<Object, Object>> set = src.entrySet();
		for (Entry<Object, Object> entry : set) {
			dest.put(entry.getKey(), entry.getValue());
		}
		return dest;
	}

	/**
	 * 加载当前项目及jar包下的配置文件,如classpath*:log4j/log4j.properties
	 * @param filename 文件名
	 * @param classLoader 加载器
	 * @return 配置信息,如果文件不存在或加载错误返回null
	 */
	private static Properties _loadClasspathsFile(String classpathsFilename, ClassLoader classLoader) {
		String filename = classpathsFilename.substring("classpath*:".length());
		List<URL> urlList = Lists.newArrayList();

		// get url
		try {
			Enumeration<URL> rs = classLoader.getResources(filename);
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
		} catch (IOException e) {
			logger.warn(e.getMessage(), e);
		}

		Properties p = null;

		for (URL url : urlList) {
			Properties prop = PropertiesLoader.loadFromUrl(url);
			p = PropertiesLoader.copy(prop, p);
		}

		return p;

	}

	/**
	 * 加载classpath下的配置文件,如classpath:log4j/log4j.properties
	 * @param filename 文件名
	 * @param classLoader 加载器
	 * @return 配置信息,如果文件不存在或加载错误返回null
	 */
	private static Properties _loadClasspathFile(String classpathFilename, ClassLoader classLoader) {
		String filename = classpathFilename.substring("classpath:".length());
		URL url = classLoader.getResource(filename);
		return PropertiesLoader.loadFromUrl(url);
	}

	/**
	 * 加载文件类型的配置文件,如log4j/log4j.properties
	 * @param filename 文件名
	 * @param classLoader 加载器
	 * @return 配置信息,如果文件不存在或加载错误返回null
	 */
	private static Properties _loadFile(String filename, ClassLoader classLoader) {
		URL url = classLoader.getResource(filename);
		return PropertiesLoader.loadFromUrl(url);
	}

	/**
	 * 加载配置文件,如果无法加载返回null
	 * @param url 资源URL
	 * @return 资源配置信息,如果资源不存在或格式错误返回null
	 */
	public static Properties loadFromUrl(URL url) {
		Validate.notNull(url, "url is null.");
		logger.info("load properties file {}", url.getPath());

		InputStream is = null;

		try {
			Properties prop = new Properties();
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
		// end
	}

}
