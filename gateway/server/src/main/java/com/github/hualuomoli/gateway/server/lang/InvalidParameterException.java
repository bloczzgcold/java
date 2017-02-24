package com.github.hualuomoli.gateway.server.lang;

/**
 * 不合法的参数
 * @author lbq
 *
 */
public class InvalidParameterException extends Exception {

	private static final long serialVersionUID = -6194287983093792180L;

	public InvalidParameterException() {
		super();
	}

	public InvalidParameterException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidParameterException(String message) {
		super(message);
	}

	public InvalidParameterException(Throwable cause) {
		super(cause);
	}

}
