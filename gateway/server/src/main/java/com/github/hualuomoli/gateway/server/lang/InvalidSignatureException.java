package com.github.hualuomoli.gateway.server.lang;

/**
 * 不合法的签名
 * @author lbq
 *
 */
public class InvalidSignatureException extends Exception {

	private static final long serialVersionUID = 4795788102124710636L;

	public InvalidSignatureException() {
		super();
	}

	public InvalidSignatureException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidSignatureException(String message) {
		super(message);
	}

	public InvalidSignatureException(Throwable cause) {
		super(cause);
	}

}
