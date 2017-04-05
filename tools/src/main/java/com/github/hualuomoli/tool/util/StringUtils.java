package com.github.hualuomoli.tool.util;

public class StringUtils {

	/**
	 * 字符串比较
	 * @param s1
	 * @param s2
	 * @return
	 */
	public static int compare(String s1, String s2) {
		if (s1 == null && s2 == null) {
			return 0;
		}
		if (s2 == null) {
			return 1;
		}
		if (s1 == null) {
			return -1;
		}
		int len1 = s1.length();
		int len2 = s2.length();
		int len = len1 > len2 ? len2 : len1;
		for (int i = 0; i < len; i++) {
			int c = s1.charAt(i) - s2.charAt(i);
			if (c == 0) {
				continue;
			}
			return c;
		}
		return len1 > len2 ? 1 : -1;
	}

}
