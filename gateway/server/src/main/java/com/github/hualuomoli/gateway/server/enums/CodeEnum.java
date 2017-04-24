package com.github.hualuomoli.gateway.server.enums;

/**
 * 编码枚举
 * @author lbq
 *
 */
public enum CodeEnum {

	/** 处理成功 */
	SUCCESS("0000", "处理成功"),
	/** 签名不合法 */
	INVALID_SIGNATURE("0003", "签名不合法"),
	/** 加密不合法 */
	INVALID_ENCRYPTION("0004", "加密不合法"),
	/** 业务处理者未找到 */
	NO_BUSINESS_HANDLER_METHOD("0005", "请求方法未注册"),
	/** 没有请求权限 */
	NO_BUSINESS_HANDLER_AUTHORITY("0006", "没有请求权限"),
	/** 业务处理失败 */
	BUSINESS_ERROR("9999", "业务处理失败");

	/** 编码 */
	private String value;
	/** 信息 */
	private String message;

	private CodeEnum(String value, String message) {
		this.value = value;
		this.message = message;
	}

	public String value() {
		return value;
	}

	public String message() {
		return message;
	}

}
