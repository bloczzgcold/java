package com.github.hualuomoli.gateway.client.util;

public class Validate {

	/**
	 * 是否为空
	 * @param value 值
	 * @return 提示信息
	 */
	public static void notNull(Object value, String message) {
		if (value == null) {
			throw new NullPointerException(message);
		}
	}

	/**
	 * 是否为空
	 * @param value 值
	 * @return 提示信息
	 */
	public static void notBlank(String value, String message) {
		if (value == null) {
			throw new NullPointerException(message);
		}
		if (value.trim().length() == 0) {
			throw new IllegalArgumentException(message);
		}
	}

	/**
	 * 是否为真
	 * @param exp 判断表达式
	 * @param message 提示信息
	 */
	public static void isTrue(boolean exp, String message) {
		if (!exp) {
			throw new IllegalArgumentException(message);
		}
	}

}
