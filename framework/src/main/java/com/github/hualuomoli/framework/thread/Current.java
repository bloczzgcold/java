package com.github.hualuomoli.framework.thread;

import java.util.Date;

/**
 * 当前线程数据
 * @author lbq
 *
 */
public class Current {

	private static final ThreadLocal<String> LOCAL_USERNAME = new ThreadLocal<>();
	private static final ThreadLocal<Date> LOCAL_DATE = new ThreadLocal<Date>();

	/**
	 * 设置当前线程用户
	 * @param username 当前线程用户
	 */
	public static void setUsername(String username) {
		LOCAL_USERNAME.set(username);
	}

	/**
	 * 获取当前线程用户
	 * @return 当前线程用户
	 */
	public static String getUsername() {
		return LOCAL_USERNAME.get();
	}

	/**
	 * 设置当前线程日期
	 * @param date 当前线程日期
	 */
	public static void setDate(Date date) {
		LOCAL_DATE.set(date);
	}

	/**
	 * 获取当前线程日期
	 * @return 当前线程日期
	 */
	public static Date getDate() {
		return LOCAL_DATE.get();
	}

}
