package com.github.hualuomoli.gateway.server.handler;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.hualuomoli.gateway.server.enums.CodeEnum;
import com.github.hualuomoli.gateway.server.enums.SignatureTypeEnum;
import com.github.hualuomoli.gateway.server.lang.InvalidSignatureException;
import com.github.hualuomoli.gateway.server.loader.PartnerLoader.Key;
import com.github.hualuomoli.gateway.server.loader.PartnerLoader.Partner;
import com.github.hualuomoli.gateway.server.parser.JSONParser;
import com.github.hualuomoli.gateway.server.security.RSA;
import com.github.hualuomoli.gateway.server.util.Utils;

/**
 * 网关认证
 * @author lbq
 *
 */
public class RSAAuthHandler implements AuthHandler {

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
	public AuthResponse execute(HttpServletRequest req, HttpServletResponse res, Partner partner, JSONParser jsonParser//
	, BusinessHandler businessHandler, List<BusinessHandler.HandlerInterceptor> interceptors) throws Throwable {

		// 获取请求数据
		RSAAuthRequest rsaReq = new RSAAuthRequest();
		rsaReq.partnerId = req.getParameter("partnerId");
		rsaReq.apiMethod = req.getParameter("apiMethod");
		rsaReq.timestamp = req.getParameter("timestamp");
		rsaReq.bizContent = req.getParameter("bizContent");
		rsaReq.signType = req.getParameter("signType");
		rsaReq.sign = req.getParameter("sign");

		// 验证请求参数
		Utils.notBlank(rsaReq.partnerId, "partnerId is not blank.");
		Utils.notBlank(rsaReq.apiMethod, "apiMethod is not blank.");
		Utils.notBlank(rsaReq.timestamp, "timestamp is not blank.");
		Utils.notBlank(rsaReq.bizContent, "bizContent is not blank.");
		Utils.notBlank(rsaReq.signType, "signType is not blank.");
		Utils.notBlank(rsaReq.sign, "sign is not blank.");

		Utils.isTrue(rsaReq.timestamp.matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}( \\d{0,4})?"), "timestamp is invalid.");

		logger.info("请求业务内容 = {}", rsaReq.bizContent);

		// 验证签名
		String origin = this.getOrigin(rsaReq);
		logger.debug("请求签名原文 = {}", origin);

		if (!RSA.verify(partner.getConfigs().get(Key.SIGNATURE_RSA_PUBLIC_KEY), origin, rsaReq.sign)) {
			throw new InvalidSignatureException("不合法的签名");
		}

		// 执行业务
		String result = businessHandler.handle(req, res, rsaReq.partnerId, rsaReq.apiMethod, rsaReq.bizContent, jsonParser, interceptors);

		logger.info("响应业务内容 = {}", result);
		// 设置返回数据
		RSAAuthResponse rsaRes = new RSAAuthResponse();
		rsaRes.code = CodeEnum.SUCCESS.value();
		rsaRes.message = "调用成功";
		rsaRes.subCode = CodeEnum.SUCCESS.value();
		rsaRes.subMessage = "业务处理成功";

		rsaRes.partnerId = rsaReq.partnerId;
		rsaRes.apiMethod = rsaReq.apiMethod;
		rsaRes.timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss S").format(new Date());
		rsaRes.result = result;
		rsaRes.signType = rsaReq.signType;

		// 获取签名
		origin = this.getOrigin(rsaRes);
		logger.debug("响应签名原文 = {}", origin);

		rsaRes.sign = RSA.signBase64(privateKeyBase64, origin);

		return rsaRes;
	}

	/**
	 * 获取签名原文
	 * @param obj 数据
	 * @return 签名原文
	 */
	private String getOrigin(Object obj) {

		List<Field> fieldList = Utils.getFields(obj.getClass());

		// sort
		Collections.sort(fieldList, new Comparator<Field>() {

			@Override
			public int compare(Field o1, Field o2) {
				String s1 = o1.getName();
				String s2 = o2.getName();

				int len1 = s1.length();
				int len2 = s2.length();

				int len = len1 > len2 ? len2 : len1;
				for (int i = 0; i < len; i++) {
					int c = s1.charAt(i) - s2.charAt(i);
					if (c == 0) {
						continue;
					}
					return c;
				}

				return len1 - len2;
			}
		});

		StringBuilder buffer = new StringBuilder();
		for (Field field : fieldList) {
			String name = field.getName();
			if ("sign".equals(name)) {
				// 签名数据不参与签名
				continue;
			}
			try {
				String value = (String) field.get(obj);
				// 空值不参与签名
				if (value == null || value.trim().length() == 0) {
					continue;
				}
				buffer.append("&").append(name).append("=").append(value);
			} catch (Exception e) {
				// 不会存在
				logger.debug("无法获取属性值", e);
			}
		}

		return buffer.toString().substring(1);
	}

	// RSA权限请求
	private final class RSAAuthRequest extends AuthRequest {
		/** 签名类型 */
		protected String signType;
		/** 签名数据 */
		private String sign;
	}

	// RSA权限响应
	public static final class RSAAuthResponse extends AuthResponse {
		/** 签名类型 */
		protected String signType;
		/** 签名数据 */
		private String sign;

		public String getSignType() {
			return signType;
		}

		public String getSign() {
			return sign;
		}
	}
}
