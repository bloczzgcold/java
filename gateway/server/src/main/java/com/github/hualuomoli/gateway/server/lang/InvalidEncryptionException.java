package com.github.hualuomoli.gateway.server.lang;

/**
 * 不合法的加密数据
 * @author lbq
 *
 */
public class InvalidEncryptionException extends Exception {

	private static final long serialVersionUID = 3452735332561462491L;

	public InvalidEncryptionException() {
		super();
	}

	public InvalidEncryptionException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidEncryptionException(String message) {
		super(message);
	}

	public InvalidEncryptionException(Throwable cause) {
		super(cause);
	}

}
