package com.github.hualuomoli.gateway.server.lang;

/**
 * 没有合作伙伴
 * @author lbq
 *
 */
public class NoPartnerFoundException extends Exception {

	private static final long serialVersionUID = -6534039408004364071L;

	public NoPartnerFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public NoPartnerFoundException(String message) {
		super(message);
	}

}
