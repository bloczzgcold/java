package com.github.hualuomoli.tool.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ObjectUtils {

	private static final Logger logger = LoggerFactory.getLogger(ObjectUtils.class);

	/**
	 * 获取类的所有属性及所有父属性
	 * @param clazz 类
	 * @param names 已经存在的属性
	 * @return 类的属性及所有父属性
	 */
	public static List<Field> getFields(Class<?> clazz) {
		List<Field> fields = getFields(clazz, new HashSet<String>());

		if (logger.isDebugEnabled()) {
			for (Field field : fields) {
				logger.debug("field name {}", field.getName());
			}
		}

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
		fieldList.addAll(getFields(clazz.getSuperclass(), names));
		return fieldList;
	}

}
