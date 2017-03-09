package com.github.hualuomoli.tool.util;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Lists;

/**
 * 集合工具
 * @author lbq
 *
 */
public class Arrays {

	/**
	 * 转换为字符串
	 * @param list 数据集合
	 * @param parser 转换器
	 * @return 转换后的字符串
	 */
	public static <T> String toString(List<T> list, Parser<T> parser) {
		return StringUtils.join(toArray(list, parser));
	}

	/**
	 * 转换为字符串数组
	 * @param list 数据集合
	 * @param parser 转换器
	 * @return 转换后的字符串数组
	 */
	public static <T> String[] toArray(List<T> list, Parser<T> parser) {

		if (list == null || list.size() == 0) {
			return new String[] {};
		}

		List<String> sList = Lists.newArrayList();
		for (T t : list) {
			sList.add(parser.parse(t));
		}

		return sList.toArray(new String[] {});
	}

	// 转换器
	public static interface Parser<T> {

		/**
		 * 转换
		 * @param t 待转换的数据
		 * @return 转换后的数据
		 */
		String parse(T t);

	}

}
