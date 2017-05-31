package com.github.hualuomoli.gateway.server.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Utils {

	/**
	 * 获取类的所有属性及所有父属性
	 * @param clazz 类
	 * @return 类的属性及所有父属性
	 */
	public static List<Field> getFields(Class<?> clazz) {
		List<Field> fields = Utils.getFields(clazz, new HashSet<String>());

		return fields;
	}

	/**
	 * 获取类的所有属性及所有父属性
	 * @param clazz 类
	 * @param names 已经存在的属性
	 * @return 类的属性及所有父属性
	 */
	private static List<Field> getFields(Class<?> clazz, Set<String> names) {
		List<Field> fieldList = new ArrayList<Field>();
		if (clazz == null) {
			return fieldList;
		}
		if (names == null) {
			names = new HashSet<String>();
		}
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) {
			if (names.contains(field.getName())) {
				continue;
			}
			if (field.getName().startsWith("this$")) {
				continue;
			}
			fieldList.add(field);
			names.add(field.getName());
		}
		fieldList.addAll(Utils.getFields(clazz.getSuperclass(), names));
		return fieldList;
	}

	/**
	 * 是否为空
	 * @param value 值
	 * @param message 提示信息
	 */
	public static void notNull(Object value, String message) {
		if (value == null) {
			throw new NullPointerException(message);
		}
	}

	/**
	 * 是否为空
	 * @param value 值
	 * @param message 提示信息
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
	 * @param exp 验证表达式
	 * @param message 提示信息
	 */
	public static void isTrue(boolean exp, String message) {
		if (!exp) {
			throw new IllegalArgumentException(message);
		}
	}

}
