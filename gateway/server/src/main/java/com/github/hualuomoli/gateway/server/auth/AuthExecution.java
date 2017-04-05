package com.github.hualuomoli.gateway.server.auth;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.validator.constraints.NotEmpty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.hualuomoli.gateway.server.business.BusinessHandler;
import com.github.hualuomoli.gateway.server.loader.PartnerLoader.Partner;
import com.github.hualuomoli.gateway.server.parser.JSONParser;

/**
 * 网关认证执行者
 * @author lbq
 *
 */
public interface AuthExecution {

	static final Logger logger = LoggerFactory.getLogger(AuthExecution.class);

	/**
	 * 是否支持
	 * @param partner 			合作伙伴信息
	 * @param req 				HTTP请求
	 * @param res 				HTTP响应
	 * @return 如果支持该方式请求,返回true,否则返回false
	 */
	boolean support(Partner partner, HttpServletRequest req, HttpServletResponse res);

	/**
	 * 处理请求,使用
	 * @param partner 		合作伙伴信息
	 * @param jsonParser	JSON转换器
	 * @param req 			HTTP请求
	 * @param res 			HTTP响应
	 * @param handler 		业务处理者
	 * @return 处理结果
	 * @throws Throwable 处理过程中出现的异常
	 */
	AuthResponse deal(Partner partner, JSONParser jsonParser, HttpServletRequest req, HttpServletResponse res, BusinessHandler handler, List<Filter> filters) throws Throwable;

	// 过滤器
	interface Filter {

		/**
		 * 预处理
		 * @param partnerId 合作伙伴ID
		 * @param apiMethod 请求方法
		 * @throws 预处理错误
		 */
		void preHandler(String partnerId, String apiMethod) throws Exception;

	}

	// 权限请求公共信息
	class AuthRequest {

		/** 合作伙伴ID */
		@NotEmpty(message = "合作伙伴不能为空")
		protected String partnerId;
		/** 请求的业务方法 */
		@NotEmpty(message = "请求业务方法不能为空")
		protected String apiMethod;
		/** 时间戳 yyyyMMddHHmmss */
		@NotEmpty(message = "请求时间戳不能为空")
		protected String timestamp;
		/** 业务内容 */
		@NotEmpty(message = "请求业务内容不能为空")
		protected String bizContent;

	}

	// 权限响应公共信息
	public class AuthResponse {

		/** 调用结果编码 #CodeEnum */
		public String code;
		/** 调用结果信息 */
		public String message;
		/** 业务处理编码 #CodeEnum */
		public String subCode;
		/** 业务处理信息 */
		public String subMessage;
		/** 合作伙伴ID */
		protected String partnerId;
		/** 时间戳 yyyyMMddHHmmss */
		protected String timestamp;
		/** 响应内容 */
		protected String result;

		public String getCode() {
			return code;
		}

		public String getMessage() {
			return message;
		}

		public String getSubCode() {
			return subCode;
		}

		public String getSubMessage() {
			return subMessage;
		}

		public String getPartnerId() {
			return partnerId;
		}

		public String getTimestamp() {
			return timestamp;
		}

		public String getResult() {
			return result;
		}

	}

}
