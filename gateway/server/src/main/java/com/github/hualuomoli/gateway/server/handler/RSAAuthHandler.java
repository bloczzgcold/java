package com.github.hualuomoli.gateway.server.handler;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.hualuomoli.gateway.server.enums.CodeEnum;
import com.github.hualuomoli.gateway.server.enums.SignatureTypeEnum;
import com.github.hualuomoli.gateway.server.handler.BusinessHandler.NoAuthorityException;
import com.github.hualuomoli.gateway.server.handler.BusinessHandler.NoMethodFoundException;
import com.github.hualuomoli.gateway.server.loader.PartnerLoader.Key;
import com.github.hualuomoli.gateway.server.loader.PartnerLoader.Partner;
import com.github.hualuomoli.gateway.server.parser.JSONParser;
import com.github.hualuomoli.gateway.server.processor.ExceptionProcessor;
import com.github.hualuomoli.gateway.server.processor.ExceptionProcessor.Message;
import com.github.hualuomoli.gateway.server.security.RSA;
import com.github.hualuomoli.gateway.server.util.Utils;

/**
 * 网关认证
 * @author lbq
 *
 */
public class RSAAuthHandler extends AbstractAuthHandler implements AuthHandler {

	private static final Logger logger = LoggerFactory.getLogger(RSAAuthHandler.class);

	/** RSA私钥 */
	private String privateKeyBase64;

	public RSAAuthHandler(String privateKeyBase64) {
		super();
		this.privateKeyBase64 = privateKeyBase64;
	}

	@Override
	public boolean support(HttpServletRequest req, HttpServletResponse res, Partner partner) {
		String signType = req.getParameter("signType");

		if (SignatureTypeEnum.RSA.name().equals(signType)) {
			return true;
		}

		return false;
	}

	@Override
	public AuthResponse execute(HttpServletRequest req, HttpServletResponse res//
	, Partner partner, JSONParser jsonParser//
	, BusinessHandler businessHandler, List<BusinessHandler.HandlerInterceptor> interceptors //
	, ExceptionProcessor exceptionProcessor) throws InvalidSignatureException {

		// 获取请求数据
		RSAAuthRequest rsaReq = this.parse(req, jsonParser, RSAAuthRequest.class);

		// 验证请求参数
		this.check(rsaReq);

		logger.info("请求业务内容 = {}", rsaReq.getBizContent());

		// 验证签名
		String origin = this.getOrigin(rsaReq, "sign");
		logger.debug("请求签名原文 = {}", origin);

		if (!RSA.verify(partner.getConfigs().get(Key.SIGNATURE_RSA_PUBLIC_KEY), origin, rsaReq.getSign())) {
			throw new InvalidSignatureException("不合法的签名");
		}

		// 响应数据
		RSAAuthResponse rsaRes = new RSAAuthResponse();
		rsaRes.setGatewayVersion(rsaReq.getGatewayVersion());
		rsaRes.setPartnerId(rsaReq.getPartnerId());
		rsaRes.setApiMethod(rsaReq.getApiMethod());
		rsaRes.setTimestamp(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss S").format(new Date()));
		rsaRes.setSignType(rsaReq.getSignType());

		// 执行业务
		String result = null;
		try {
			result = businessHandler.handle(req, res, rsaReq.partnerId, rsaReq.apiMethod, rsaReq.bizContent, jsonParser, interceptors);
			logger.info("响应业务内容={}", result);

			// 设置返回数据
			rsaRes.code = CodeEnum.SUCCESS.value();
			rsaRes.message = "调用成功";
			rsaRes.subCode = CodeEnum.SUCCESS.value();
			rsaRes.subMessage = "业务处理成功";

			rsaRes.result = result;
		} catch (NoMethodFoundException e) {
			rsaRes.code = CodeEnum.NO_BUSINESS_HANDLER_METHOD.value();
			rsaRes.message = CodeEnum.NO_BUSINESS_HANDLER_METHOD.message();
		} catch (NoAuthorityException e) {
			rsaRes.code = CodeEnum.NO_BUSINESS_HANDLER_AUTHORITY.value();
			rsaRes.message = CodeEnum.NO_BUSINESS_HANDLER_AUTHORITY.message();
		} catch (Throwable t) {
			Message message = exceptionProcessor.process(t);
			logger.debug("业务处理失败={}", message);

			rsaRes.code = CodeEnum.SUCCESS.value();
			rsaRes.message = "调用成功";
			rsaRes.subCode = message.getCode();
			rsaRes.subMessage = message.getMessage();
		}

		// 获取签名
		origin = this.getOrigin(rsaRes, "sign");
		logger.debug("响应签名原文 = {}", origin);

		String sign = RSA.signBase64(privateKeyBase64, origin);
		rsaRes.setSign(sign);

		return rsaRes;
	}

	/**
	 * 获取签名原文
	 * @param obj 数据
	 * @return 签名原文
	 */
	private String getOrigin(Object obj, String... ignores) {
		Set<String> set = new HashSet<String>();
		for (String ignore : ignores) {
			set.add(ignore);
		}

		List<Field> fields = Utils.getFields(obj.getClass());
		List<Field> array = new ArrayList<Field>();

		// 移除忽略
		for (Field field : fields) {
			String name = field.getName();
			if (set.contains(name)) {
				continue;
			}
			array.add(field);
		}

		// 排序
		Collections.sort(array, new Comparator<Field>() {

			@Override
			public int compare(Field o1, Field o2) {
				return o1.getName().compareTo(o2.getName());
			}
		});

		// 获取签名原文
		Class<?> clazz = obj.getClass();
		StringBuilder buffer = new StringBuilder();
		for (Field field : array) {
			String name = field.getName();
			String methodName = "get" + name.substring(0, 1).toUpperCase() + name.substring(1);
			try {
				String value = (String) clazz.getMethod(methodName, new Class[0]).invoke(obj, new Object[0]);
				// 空值不参与签名
				if (value == null || value.trim().length() == 0) {
					continue;
				}
				buffer.append("&").append(name).append("=").append(value);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}

		}

		return buffer.toString().substring(1);
	}

	// RSA权限请求
	public static final class RSAAuthRequest extends AuthRequest {
		/** 签名类型 */
		private String signType;
		/** 签名数据 */
		private String sign;

		public String getSignType() {
			return signType;
		}

		public void setSignType(String signType) {
			this.signType = signType;
		}

		public String getSign() {
			return sign;
		}

		public void setSign(String sign) {
			this.sign = sign;
		}

		@Override
		public String toString() {
			return "RSAAuthRequest [signType=" + signType + ", sign=" + sign + ", gatewayVersion=" + gatewayVersion + ", partnerId=" + partnerId + ", apiMethod=" + apiMethod + ", timestamp="
					+ timestamp + ", bizContent=" + bizContent + "]";
		}

	}

	// RSA权限响应
	public static final class RSAAuthResponse extends AuthResponse {
		/** 签名类型 */
		private String signType;
		/** 签名数据 */
		private String sign;

		public String getSignType() {
			return signType;
		}

		public void setSignType(String signType) {
			this.signType = signType;
		}

		public String getSign() {
			return sign;
		}

		public void setSign(String sign) {
			this.sign = sign;
		}

		@Override
		public String toString() {
			return "RSAAuthResponse [signType=" + signType + ", sign=" + sign + ", gatewayVersion=" + gatewayVersion + ", partnerId=" + partnerId + ", apiMethod=" + apiMethod + ", timestamp="
					+ timestamp + ", code=" + code + ", message=" + message + ", subCode=" + subCode + ", subMessage=" + subMessage + ", result=" + result + "]";
		}

	}
}
