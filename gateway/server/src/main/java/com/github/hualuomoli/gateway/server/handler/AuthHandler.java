package com.github.hualuomoli.gateway.server.handler;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.github.hualuomoli.gateway.server.loader.PartnerLoader.Partner;
import com.github.hualuomoli.gateway.server.parser.JSONParser;

/**
 * 网关认证执行者
 * @author lbq
 *
 */
public interface AuthHandler {

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
	 * @return 业务处理结果
	 * @throws Throwable 异常
	 */
	AuthResponse execute(HttpServletRequest req, HttpServletResponse res, Partner partner, JSONParser jsonParser//
	, BusinessHandler businessHandler, List<BusinessHandler.HandlerInterceptor> interceptors) throws Throwable;

	// 权限请求公共信息
	class AuthRequest {

		/** 合作伙伴ID */
		protected String partnerId;
		/** 请求的业务方法 */
		protected String apiMethod;
		/** 时间戳 yyyy-MM-dd HH:mm:ss S */
		protected String timestamp;
		/** 业务内容 */
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
		/** 请求的业务方法 */
		protected String apiMethod;
		/** 时间戳 yyyy-MM-dd HH:mm:ss S */
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
