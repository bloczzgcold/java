package com.github.hualuomoli.gateway.server.auth;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.validator.constraints.NotEmpty;

import com.github.hualuomoli.gateway.server.business.BusinessHandler;
import com.github.hualuomoli.gateway.server.constants.CodeEnum;
import com.github.hualuomoli.gateway.server.constants.NameEnum;
import com.github.hualuomoli.gateway.server.constants.SignatureTypeEnum;
import com.github.hualuomoli.gateway.server.lang.InvalidSignatureException;
import com.github.hualuomoli.gateway.server.loader.PartnerLoader.Key;
import com.github.hualuomoli.gateway.server.loader.PartnerLoader.Partner;
import com.github.hualuomoli.gateway.server.parser.JSONParser;
import com.github.hualuomoli.tool.RSA;
import com.github.hualuomoli.validate.values.Values;

/**
 * RSA权限认证
 * @author lbq
 *
 */
public class RSAAuthExecution implements AuthExecution {

	@Override
	public boolean support(Partner partner, HttpServletRequest req, HttpServletResponse res) {

		// 签名类型
		String signType = req.getParameter(NameEnum.IN_OUT_SIGNTYPE.value());
		if (!SignatureTypeEnum.RSA.name().equals(signType)) {
			return false;
		}

		// 没有配置RSA
		Map<Key, String> configs = partner.getConfigs();
		if (!configs.containsKey(Key.SIGNATURE_RSA_PUBLIC_KEY) || !configs.containsKey(Key.SIGNATURE_RSA_PRIVATE_KEY)) {
			return false;
		}

		return true;
	}

	@Override
	public RSAAuthResponse deal(Partner partner, JSONParser jsonParser, HttpServletRequest req, HttpServletResponse res, BusinessHandler handler)
			throws Throwable {

		// 获取请求数据
		RSAAuthRequest rsaReq = new RSAAuthRequest();
		rsaReq.partnerId = req.getParameter(NameEnum.IN_OUT_PARTNERID.value());
		rsaReq.method = req.getParameter(NameEnum.IN_METHOD.value());
		rsaReq.timestamp = req.getParameter(NameEnum.IN_OUT_TIMESTAMP.value());
		rsaReq.bizContent = req.getParameter(NameEnum.IN_BIZCONTENT.value());
		rsaReq.signType = req.getParameter(NameEnum.IN_OUT_SIGNTYPE.value());
		rsaReq.signData = req.getParameter(NameEnum.IN_OUT_SIGNDATA.value());

		logger.info("请求业务内容 = {}", rsaReq.bizContent);

		// 验证签名
		String origin = this.getOrigin(rsaReq);

		if (!RSA.verify(partner.getConfigs().get(Key.SIGNATURE_RSA_PUBLIC_KEY), origin, rsaReq.signData)) {
			throw new InvalidSignatureException("不合法的签名");
		}

		// 执行业务操作
		String result = handler.handle(rsaReq.method, rsaReq.bizContent, jsonParser, req, res);

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
		rsaRes.signData = RSA.signBase64(partner.getConfigs().get(Key.SIGNATURE_RSA_PRIVATE_KEY), origin);

		return rsaRes;

	}

	/**
	 * 获取签名原文
	 * @param obj 数据
	 * @return 签名原文
	 */
	private String getOrigin(Object obj) {
		Field[] parentFields = obj.getClass().getSuperclass().getDeclaredFields();
		Field[] fields = obj.getClass().getDeclaredFields();

		List<Field> fieldList = new ArrayList<Field>();
		for (Field field : parentFields) {
			fieldList.add(field);
		}
		for (Field field : fields) {
			fieldList.add(field);
		}

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
			if (NameEnum.IN_OUT_SIGNDATA.value().equals(name)) {
				// 签名数据不参与签名
				continue;
			}
			try {
				buffer.append("&").append(name).append("=").append(field.get(obj));
			} catch (Exception e) {
				// 不会存在
			}
		}

		return buffer.toString().substring(1);
	}

	// RSA权限请求
	private static class RSAAuthRequest extends AuthRequest {
		/** 签名类型 */
		@NotEmpty(message = "签名类型不能为空")
		@Values(value = { "RSA", "MD5" }, message = "签名类型支持RSA,MD5")
		private String signType;
		/** 签名数据 */
		@NotEmpty(message = "签名数据不能为空")
		private String signData;
	}

	// RSA权限响应
	public static class RSAAuthResponse extends AuthResponse {
		/** 签名类型 */
		private String signType;
		/** 签名数据 */
		private String signData;

		public String getSignType() {
			return signType;
		}

		public String getSignData() {
			return signData;
		}
	}

}
