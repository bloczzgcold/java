package com.github.hualuomoli.gateway.server.constants;

/**
 * 名称枚举
 * @author lbq
 *
 */
public enum NameEnum {

	/** API版本号 */
	API_VERSION("apiVersion"),

	// ==================== 输入输出参数 ====================
	/** 合作伙伴ID */
	IN_OUT_PARTNERID("partnerId"),
	/** 时间戳 */
	IN_OUT_TIMESTAMP("timestamp"),
	/** 签名类型 */
	IN_OUT_SIGNTYPE("signType"),
	/** 签名数据 */
	IN_OUT_SIGN("sign"),
	/** 加密类型 */
	IN_OUT_ENCRYPTTYPE("encryptType"),

	// ==================== 输入参数 ====================
	/** 请求方法 */
	IN_API_METHOD("apiMethod"),
	/** 请求业务内容 */
	IN_BIZCONTENT("bizContent"),

	// ==================== 输出参数 ====================
	/** 网关响应编码 */
	OUT_CODE("code"),
	/** 网关响应信息 */
	OUT_MESSAGE("message"),
	/** 业务响应编码 */
	OUT_SUB_CODE("subCode"),
	/** 业务响应内容 */
	OUT_SUB_MESSAGE("subMessage"),
	/** 业务响应结果 */
	OUT_RESULT("result");

	private String value;

	private NameEnum(String value) {
		this.value = value;
	}

	public String value() {
		return value;
	}

}
