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
import com.github.hualuomoli.gateway.server.lang.InvalidEncryptionException;
import com.github.hualuomoli.gateway.server.lang.InvalidParameterException;
import com.github.hualuomoli.gateway.server.lang.InvalidSignatureException;
import com.github.hualuomoli.gateway.server.lang.NoMethodFoundException;
import com.github.hualuomoli.gateway.server.loader.PartnerLoader;
import com.github.hualuomoli.gateway.server.loader.PartnerLoader.Partner;
import com.github.hualuomoli.gateway.server.parser.JSONParser;
import com.github.hualuomoli.gateway.server.processor.ExceptionProcessor;
import com.github.hualuomoli.gateway.server.processor.ExceptionProcessor.Message;

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
	private List<AuthExecution> authExecutions = new ArrayList<AuthExecution>();

	/**
	 * 初始化
	 * @param partnerLoader　		合作伙伴加载器
	 * @param exceptionProcessor　	异常处理器
	 * @param jsonParser			JSON转换器
	 * @param authExecutions　		权限执行者
	 * @param businessHandlers　		业务处理者
	 */
	public GatewayServer(PartnerLoader partnerLoader//
			, ExceptionProcessor exceptionProcessor//
			, BusinessHandler businessHandler //
			, JSONParser jsonParser //
			, List<AuthExecution> authExecutions) {

		this.partnerLoader = partnerLoader;
		this.exceptionProcessor = exceptionProcessor;
		this.businessHandler = businessHandler;
		this.jsonParser = jsonParser;
		this.authExecutions.addAll(authExecutions);

	}

	/**
	 * 调用
	 * @param req HTTP请求
	 * @param res HTTP响应
	 */
	public void invoke(HttpServletRequest req, HttpServletResponse res) {

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
		} catch (InvalidSignatureException ise) {
			authRes = new AuthResponse();
			authRes.code = CodeEnum.INVALID_SIGNATURE.value();
			authRes.message = "不合法的签名数据";
			flush(authRes, req, res);
		} catch (InvalidEncryptionException iee) {
			authRes = new AuthResponse();
			authRes.code = CodeEnum.INVALID_ENCRYPTION.value();
			authRes.message = "不合法的加密数据";
			flush(authRes, req, res);
		} catch (NoMethodFoundException nmfe) {
			authRes = new AuthResponse();
			authRes.code = CodeEnum.NO_BUSINESS_HANDLER_FOUND.value();
			authRes.message = nmfe.getMessage();
			flush(authRes, req, res);
		} catch (InvalidParameterException ipe) {
			authRes = new AuthResponse();
			authRes.code = CodeEnum.INVALID_PARAMETER.value();
			authRes.message = ipe.getMessage();
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
	private void flush(AuthResponse authRes, HttpServletRequest req, HttpServletResponse res) {
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
	private AuthExecution getAuthExecution(Partner partner, HttpServletRequest req, HttpServletResponse res) {
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

}