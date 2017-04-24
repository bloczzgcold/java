package com.github.hualuomoli.gateway.server.handler;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.github.hualuomoli.gateway.server.parser.JSONParser;

/**
 * 业务处理者
 * @author lbq
 *
 */
public interface BusinessHandler {

	/**
	 * 业务处理
	 * @param req HTTP请求
	 * @param res HTTP响应
	 * @param partnerId 合作伙伴id
	 * @param apiMethod API方法名
	 * @param bizContent 请求的业务内容
	 * @param jsonParser JSON转换器
	 * @param interceptors 拦截器
	 * @return 业务处理结果
	 * @throws NoMethodFoundException 请求方法未找到
	 * @throws NoAuthorityException 没有访问权限
	 * @throws Throwable 业务处理发生的异常
	 */
	String handle(HttpServletRequest req, HttpServletResponse res//
	, String partnerId, String apiMethod, String bizContent, JSONParser jsonParser//
	, List<HandlerInterceptor> interceptors) throws NoMethodFoundException, NoAuthorityException, Throwable;

	// 方法未找到
	public static final class NoMethodFoundException extends RuntimeException {
		private static final long serialVersionUID = -4837058503357011640L;

		public NoMethodFoundException() {
			super();
		}

		public NoMethodFoundException(String message, Throwable cause) {
			super(message, cause);
		}

		public NoMethodFoundException(String message) {
			super(message);
		}

		public NoMethodFoundException(Throwable cause) {
			super(cause);
		}
	}

	// 没有访问权限
	public static final class NoAuthorityException extends RuntimeException {

		private static final long serialVersionUID = -7872913865614349550L;

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

	// 拦截器
	interface HandlerInterceptor {

		/**
		 * 预处理
		 * @param req HTTP请求
		 * @param res HTTP响应
		 * @param partnerId 合作或按id
		 * @param apiMethod API方法名
		 * @param handler 具体业务处理
		 * @throws Exception 异常
		 */
		void preHandle(HttpServletRequest req, HttpServletResponse res, String partnerId, String apiMethod, Object handler) throws Exception;

		/**
		 * 后处理
		 * @param req HTTP请求
		 * @param res HTTP响应
		 * @param handler 具体业务处理
		 * @throws Exception 异常
		 */
		void postHandle(HttpServletRequest req, HttpServletResponse res, Object handler) throws Exception;

		/**
		 * 业务执行完成后调用
		 * @param req HTTP请求
		 * @param res HTTP响应
		 * @param handler 具体业务处理
		 * @param t 异常信息
		 * @throws Exception 异常
		 */
		void afterCompletion(HttpServletRequest req, HttpServletResponse res, Object handler, Throwable t) throws Exception;

	}

}
