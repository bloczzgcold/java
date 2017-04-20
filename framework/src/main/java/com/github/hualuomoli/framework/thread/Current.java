package com.github.hualuomoli.framework.thread;

import java.util.Date;

/**
 * 当前线程数据
 * @author lbq
 *
 */
public class Current {

	// 应用
	private static final ThreadLocal<String> LOCAL_APPLICATION = new ThreadLocal<>();
	// 处理用户
	private static final ThreadLocal<String> LOCAL_USERNAME = new ThreadLocal<>();
	// 日期
	private static final ThreadLocal<Date> LOCAL_DATE = new ThreadLocal<Date>();

	/**
	 * 设置当前线程应用
	 * @param application 应用名称
	 */
	public static void setApplication(String application) {
		LOCAL_APPLICATION.set(application);
	}

	/**
	 * 获取当前线程应用名称
	 * @return 应用名称
	 */
	public static String getApplication() {
		return LOCAL_APPLICATION.get();
	}

	/**
	 * 设置当前线程用户
	 * @param username 用户名
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
		Date date = LOCAL_DATE.get();
		if (date == null) {
			date = new Date();
			setDate(date);
		}
		return date;
	}

}
