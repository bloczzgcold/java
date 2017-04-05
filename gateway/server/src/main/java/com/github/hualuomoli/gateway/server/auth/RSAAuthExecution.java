package com.github.hualuomoli.gateway.server.auth;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.NotEmpty;

import com.github.hualuomoli.gateway.server.business.BusinessHandler;
import com.github.hualuomoli.gateway.server.constants.CodeEnum;
import com.github.hualuomoli.gateway.server.constants.NameEnum;
import com.github.hualuomoli.gateway.server.constants.SignatureTypeEnum;
import com.github.hualuomoli.gateway.server.lang.InvalidSignatureException;
import com.github.hualuomoli.gateway.server.loader.PartnerLoader.Key;
import com.github.hualuomoli.gateway.server.loader.PartnerLoader.Partner;
import com.github.hualuomoli.gateway.server.parser.JSONParser;
import com.github.hualuomoli.tool.security.RSA;
import com.github.hualuomoli.tool.util.ObjectUtils;
import com.github.hualuomoli.validator.constraints.Values;

/**
 * RSA权限认证
 * @author lbq
 *
 */
public class RSAAuthExecution implements AuthExecution {

	/** RSA私钥 */
	private String privateKeyBase64;

	public RSAAuthExecution(String privateKeyBase64) {
		super();
		this.privateKeyBase64 = privateKeyBase64;
	}

	@Override
	public boolean support(Partner partner, HttpServletRequest req, HttpServletResponse res) {

		// 签名类型
		String signType = req.getParameter(NameEnum.IN_OUT_SIGNTYPE.value());
		if (!SignatureTypeEnum.RSA.name().equals(signType)) {
			return false;
		}

		// 没有配置RSA
		Map<Key, String> configs = partner.getConfigs();
		if (!configs.containsKey(Key.SIGNATURE_RSA_PUBLIC_KEY)) {
			return false;
		}

		return true;
	}

	@Override
	public RSAAuthResponse deal(Partner partner, JSONParser jsonParser, HttpServletRequest req, HttpServletResponse res, BusinessHandler handler, List<AuthExecution.Filter> filters) throws Throwable {

		// 获取请求数据
		RSAAuthRequest rsaReq = new RSAAuthRequest();
		rsaReq.partnerId = req.getParameter(NameEnum.IN_OUT_PARTNERID.value());
		rsaReq.apiMethod = req.getParameter(NameEnum.IN_API_METHOD.value());
		rsaReq.timestamp = req.getParameter(NameEnum.IN_OUT_TIMESTAMP.value());
		rsaReq.bizContent = req.getParameter(NameEnum.IN_BIZCONTENT.value());
		rsaReq.signType = req.getParameter(NameEnum.IN_OUT_SIGNTYPE.value());
		rsaReq.sign = req.getParameter(NameEnum.IN_OUT_SIGN.value());

		logger.info("请求业务内容 = {}", rsaReq.bizContent);

		// 验证签名
		String origin = this.getOrigin(rsaReq);
		logger.debug("请求签名原文 = {}", origin);

		if (!RSA.verify(partner.getConfigs().get(Key.SIGNATURE_RSA_PUBLIC_KEY), origin, rsaReq.sign)) {
			throw new InvalidSignatureException("不合法的签名");
		}

		// 过滤器
		for (Filter filter : filters) {
			filter.preHandler(rsaReq.partnerId, rsaReq.apiMethod);
		}

		// 执行业务操作
		String result = handler.handle(rsaReq.apiMethod, rsaReq.bizContent, jsonParser, req, res);
		logger.info("响应业务内容 = {}", result);

		// 设置返回数据
		RSAAuthResponse rsaRes = new RSAAuthResponse();
		rsaRes.code = CodeEnum.SUCCESS.value();
		rsaRes.message = "处理成功";
		rsaRes.subCode = CodeEnum.SUCCESS.value();
		rsaRes.subMessage = "业务处理成功";
		rsaRes.partnerId = rsaReq.partnerId;
		rsaRes.timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		rsaRes.result = result;
		rsaRes.signType = rsaReq.signType;
		// 获取签名
		origin = this.getOrigin(rsaRes);
		logger.info("响应签名原文 = {}", origin);

		rsaRes.sign = RSA.signBase64(privateKeyBase64, origin);

		return rsaRes;

	}

	/**
	 * 获取签名原文
	 * @param obj 数据
	 * @return 签名原文
	 */
	private String getOrigin(Object obj) {

		List<Field> fieldList = ObjectUtils.getFields(obj.getClass());

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
			if (NameEnum.IN_OUT_SIGN.value().equals(name)) {
				// 签名数据不参与签名
				continue;
			}
			try {
				String value = (String) field.get(obj);
				// 空值不参与签名
				if (StringUtils.isBlank(value)) {
					continue;
				}
				buffer.append("&").append(name).append("=").append(value);
			} catch (Exception e) {
				// 不会存在
			}
		}

		return buffer.toString().substring(1);
	}

	// RSA权限请求
	private static final class RSAAuthRequest extends AuthRequest {
		/** 签名类型 */
		@NotEmpty(message = "签名类型不能为空")
		@Values(value = { "RSA", "MD5" }, message = "签名类型支持RSA,MD5")
		protected String signType;
		/** 签名数据 */
		@NotEmpty(message = "签名数据不能为空")
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
