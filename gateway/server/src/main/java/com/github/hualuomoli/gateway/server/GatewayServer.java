package com.github.hualuomoli.gateway.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.github.hualuomoli.gateway.server.auth.AuthExecution;
import com.github.hualuomoli.gateway.server.auth.AuthExecution.AuthResponse;
import com.github.hualuomoli.gateway.server.business.BusinessHandler;
import com.github.hualuomoli.gateway.server.constants.CodeEnum;
import com.github.hualuomoli.gateway.server.constants.NameEnum;
import com.github.hualuomoli.gateway.server.loader.PartnerLoader;
import com.github.hualuomoli.gateway.server.loader.PartnerLoader.Partner;
import com.github.hualuomoli.gateway.server.parser.JSONParser;
import com.github.hualuomoli.gateway.server.processor.ExceptionProcessor;
import com.github.hualuomoli.gateway.server.processor.ExceptionProcessor.Message;

public class GatewayServer {

	private static final Map<String, Partner> partners = new HashMap<String, Partner>();

	private static PartnerLoader partnerLoader;
	private static ExceptionProcessor exceptionProcessor;
	private static BusinessHandler businessHandler;
	private static JSONParser jsonParser;
	private static final List<AuthExecution> authExecutions = new ArrayList<AuthExecution>();

	private static boolean init = false;

	/**
	 * 初始化
	 * @param partnerLoader　		合作伙伴加载器
	 * @param exceptionProcessor　	异常处理器
	 * @param jsonParser			JSON转换器
	 * @param authExecutions　		权限执行者
	 * @param businessHandlers　		业务处理者
	 */
	public static final void config(PartnerLoader partnerLoader//
			, ExceptionProcessor exceptionProcessor//
			, BusinessHandler businessHandler //
			, JSONParser jsonParser //
			, List<AuthExecution> authExecutions) {

		if (partnerLoader == null //
				|| exceptionProcessor == null //
				|| businessHandler == null //
				|| jsonParser == null //
				|| authExecutions == null || authExecutions.size() == 0) {
			throw new RuntimeException("please use valid configure.");
		}

		GatewayServer.partnerLoader = partnerLoader;
		GatewayServer.exceptionProcessor = exceptionProcessor;
		GatewayServer.businessHandler = businessHandler;
		GatewayServer.jsonParser = jsonParser;
		GatewayServer.authExecutions.addAll(authExecutions);

		init = true;
	}

	/**
	 * 调用
	 * @param req HTTP请求
	 * @param res HTTP响应
	 */
	public static final void invoke(HttpServletRequest req, HttpServletResponse res) {
		if (!init) {
			throw new RuntimeException("please use GatewayServer.config init.");
		}

		AuthResponse authRes = null;

		// 合作伙伴
		Partner partner = getPartner(req.getParameter(NameEnum.IN_OUT_PARTNERID.value()));
		if (partner == null) {
			authRes = new AuthResponse();
			authRes.code = CodeEnum.NO_PARTNER_FOUND.value();
			authRes.message = "合作伙伴未找到";
			flush(authRes, req, res);
			return;
		}

		// 权限执行者
		AuthExecution authExecution = getAuthExecution(partner, req, res);
		if (authExecution == null) {
			authRes = new AuthResponse();
			authRes.code = CodeEnum.NO_AUTH_EXECUTION_FOUND.value();
			authRes.message = "网关执行者未找到";
			flush(authRes, req, res);
			return;
		}

		// 执行业务
		try {
			authRes = authExecution.deal(partner, jsonParser, req, res, businessHandler);
			flush(authRes, req, res);
		} catch (Throwable e) {
			Message message = exceptionProcessor.process(e);
			authRes = new AuthResponse();
			authRes.code = CodeEnum.SUCCESS.value();
			authRes.subCode = message.getCode();
			authRes.subMessage = message.getMessage();
			flush(authRes, req, res);
		}
	}

	/**
	 * 输出数据
	 * @param authRes 	响应内容
	 * @param req 		HTTP请求
	 * @param res 		HTTP响应
	 */
	private static void flush(AuthResponse authRes, HttpServletRequest req, HttpServletResponse res) {
		String flushData = jsonParser.toJsonString(authRes);

		res.setContentType("application/json");
		res.setCharacterEncoding("UTF-8");

		try {
			res.getWriter().println(flushData);
		} catch (IOException e) {
		}

	}

	/**
	 * 获取权限执行者
	 * @param partner 合作伙伴
	 * @param req HTTP请求
	 * @param res HTTP响应
	 * @return 权限执行者,如果没有找到返回null
	 */
	private static AuthExecution getAuthExecution(Partner partner, HttpServletRequest req, HttpServletResponse res) {
		for (AuthExecution authExecution : authExecutions) {
			if (authExecution.support(partner, req, res)) {
				return authExecution;
			}
		}
		return null;
	}

	/**
	 * 获取合作伙伴信息 
	 * @param partnerId 合作伙伴ID
	 * @return 合作伙伴信息,如果合作伙伴不存在,返回null
	 */
	private static Partner getPartner(String partnerId) {
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

}
