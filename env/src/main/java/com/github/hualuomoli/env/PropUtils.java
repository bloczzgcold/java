package com.github.hualuomoli.env;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.hualuomoli.env.EnvUtils.Env;

public class PropUtils {

	private static final Logger logger = LoggerFactory.getLogger(PropUtils.class);

	/**
	 * 转换
	 * @param prop 包含运行环境的配置信息
	 * @return 使用当前指定的环境覆盖默认环境信息
	 */
	public static final Properties parse(Properties prop) {

		// null or empty
		if (prop == null || prop.size() == 0) {
			return prop;
		}

		// 运行环境
		Env env = EnvUtils.getEnv();

		Set<String> names = prop.stringPropertyNames();

		// 制定环境的配置优先级最高
		String prefix = env.name() + ".";
		for (String name : names) {

			// 非当前运行环境的配置,忽略
			if (!name.startsWith(prefix)) {
				continue;
			}

			// 实际需要配置的参数名
			String configName = name.substring(prefix.length());

			// 值
			String value = prop.getProperty(name);

			// 使用制定环境的配置覆盖默认配置
			logger.debug("覆盖默认配置configName={}", configName);
			prop.put(configName, value);
		}

		// 移除运行环境的配置
		names = prop.stringPropertyNames();
		Env[] values = Env.values();
		for (String name : names) {

			for (Env value : values) {
				String pre = value.name() + ".";
				if (name.startsWith(pre)) {
					prop.remove(name);
				}
			}
		}

		if (logger.isInfoEnabled()) {

			names = prop.stringPropertyNames();
			List<String> list = new ArrayList<String>(names);
			Collections.sort(list);
			for (String name : list) {
				logger.info("{}={}", name, prop.getProperty(name));
			}
		}

		return prop;

	}

}
