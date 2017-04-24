package com.github.hualuomoli.gateway.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.github.hualuomoli.gateway.server.handler.AuthHandler;
import com.github.hualuomoli.gateway.server.handler.AuthHandler.InvalidEncryptionException;
import com.github.hualuomoli.gateway.server.handler.AuthHandler.InvalidSignatureException;
import com.github.hualuomoli.gateway.server.handler.BusinessHandler;
import com.github.hualuomoli.gateway.server.loader.PartnerLoader;
import com.github.hualuomoli.gateway.server.loader.PartnerLoader.Partner;
import com.github.hualuomoli.gateway.server.parser.JSONParser;
import com.github.hualuomoli.gateway.server.processor.ExceptionProcessor;

/**
 * 网关服务器
 * @author lbq
 *
 */
public class GatewayServer {

	private Map<String, Partner> partners = new HashMap<String, Partner>();

	private PartnerLoader partnerLoader;
	private ExceptionProcessor exceptionProcessor;
	private BusinessHandler businessHandler;
	private JSONParser jsonParser;
	private List<AuthHandler> authHandlers = new ArrayList<AuthHandler>();
	private List<BusinessHandler.HandlerInterceptor> interceptors = new ArrayList<BusinessHandler.HandlerInterceptor>();

	public GatewayServer(PartnerLoader partnerLoader//
	, ExceptionProcessor exceptionProcessor//
	, BusinessHandler businessHandler //
	, JSONParser jsonParser //
	, List<AuthHandler> authHandlers //
	, List<BusinessHandler.HandlerInterceptor> interceptors) {

		this.partnerLoader = partnerLoader;
		this.exceptionProcessor = exceptionProcessor;
		this.businessHandler = businessHandler;
		this.jsonParser = jsonParser;
		this.authHandlers = authHandlers;
		this.interceptors = interceptors;
	}

	/**
	 * 调用
	 * @param req HTTP请求
	 * @param res HTTP响应
	 * @throws NoPartnerException 合作伙伴未找到
	 * @throws NotSupportAuthException 不支持的权限验证
	 * @throws InvalidSignatureException 不合法的签名
	 * @throws InvalidEncryptionException 不合法的加密
	 * 
	 */
	public String invoke(HttpServletRequest req, HttpServletResponse res)//
			throws NoPartnerException, NotSupportAuthException, InvalidSignatureException, InvalidEncryptionException {

		AuthHandler.AuthResponse authRes = null;

		// 合作伙伴
		String partnerId = req.getParameter("partnerId");
		if (partnerId == null || partnerId.trim().length() == 0) {
			throw new IllegalArgumentException("请设置partnerId请求参数");
		}

		Partner partner = getPartner(partnerId);
		if (partner == null) {
			throw new NoPartnerException(partnerId);
		}

		// 权限执行者
		AuthHandler authHandler = getAuthHandler(partner, req, res);
		if (authHandler == null) {
			throw new NotSupportAuthException(partner);
		}

		// 执行业务
		authRes = authHandler.execute(req, res, partner, jsonParser, businessHandler, interceptors, exceptionProcessor);
		return jsonParser.toJsonString(authRes);
	}

	/**
	 * 获取权限执行者
	 * @param partner 合作伙伴
	 * @param req HTTP请求
	 * @param res HTTP响应
	 * @return 权限执行者,如果没有找到返回null
	 */
	private AuthHandler getAuthHandler(Partner partner, HttpServletRequest req, HttpServletResponse res) {
		for (AuthHandler authHandler : authHandlers) {
			if (authHandler.support(req, res, partner)) {
				return authHandler;
			}
		}
		return null;
	}

	/**
	 * 获取合作伙伴信息 
	 * @param partnerId 合作伙伴ID
	 * @return 合作伙伴信息,如果合作伙伴不存在,返回null
	 */
	private Partner getPartner(String partnerId) {
		Partner partner = partners.get(partnerId);

		if (partner != null) {
			return partner;
		}

		partner = partnerLoader.load(partnerId);
		if (partner != null) {
			partners.put(partnerId, partner);
		}

		return partner;
	}

	// 合作伙伴未找到
	@SuppressWarnings("serial")
	public static class NoPartnerException extends RuntimeException {

		private String partnerId;

		public NoPartnerException(String partnerId) {
			super();
			this.partnerId = partnerId;
		}

		public String getPartnerId() {
			return partnerId;
		}
	}

	// 不支持的权限认证
	@SuppressWarnings("serial")
	public static class NotSupportAuthException extends RuntimeException {

		private Partner partner;

		public NotSupportAuthException(Partner partner) {
			super();
			this.partner = partner;
		}

		public Partner getPartner() {
			return partner;
		}
	}

}
