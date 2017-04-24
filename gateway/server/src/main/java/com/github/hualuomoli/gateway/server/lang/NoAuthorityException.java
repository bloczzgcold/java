package com.github.hualuomoli.gateway.server.lang;

/**
 * 没有访问权限
 * @author baoquan
 *
 */
public class NoAuthorityException extends RuntimeException {

	private static final long serialVersionUID = -1161776557879892591L;

	public NoAuthorityException() {
		super();
	}

	public NoAuthorityException(String message, Throwable cause) {
		super(message, cause);
	}

	public NoAuthorityException(String message) {
		super(message);
	}

	public NoAuthorityException(Throwable cause) {
		super(cause);
	}

}
