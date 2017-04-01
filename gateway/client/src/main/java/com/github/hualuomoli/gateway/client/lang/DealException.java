package com.github.hualuomoli.gateway.client.lang;

/**
 * 业务处理失败
 * @author lbq
 *
 */
@SuppressWarnings("serial")
public class DealException extends Exception {

	private String subCode;
	private String subMessage;

	public DealException(String subCode, String subMessage) {
		super(subMessage);
		this.subCode = subCode;
		this.subMessage = subMessage;
	}

	public DealException(String subCode, String subMessage, Throwable t) {
		super(subMessage, t);
		this.subCode = subCode;
		this.subMessage = subMessage;
	}

	public String getSubCode() {
		return subCode;
	}

	public String getSubMessage() {
		return subMessage;
	}

}
