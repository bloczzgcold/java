package com.github.hualuomoli.framework;

import java.util.Date;

/**
 * 当前线程数据
 */
public class CurrentThread {

	// 应用
	private static final ThreadLocal<String> LOCAL_APPLICATION = new ThreadLocal<>();
	// 操作用户
	private static final ThreadLocal<String> LOCAL_OPERATOR = new ThreadLocal<>();
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
	 * 设置当前线程操作用户
	 * @param operator 操作用户
	 */
	public static void setOperator(String operator) {
		LOCAL_OPERATOR.set(operator);
	}

	/**
	 * 获取当前线程操作用户
	 * @return 当前线程操作用户
	 */
	public static String getOperator() {
		return LOCAL_OPERATOR.get();
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
