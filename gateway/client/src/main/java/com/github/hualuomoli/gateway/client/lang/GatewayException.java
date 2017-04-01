package com.github.hualuomoli.gateway.client.lang;

/**
 * 网关调用错误
 * @author lbq
 *
 */
@SuppressWarnings("serial")
public class GatewayException extends Exception {

	private String code;

	public GatewayException(String code, String message) {
		super(message);
		this.code = code;
	}

	public GatewayException(String code, String message, Throwable cause) {
		super(message, cause);
		this.code = code;
	}

	public String getCode() {
		return code;
	}

}
