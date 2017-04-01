package com.github.hualuomoli.gateway.server;

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
import com.github.hualuomoli.gateway.server.lang.InvalidSignatureException;
import com.github.hualuomoli.gateway.server.lang.NoMethodFoundException;
import com.github.hualuomoli.gateway.server.loader.PartnerLoader;
import com.github.hualuomoli.gateway.server.loader.PartnerLoader.Partner;
import com.github.hualuomoli.gateway.server.parser.JSONParser;
import com.github.hualuomoli.gateway.server.processor.ExceptionProcessor;
import com.github.hualuomoli.gateway.server.processor.ExceptionProcessor.Message;
import com.github.hualuomoli.validator.lang.InvalidParameterException;

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
	 * @param businessHandler　		业务处理者
	 * @param jsonParser			JSON转换器
	 * @param authExecutions　		权限执行者
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
	public String invoke(HttpServletRequest req, HttpServletResponse res) {

		AuthResponse authRes = null;

		// 合作伙伴
		Partner partner = getPartner(req.getParameter(NameEnum.IN_OUT_PARTNERID.value()));
		if (partner == null) {
			authRes = new AuthResponse();
			authRes.code = CodeEnum.NO_PARTNER_FOUND.value();
			authRes.message = "合作伙伴未找到";
			return jsonParser.toJsonString(authRes);
		}

		// 权限执行者
		AuthExecution authExecution = getAuthExecution(partner, req, res);
		if (authExecution == null) {
			authRes = new AuthResponse();
			authRes.code = CodeEnum.NO_AUTH_EXECUTION_FOUND.value();
			authRes.message = "网关执行者未找到";
			return jsonParser.toJsonString(authRes);
		}

		// 执行业务
		try {
			authRes = authExecution.deal(partner, jsonParser, req, res, businessHandler);
			return jsonParser.toJsonString(authRes);
		} catch (InvalidSignatureException ise) {
			// 签名不合法
			authRes = new AuthResponse();
			authRes.code = CodeEnum.INVALID_SIGNATURE.value();
			authRes.message = "不合法的签名数据";
			return jsonParser.toJsonString(authRes);
		} catch (InvalidEncryptionException iee) {
			// 加密不合法
			authRes = new AuthResponse();
			authRes.code = CodeEnum.INVALID_ENCRYPTION.value();
			authRes.message = "不合法的加密数据";
			return jsonParser.toJsonString(authRes);
		} catch (NoMethodFoundException nmfe) {
			// 请求方法未找到
			authRes = new AuthResponse();
			authRes.code = CodeEnum.NO_BUSINESS_HANDLER_FOUND.value();
			authRes.message = nmfe.getMessage();
			return jsonParser.toJsonString(authRes);
		} catch (InvalidParameterException ipe) {
			// 实体类要求参数规则不合法
			authRes = new AuthResponse();
			authRes.code = CodeEnum.INVALID_PARAMETER.value();
			authRes.message = ipe.getMessage();
			return jsonParser.toJsonString(authRes);
		} catch (Throwable e) {
			// 其他错误
			Message message = exceptionProcessor.process(e);
			authRes = new AuthResponse();
			authRes.code = CodeEnum.SUCCESS.value();
			authRes.subCode = message.getCode();
			authRes.subMessage = message.getMessage();
			return jsonParser.toJsonString(authRes);
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
