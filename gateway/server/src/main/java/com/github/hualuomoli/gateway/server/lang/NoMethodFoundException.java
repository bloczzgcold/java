package com.github.hualuomoli.gateway.server.lang;

/**
 * 没有执行方法
 * @author lbq
 *
 */
public class NoMethodFoundException extends Exception {

	private static final long serialVersionUID = -8013811701401811489L;

	public NoMethodFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public NoMethodFoundException(String message) {
		super(message);
	}

}
