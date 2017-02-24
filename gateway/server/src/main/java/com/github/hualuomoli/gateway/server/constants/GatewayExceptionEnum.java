package com.github.hualuomoli.gateway.server.constants;

/**
 * 网关异常枚举
 * @author lbq
 *
 */
public enum GatewayExceptionEnum {

	/** 合作伙伴未找到 */
	NO_PARTNER_FOUND(),
	/** 不合法的签名 */
	INVALID_SIGNATURE(),
	/** 不合法的加密 */
	INVALID_ENCRYPTION();

}
