package com.github.hualuomoli.gateway.client.enums;

/**
 * 响应编码
 * 
 * @author lbq
 *
 */
public enum ResultCode {

	/** 处理成功 */
	SUCCESS("0000", "处理成功"),
	/** 合作伙伴未找到 */
	NO_PARTNER("0001", "合作伙伴未找到"),
	/** 网关执行者未找到 */
	NO_AUTH_HANDLER("0002", "网关未找到"),
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
	private String code;
	/** 信息 */
	private String message;

	private ResultCode(String code, String message) {
		this.code = code;
		this.message = message;
	}

	public String code() {
		return code;
	}

	public String message() {
		return message;
	}

}
