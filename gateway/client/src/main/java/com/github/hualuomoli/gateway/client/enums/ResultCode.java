package com.github.hualuomoli.gateway.client.enums;

/**
 * 响应编码
 * 
 * @author lbq
 *
 */
public enum ResultCode {

	/** 处理成功 */
	SUCCESS("0000"),
	/** 合作伙伴未找到 */
	NO_PARTNER("0001"),
	/** 合作伙伴没有操作权限 */
	NO_PERMISSION("0002"),
	/** 签名错误 */
	SIGN("0003"),
	/** 加密错误 */
	ENCRYPT("0004"),
	/** 运行错误 */
	RUNTIME("0005"),
	/** 业务处理错误 */
	DEAL("1001");

	private String code;

	private ResultCode(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}

}
