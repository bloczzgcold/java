package com.github.hualuomoli.framework.base.config;

import org.apache.log4j.PropertyConfigurator;

import com.github.hualuomoli.tool.PropertiesLoader;

/**
 * 初始化配置文件
 * @author lbq
 *
 */
public class Log4jInitializer {

	/**
	 * 初始化log4j	
	 */
	public static void init() {
		init("log4j.properties");
	}

	/**
	 * 初始化log4j
	 * @param log4jFilename log4j文件名
	 */
	public static void init(String log4jFilename) {
		String environment = System.getenv("environment");
		initLogger(environment, log4jFilename);
	}

	private static void initLogger(String environment, String filename) {
		PropertyConfigurator.configure(PropertiesLoader.load(filename, environment));
	}

}
