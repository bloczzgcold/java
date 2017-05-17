package com.github.hualuomoli.framework.base.config;

import java.util.Properties;

import org.apache.log4j.PropertyConfigurator;

import com.github.hualuomoli.tool.util.EnvUtils;
import com.github.hualuomoli.tool.util.EnvUtils.Env;
import com.github.hualuomoli.tool.util.PropertyUtils;

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
		Env env = EnvUtils.getEnv();
		// 指定环境变量的文件
		String envLog4jFilename = log4jFilename.substring(0, log4jFilename.lastIndexOf(".")) //
				+ "-" + env.name().toLowerCase() //
				+ log4jFilename.substring(log4jFilename.lastIndexOf("."));
		Properties prop = PropertyUtils.loadFirst(envLog4jFilename, log4jFilename);
		PropertyConfigurator.configure(prop);
	}

}
