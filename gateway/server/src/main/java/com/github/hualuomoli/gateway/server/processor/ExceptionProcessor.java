package com.github.hualuomoli.gateway.server.processor;

/**
 * 异常处理器
 * @author lbq
 *
 */
public interface ExceptionProcessor {

	/**
	 * 处理错误
	 * @param t 错误
	 * @return 错误信息
	 */
	Message process(Throwable t);

	// 错误信息
	public static final class Message {
		/** 错误编码 */
		private String code;
		/** 错误信息 */
		private String message;
		/** 错误描述 */
		private String description;

		public Message(String code, String message, String description) {
			this.code = code;
			this.message = message;
			this.description = description;
		}

		public String getCode() {
			return code;
		}

		public String getMessage() {
			return message;
		}

		public String getDescription() {
			return description;
		}
	}

}
