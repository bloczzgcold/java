package com.github.hualuomoli.env;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 运行环境工具
 * @author lbq
 *
 */
public class EnvUtils {

	private static final Lock LOCK = new ReentrantLock();
	private static String[] keys = new String[] { "environment", "env" };
	private static Env env;

	/**
	 * 初始化环境变量的名称,默认为environment,env
	 * @param keys 名称
	 */
	public static final void init(String... keys) {
		EnvUtils.keys = keys;
	}

	/**
	 * 获取环境变量
	 * @return
	 */
	public static final Env getEnv() {

		if (env != null) {
			return env;
		}

		try {
			// lock
			LOCK.lock();

			if (env != null) {
				return env;
			}

			String runtime = getRuntime(keys);
			if (runtime != null && runtime.length() > 0) {
				System.err.println("runtime=" + runtime);
				env = Env.valueOf(Env.class, runtime);
			} else {
				env = Env.product;
			}
		} finally {
			System.err.println("运行环境env=" + env);
			// unlock
			LOCK.unlock();
		}

		return env;

	}

	private static final String getRuntime(String... keys) {
		if (keys == null || keys.length == 0) {
			return null;
		}

		String value = null;

		for (String key : keys) {
			value = getRuntime(key);
			if (value != null && value.trim().length() > 0) {
				break;
			}
		}

		return value;
	}

	private static final String getRuntime(String key) {
		String value = null;

		// read from -D parameter. 
		// such as java -Denv=test Main
		if (value == null || value.trim().length() == 0) {
			value = System.getProperty(key);
		}

		// read from environment configure.
		// such as export env=test & java Main
		if (value == null || value.trim().length() == 0) {
			System.getenv(key);
		}

		return value;
	}

	public static enum Env {
		/** 开发环境 */
		development,
		/** 测试环境 */
		test,
		/** 准生产环境 */
		prepare,
		/** 生产环境 */
		product;
	}

}
