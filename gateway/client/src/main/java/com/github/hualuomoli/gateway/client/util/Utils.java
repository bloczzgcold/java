package com.github.hualuomoli.gateway.client.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Utils {

	private static final Logger logger = LoggerFactory.getLogger(Utils.class);

	/**
	 * 获取类的所有属性及所有父属性
	 * @param clazz 类
	 * @param names 已经存在的属性
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
	 * 获取urlencoded放肆请求的参数
	 * @param object 参数
	 * @param datePattern 日期格式化方式
	 * @return 参数信息
	 */
	public static List<UrlencodedParam> getUrlencodedParams(Object object, DateFormat dateFormat) {
		Validate.notNull(dateFormat, "dateFormat is null.");

		List<UrlencodedParam> params = new ArrayList<UrlencodedParam>();
		if (object == null) {
			return params;
		}

		Class<?> clazz = object.getClass();
		List<Field> fields = Utils.getFields(clazz);
		for (Field field : fields) {
			String name = field.getName();
			Object value = Utils.getFieldValue(field, object, clazz);
			params.addAll(Utils.getUrlencodedParams(name, value, field, dateFormat));
		}

		return params;
	}

	/**
	 * 获取urlencoded放肆请求的参数
	 * @param object 参数
	 * @param datePattern 日期格式化方式
	 * @return 参数信息
	 */
	public static List<UrlencodedParam> getUrlencodedParams(Map<String, Object> map, DateFormat dateFormat) {
		Validate.notNull(dateFormat, "dateFormat is null.");

		List<UrlencodedParam> params = new ArrayList<UrlencodedParam>();
		if (map == null || map.size() == 0) {
			return params;
		}

		for (String name : map.keySet()) {
			params.addAll(Utils.getUrlencodedParams(name, map.get(name), null, dateFormat));
		}

		return params;
	}

	/**
	 * 获取urlencoded放肆请求的参数
	 * @param name 名称
	 * @param value 值
	 * @param field 属性
	 * @param dateFormat 日期格式化
	 * @return 参数列表
	 */
	@SuppressWarnings("unchecked")
	public static List<UrlencodedParam> getUrlencodedParams(String name, Object value, Field f, DateFormat dateFormat) {

		List<UrlencodedParam> params = new ArrayList<UrlencodedParam>();

		if (value == null) {
			return params;
		}

		Class<?> clazz = value.getClass();

		// 字符串
		if (String.class.isAssignableFrom(clazz)) {
			params.add(new UrlencodedParam(name, String.valueOf(value)));
			return params;
		}

		// 数值
		if (Integer.class.isAssignableFrom(clazz) || Long.class.isAssignableFrom(clazz)//
				|| Float.class.isAssignableFrom(clazz) || Double.class.isAssignableFrom(clazz)) {
			params.add(new UrlencodedParam(name, String.valueOf(value)));
			return params;
		}

		// 日期
		if (Date.class.isAssignableFrom(clazz)) {
			params.add(new UrlencodedParam(name, dateFormat.format((Date) value, f)));
			return params;
		}

		// 枚举
		if (Enum.class.isAssignableFrom(clazz)) {
			params.add(new UrlencodedParam(name, ((Enum<?>) value).name()));
			return params;
		}

		// map
		if (Map.class.isAssignableFrom(clazz)) {
			Map<Object, Object> map = (Map<Object, Object>) value;

			for (Object key : map.keySet()) {
				if (key == null) {
					continue;
				}
				String k = key.toString();
				Object v = map.get(key);
				// name[attribute]
				params.addAll(Utils.getUrlencodedParams(name + "[" + k + "]", v, f, dateFormat));
			}
			return params;
		}

		// list
		if (List.class.isAssignableFrom(clazz)) {
			List<?> list = (List<?>) value;

			for (int i = 0; i < list.size(); i++) {
				Object v = list.get(i);
				// name[index]
				params.addAll(getUrlencodedParams(name + "[" + i + "]", v, f, dateFormat));
			}
			return params;
		}

		// 实体类
		List<Field> fields = Utils.getFields(clazz, new HashSet<String>());

		for (Field field : fields) {
			String fieldName = field.getName();
			Object v = Utils.getFieldValue(field, value, clazz);
			params.addAll(Utils.getUrlencodedParams(name + "[" + fieldName + "]", v, field, dateFormat));
		}
		return params;
	}

	/**
	 * 获取属性值
	 * @param field 属性
	 * @param obj 数据
	 * @param clazz 数据类型
	 * @return 值
	 */
	public static Object getFieldValue(Field field, Object obj, Class<?> clazz) {

		String name = field.getName();

		try {
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
			logger.debug("无法获取属性{}", name, e);
		}
		return null;
	}

	// 参数
	public static class UrlencodedParam {
		public String name;
		public String value;

		public UrlencodedParam(String name, String value) {
			super();
			this.name = name;
			this.value = value;
		}
	}

	// 日期格式化
	public static interface DateFormat {

		/**
		 * 格式化日期
		 * @param date 日期
		 * @param field 实体类属性
		 * @return 日期字符串
		 */
		String format(Date date, Field field);

	}

	/**
	 * 排序
	 * @param list 集合
	 * @return 排序后的集合
	 */
	public static List<UrlencodedParam> sort(List<UrlencodedParam> list) {

		if (list == null || list.size() == 0) {
			return list;
		}

		Collections.sort(list, new Comparator<UrlencodedParam>() {

			@Override
			public int compare(UrlencodedParam o1, UrlencodedParam o2) {
				String name1 = o1.name;
				String name2 = o2.name;

				int len1 = name1.length();
				int len2 = name2.length();

				int len = len1 > len2 ? len2 : len1;

				for (int i = 0; i < len; i++) {
					int c = name1.charAt(i) - name2.charAt(i);

					if (c == 0) {
						continue;
					}

					return c;

				}

				return len1 - len2;
			}
		});

		return list;
	}

}
