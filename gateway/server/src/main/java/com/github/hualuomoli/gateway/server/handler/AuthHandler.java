package com.github.hualuomoli.gateway.server.handler;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.github.hualuomoli.gateway.server.loader.PartnerLoader.Partner;
import com.github.hualuomoli.gateway.server.parser.JSONParser;
import com.github.hualuomoli.gateway.server.processor.ExceptionProcessor;

/**
 * 网关认证执行者
 * @author lbq
 *
 */
public interface AuthHandler {

	/** 网关版本号 */
	final String VERSION = "1.0.0";

	/**
	 * 是否支持
	 * @param req HTTP请求
	 * @param res HTTP响应
	 * @param partner 合作伙伴
	 * @return 是否支持
	 */
	boolean support(HttpServletRequest req, HttpServletResponse res, Partner partner);

	/**
	 * 执行
	 * @param req HTTP请求
	 * @param res HTTP响应
	 * @param partner 合作伙伴
	 * @param jsonParser JSON转换器
	 * @param businessHandler 业务处理 
	 * @param interceptors 拦截器
	 * @param exceptionProcessor 业务错误处理
	 * @return 业务处理结果
	 * @throws InvalidEncryptionException 不合法的加密
	 * @throws InvalidSignatureException 不合法的签名
	 */
	AuthResponse execute(HttpServletRequest req, HttpServletResponse res, Partner partner, JSONParser jsonParser//
	, BusinessHandler businessHandler, List<BusinessHandler.HandlerInterceptor> interceptors//
	, ExceptionProcessor exceptionProcessor) //
			throws InvalidEncryptionException, InvalidSignatureException;

	// 权限请求公共信息
	public static class AuthRequest {
		/** 网关版本号 */
		@NotBlank(message = "gatewayVersion is blank.")
		protected String gatewayVersion;
		/** 合作伙伴ID */
		@NotBlank(message = "partnerId is blank.")
		protected String partnerId;
		/** 请求的业务方法 */
		@NotBlank(message = "apiMethod is blank.")
		protected String apiMethod;
		@NotBlank(message = "timestamp is blank.")
		@Pattern(regex = "\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}( \\d{0,4})?", message = "timestamp is invalid.")
		/** 时间戳 yyyy-MM-dd HH:mm:ss S */
		protected String timestamp;

		/** 业务内容 */
		protected String bizContent;

		public String getGatewayVersion() {
			return gatewayVersion;
		}

		public void setGatewayVersion(String gatewayVersion) {
			this.gatewayVersion = gatewayVersion;
		}

		public String getPartnerId() {
			return partnerId;
		}

		public void setPartnerId(String partnerId) {
			this.partnerId = partnerId;
		}

		public String getApiMethod() {
			return apiMethod;
		}

		public void setApiMethod(String apiMethod) {
			this.apiMethod = apiMethod;
		}

		public String getTimestamp() {
			return timestamp;
		}

		public void setTimestamp(String timestamp) {
			this.timestamp = timestamp;
		}

		public String getBizContent() {
			return bizContent;
		}

		public void setBizContent(String bizContent) {
			this.bizContent = bizContent;
		}

		@Override
		public String toString() {
			return "AuthRequest [gatewayVersion=" + gatewayVersion + ", partnerId=" + partnerId + ", apiMethod=" + apiMethod + ", timestamp=" + timestamp + ", bizContent=" + bizContent + "]";
		}

	}

	// 权限响应公共信息
	public static class AuthResponse {

		/** 网关版本号 */
		protected String gatewayVersion;

		/** 合作伙伴ID */
		protected String partnerId;
		/** 请求的业务方法 */
		protected String apiMethod;
		/** 时间戳 yyyy-MM-dd HH:mm:ss S */
		protected String timestamp;

		/** 调用结果编码 #CodeEnum */
		protected String code;
		/** 调用结果信息 */
		protected String message;
		/** 业务处理编码 #CodeEnum */
		protected String subCode;
		/** 业务处理信息 */
		protected String subMessage;

		/** 响应内容 */
		protected String result;

		public String getGatewayVersion() {
			return gatewayVersion;
		}

		public void setGatewayVersion(String gatewayVersion) {
			this.gatewayVersion = gatewayVersion;
		}

		public String getPartnerId() {
			return partnerId;
		}

		public void setPartnerId(String partnerId) {
			this.partnerId = partnerId;
		}

		public String getApiMethod() {
			return apiMethod;
		}

		public void setApiMethod(String apiMethod) {
			this.apiMethod = apiMethod;
		}

		public String getTimestamp() {
			return timestamp;
		}

		public void setTimestamp(String timestamp) {
			this.timestamp = timestamp;
		}

		public String getCode() {
			return code;
		}

		public void setCode(String code) {
			this.code = code;
		}

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}

		public String getSubCode() {
			return subCode;
		}

		public void setSubCode(String subCode) {
			this.subCode = subCode;
		}

		public String getSubMessage() {
			return subMessage;
		}

		public void setSubMessage(String subMessage) {
			this.subMessage = subMessage;
		}

		public String getResult() {
			return result;
		}

		public void setResult(String result) {
			this.result = result;
		}

		@Override
		public String toString() {
			return "AuthResponse [gatewayVersion=" + gatewayVersion + ", partnerId=" + partnerId + ", apiMethod=" + apiMethod + ", timestamp=" + timestamp + ", code=" + code + ", message=" + message
					+ ", subCode=" + subCode + ", subMessage=" + subMessage + ", result=" + result + "]";
		}

	}

	// 不能为空字符串
	@Target({ FIELD })
	@Retention(RUNTIME)
	@interface NotBlank {
		String message();
	}

	// 正则表达式
	@Target({ FIELD })
	@Retention(RUNTIME)
	@interface Pattern {
		String regex();

		String message();
	}

	// 不合法的加密数据
	public static class InvalidEncryptionException extends RuntimeException {

		private static final long serialVersionUID = 5805857045397640691L;

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

	// 不合法的签名
	public class InvalidSignatureException extends RuntimeException {

		private static final long serialVersionUID = 4520938290693412568L;

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

	// end

}
