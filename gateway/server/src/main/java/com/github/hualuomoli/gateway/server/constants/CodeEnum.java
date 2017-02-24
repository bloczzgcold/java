package com.github.hualuomoli.gateway.server.constants;

/**
 * 编码枚举
 * @author hualuomoli
 *
 */
public enum CodeEnum {

	/** 处理成功 */
	SUCCESS("0000", "处理成功"),
	/** 合作伙伴未找到 */
	NO_PARTNER_FOUND("0001", "合作伙伴未找到"),
	/** 网关执行者未找到 */
	NO_AUTH_EXECUTION_FOUND("0002", "网关执行者未找到"),
	/** 业务处理者未找到 */
	NO_BUSINESS_HANDLER_FOUND("0003", "业务处理者未找到"),
	/** 签名不合法 */
	INVALID_SIGNATURE("0004", "签名不合法"),
	/** 加密不合法 */
	INVALID_ENCRYPTION("0005", "加密不合法"),
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
