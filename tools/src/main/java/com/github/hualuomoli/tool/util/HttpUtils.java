package com.github.hualuomoli.tool.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.Validate;

import com.google.common.collect.Lists;

/**
 * HTTP工具
 * @author lbq
 *
 */
public class HttpUtils {

	private static final String datePattern = "yyyy-MM-dd HH:mm:ss S";

	/**
	 * 获取urlencoded方式的请求参数
	 * @param value 实体类
	 * @return 編碼后后參數
	 */
	public static List<Param> getUrlencodedParams(Object value) {
		return getUrlencodedParams(value, datePattern);
	}

	/**
	 * 获取urlencoded方式的请求参数
	 * @param name 请求名
	 * @param value 请求值
	 * @return 編碼后后參數
	 */
	public static List<Param> getUrlencodedParams(String name, Object value) {
		return getUrlencodedParams(name, value, datePattern);
	}

	/**
	 * 获取urlencoded方式的请求参数
	 * @param value 实体类
	 * @param datePattern 日期格式化方式
	 * @return 編碼后后參數
	 */
	public static List<Param> getUrlencodedParams(Object value, String datePattern) {
		Validate.notNull(value, "value is null.");
		Validate.notBlank(datePattern, "datePattern is blank.");

		List<Param> params = Lists.newArrayList();

		Class<?> clazz = value.getClass();
		List<Field> fields = getFields(clazz, new HashSet<String>());
		for (Field field : fields) {
			Object v = getFieldValue(field, value, clazz);
			params.addAll(getUrlencodedParams(field.getName(), v, datePattern));
		}

		Collections.sort(params, new Comparator<Param>() {

			@Override
			public int compare(Param o1, Param o2) {
				return StringUtils.compare(o1.name, o2.name);
			}
		});

		return params;
	}

	/**
	 * 获取urlencoded方式的请求参数
	 * @param name 请求名
	 * @param value 请求值
	 * @param datePattern 日期格式化方式
	 * @return 編碼后后參數
	 */
	@SuppressWarnings("unchecked")
	public static List<Param> getUrlencodedParams(String name, Object value, String datePattern) {
		Validate.notNull(name, "name is null.");
		Validate.notNull(datePattern, "datePattern is null.");

		List<Param> params = Lists.newArrayList();
		if (value == null) {
			return params;
		}

		Class<?> clazz = value.getClass();

		// 字符串
		if (String.class.isAssignableFrom(clazz)) {
			return Lists.newArrayList(new Param(name, (String) value));
		}

		// 数值
		if (Integer.class.isAssignableFrom(clazz) || Long.class.isAssignableFrom(clazz)//
				|| Float.class.isAssignableFrom(clazz) || Double.class.isAssignableFrom(clazz)) {
			return Lists.newArrayList(new Param(name, String.valueOf(value)));
		}

		// 日期
		if (Date.class.isAssignableFrom(clazz)) {
			return Lists.newArrayList(new Param(name, new SimpleDateFormat(datePattern).format((Date) value)));
		}

		// 枚举
		if (Enum.class.isAssignableFrom(clazz)) {
			return Lists.newArrayList(new Param(name, ((Enum<?>) value).name()));
		}

		// map
		if (Map.class.isAssignableFrom(clazz)) {
			Map<Object, Object> map = (Map<Object, Object>) value;

			for (Object key : map.keySet()) {
				if (key == null) {
					continue;
				}
				Object v = map.get(key);
				if (v == null) {
					continue;
				}
				String k = key.toString();
				// name[attribute]
				params.addAll(getUrlencodedParams(name + "[" + k + "]", v, datePattern));
			}
			return params;
		}

		// list
		if (List.class.isAssignableFrom(clazz)) {
			List<?> list = (List<?>) value;

			for (int i = 0; i < list.size(); i++) {
				Object v = list.get(i);
				// name[index]
				params.addAll(getUrlencodedParams(name + "[" + i + "]", v, datePattern));
			}
			return params;
		}

		// 实体类
		List<Field> fields = getFields(clazz, new HashSet<String>());

		for (Field field : fields) {
			String fieldName = field.getName();
			Object v = getFieldValue(field, value, clazz);
			params.addAll(getUrlencodedParams(name + "[" + fieldName + "]", v, datePattern));
			// end for
		}

		return params;

	}

	// 获取属性值
	private static Object getFieldValue(Field field, Object obj, Class<?> clazz) {
		try {
			String name = field.getName();
			String upper = name.substring(0, 1).toUpperCase();
			if (name.length() > 1) {
				upper += name.substring(1);
			}
			String methodName = null;
			if (Boolean.class.isAssignableFrom(field.getType()) || boolean.class.isAssignableFrom(field.getType())) {
				methodName = "is" + upper;
			} else {
				methodName = "get" + upper;
			}
			Method method = clazz.getMethod(methodName);
			return method.invoke(obj);
		} catch (Exception e) {
		}
		return null;
	}

	/**
	 * 获取属性(去重)
	 * @param clazz 类型
	 * @param names 当前已经有的属性名称
	 * @return 属性名称集合
	 */
	private static List<Field> getFields(Class<?> clazz, Set<String> names) {
		Validate.notNull(names, "names is null.");
		List<Field> fieldList = Lists.newArrayList();
		if (clazz == null) {
			return fieldList;
		}

		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) {
			String name = field.getName();
			if (names.contains(name)) {
				continue;
			}
			fieldList.add(field);
			names.add(name);
		}

		fieldList.addAll(getFields(clazz.getSuperclass(), names));
		return fieldList;
	}

	// 参数
	public static class Param {
		public String name;
		public String value;

		public Param(String name, String value) {
			this.name = name;
			this.value = value;
		}
		// end
	}
	// end param

	/**
	 * 对值进行编码
	 * @param value 值
	 * @param charset 编码类型
	 * @return 编码后的值
	 */
	public static String encoded(String value, String charset) {
		if (value == null) {
			return null;
		}

		try {
			return URLEncoder.encode(value, charset);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
