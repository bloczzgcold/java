package com.github.hualuomoli.gateway.server.lang;

import java.util.Set;

/**
 * 不合法的请求参数(参数注解验证)
 * @author lbq
 *
 */
public class InvalidRequestParameterAnnotationException extends Exception {

	private static final long serialVersionUID = -2202285420399186821L;

	private Set<String> errors;

	public InvalidRequestParameterAnnotationException(Set<String> errors) {
		super();
	}

	public Set<String> getErrors() {
		return errors;
	}

}
