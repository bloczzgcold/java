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
import com.github.hualuomoli.gateway.server.lang.InvalidSignatureException;
import com.github.hualuomoli.gateway.server.loader.PartnerLoader.Key;
import com.github.hualuomoli.gateway.server.loader.PartnerLoader.Partner;
import com.github.hualuomoli.gateway.server.parser.JSONParser;
import com.github.hualuomoli.gateway.server.security.RSA;

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
		this.notBlank(rsaReq.partnerId, "partnerId is not blank.");
		this.notBlank(rsaReq.apiMethod, "apiMethod is not blank.");
		this.notBlank(rsaReq.timestamp, "timestamp is not blank.");
		this.notBlank(rsaReq.bizContent, "bizContent is not blank.");
		this.notBlank(rsaReq.signType, "signType is not blank.");
		this.notBlank(rsaReq.sign, "sign is not blank.");

		this.isTrue(rsaReq.timestamp.matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}( \\d{0,4})?"), "timestamp is invalid.");

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

		List<Field> fieldList = this.getFields(obj.getClass());

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

	/**
	 * 获取类的所有属性及所有父属性
	 * @param clazz 类
	 * @param names 已经存在的属性
	 * @return 类的属性及所有父属性
	 */
	public List<Field> getFields(Class<?> clazz) {
		List<Field> fields = this.getFields(clazz, new HashSet<String>());

		if (logger.isDebugEnabled()) {
			for (Field field : fields) {
				logger.debug("field name {}", field.getName());
			}
		}

		return fields;
	}

	/**
	 * 获取类的所有属性及所有父属性
	 * @param clazz 类
	 * @param names 已经存在的属性
	 * @return 类的属性及所有父属性
	 */
	private List<Field> getFields(Class<?> clazz, Set<String> names) {
		List<Field> fieldList = new ArrayList<Field>();
		if (clazz == null) {
			return fieldList;
		}
		if (names == null) {
			names = new HashSet<String>();
		}
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) {
			if (names.contains(field.getName())) {
				continue;
			}
			if (field.getName().startsWith("this$")) {
				continue;
			}
			fieldList.add(field);
			names.add(field.getName());
		}
		fieldList.addAll(this.getFields(clazz.getSuperclass(), names));
		return fieldList;
	}

	/**
	 * 是否为空
	 * @param value 值
	 * @return 提示信息
	 */
	private void notBlank(String value, String message) {
		if (value == null) {
			throw new NullPointerException(message);
		}
		if (value.trim().length() == 0) {
			throw new IllegalArgumentException(message);
		}
	}

	/**
	 * 是否为真
	 * @param exp 验证表达式
	 * @param message 提示信息
	 */
	private void isTrue(boolean exp, String message) {
		if (!exp) {
			throw new IllegalArgumentException(message);
		}
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
