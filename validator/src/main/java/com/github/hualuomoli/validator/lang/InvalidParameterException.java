package com.github.hualuomoli.validator.lang;

import java.util.Set;

import org.apache.commons.lang3.StringUtils;

/**
 * 不合法的参数
 * @author lbq
 *
 */
public class InvalidParameterException extends RuntimeException {

	private static final long serialVersionUID = 4331915009425653751L;

	private Set<String> messages;

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

	public InvalidParameterException(Set<String> messages) {
		super(StringUtils.join(messages, ","));
		this.messages = messages;
	}

	public Set<String> getMessages() {
		return messages;
	}

}
