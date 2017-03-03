package com.github.hualuomoli.framework.mvc.condition;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.mvc.condition.RequestCondition;

/**
 * 根据版本号请求
 * @author hualuomoli
 *
 */
public class VersionRequestCondition implements RequestCondition<VersionRequestCondition> {

	private static final String DEFAULT_VERSION = "0.0.0";

	private String version; // 版本

	public VersionRequestCondition() {
		this(DEFAULT_VERSION);
	}

	public VersionRequestCondition(String version) {
		this.version = version;
	}

	/**
	 * 采用最后定义优先原则
	 * 方法上的定义覆盖类上面的定义
	 */
	@Override
	public VersionRequestCondition combine(VersionRequestCondition other) {
		return new VersionRequestCondition(other.getVersion());
	}

	// 获取符合的条件
	@Override
	public VersionRequestCondition getMatchingCondition(HttpServletRequest request) {
		// 请求版本号
		String version = this.getRequestVersion(request);

		// 没有指定版本号
		if (version == null) {
			return this;
		}

		// 如果指定版本号与当前版本号相同,该版本符合
		if (this.version.equals(version)) {
			return this;
		}

		int c = this.compareTo(this.version, version);
		return c > 0 ? null : this;
	}

	// 版本号排序,版本号越大越靠前
	@Override
	public int compareTo(VersionRequestCondition other, HttpServletRequest request) {
		return this.compareTo(other.getVersion(), this.getVersion());
	}

	/**
	 * 比较
	 * @param version1 第一个版本
	 * @param version2 第二个版本
	 * @return 相等返回0,第一个比第二个大返回1,第一个比第二个小返回-1
	 */
	private int compareTo(String version1, String version2) {

		if (version1 == null && version2 == null) {
			return 0;
		}
		if (version1 == null) {
			return -1;
		}
		if (version2 == null) {
			return 1;
		}

		if (version1.equals(version2)) {
			return 0;
		}

		String[] array1 = version1.split("[.]");
		String[] array2 = version2.split("[.]");
		int len1 = array1.length;
		int len2 = array2.length;

		int length = len1 > len2 ? len2 : len1;
		for (int i = 0; i < length; i++) {
			int value1 = Integer.parseInt(array1[i]);
			int value2 = Integer.parseInt(array2[i]);

			// 如果相等,继续下一个比较
			if (value1 == value2) {
				continue;
			}

			return value1 > value2 ? 1 : -1;
		}

		return len1 > len2 ? 1 : -1;

	}

	/**
	 * 获取请求的版本,如果没有制定请求版本号,使用最大版本号
	 * @param request HTTP请求
	 * @return 版本号
	 */
	private String getRequestVersion(HttpServletRequest request) {
		String version = request.getHeader("apiVersion");
		if (version == null || version.trim().length() == 0) {
			return null;
		}
		return version.trim();
	}

	public String getVersion() {
		return version;
	}

	@Override
	public int hashCode() {
		return getVersion().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj != null && obj instanceof VersionRequestCondition) {
			VersionRequestCondition c = (VersionRequestCondition) obj;
			return StringUtils.equals(this.getVersion(), c.getVersion());
		}
		return false;
	}

	@Override
	public String toString() {
		return version;
	}

}
