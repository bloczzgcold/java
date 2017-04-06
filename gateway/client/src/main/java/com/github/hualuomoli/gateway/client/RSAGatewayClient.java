package com.github.hualuomoli.gateway.client;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.hualuomoli.gateway.client.enums.SignType;
import com.github.hualuomoli.gateway.client.http.HttpClient;
import com.github.hualuomoli.gateway.client.json.JSONParser;
import com.github.hualuomoli.gateway.client.lang.DealException;
import com.github.hualuomoli.gateway.client.lang.GatewayException;
import com.github.hualuomoli.gateway.client.security.RSA;
import com.github.hualuomoli.gateway.client.util.Utils;
import com.github.hualuomoli.gateway.client.util.Validate;

/**
 * RSA网关客户端
 * @author lbq
 *
 */
public class RSAGatewayClient extends GatewayClientAdaptor implements GatewayClient {

	private static final Logger logger = LoggerFactory.getLogger(RSAGatewayClient.class);

	/** 服务端URL */
	private String serverURL;
	/** 合作伙伴ID */
	private String partnerId;
	/** 公钥 */
	private String publicKey;
	/** 私钥 */
	private String privateKey;
	/** 数据编码集 */
	private Charset charset;
	/** JSON转换器 */
	private JSONParser jsonParser;
	/** HTTP发起URL请求 */
	private HttpClient httpClient;

	public RSAGatewayClient(String serverURL, String partnerId, String publicKey, String privateKey, Charset charset, JSONParser jsonParser, HttpClient httpClient) {
		super(jsonParser);
		this.serverURL = serverURL;
		this.partnerId = partnerId;
		this.publicKey = publicKey;
		this.privateKey = privateKey;
		this.charset = charset;
		this.jsonParser = jsonParser;
		this.httpClient = httpClient;
	}

	@Override
	public String call(String method, String bizContent) throws DealException, GatewayException {
		try {

			RSARequest req = new RSARequest();
			req.partnerId = this.partnerId;
			req.apiMethod = method;
			req.timestamp = this.getTimeStamp();
			req.bizContent = bizContent;
			req.signType = SignType.RSA.name();

			logger.debug("请求业务内容={}", req.bizContent);

			String origin = this.getASCIISignOrigin(req, "sign");
			logger.debug("请求签名原文={}", origin);

			req.sign = RSA.signBase64(this.privateKey, origin);

			// 执行盗用
			String result = httpClient.urlencoded(this.serverURL, this.charset, req);

			// 返回结果
			RSAResponse res = jsonParser.parseObject(result, RSAResponse.class);

			// 验证签名
			origin = this.getASCIISignOrigin(res, "sign");
			logger.debug("响应签名原文={}", origin);

			Validate.isTrue(RSA.verify(this.publicKey, origin, res.sign), "签名不合法");

			// 网关
			if (!"0000".equals(res.code)) {
				throw new GatewayException(res.code, res.message);
			}

			if (!"0000".equals(res.subCode)) {
				throw new DealException(res.subCode, res.subMessage);
			}

			logger.debug("响应业务数据={}", res.result);

			return res.result;
		} catch (IOException e) {
			throw new GatewayException("404", "调用失败");
		}
	}

	/** RSA请求 */
	class RSARequest {
		/** 合作伙伴ID */
		private String partnerId;
		/** 请求的业务方法 */
		private String apiMethod;
		/** 时间戳 */
		private String timestamp;
		/** 业务内容 */
		private String bizContent;
		/** 签名类型 */
		private String signType;
		/** 签名数据 */
		private String sign;

		public String getPartnerId() {
			return partnerId;
		}

		public String getApiMethod() {
			return apiMethod;
		}

		public String getTimestamp() {
			return timestamp;
		}

		public String getBizContent() {
			return bizContent;
		}

		public String getSignType() {
			return signType;
		}

		public String getSign() {
			return sign;
		}

		@Override
		public String toString() {
			return "RSARequest [partnerId=" + partnerId + ", apiMethod=" + apiMethod + ", timestamp=" + timestamp + ", bizContent=" + bizContent + ", signType=" + signType + ", sign=" + sign + "]";
		}

	}

	/** RSA响应 */
	public static final class RSAResponse {

		/** 调用结果编码 */
		protected String code;
		/** 调用结果名称 */
		protected String message;
		/** 业务处理编码  */
		protected String subCode;
		/** 业务处理名称 */
		protected String subMessage;
		/** 合作伙伴ID */
		protected String partnerId;
		/** 请求的业务方法 */
		protected String apiMethod;
		/** 时间戳 */
		protected String timestamp;
		/** 响应内容 */
		protected String result;
		/** 签名类型 */
		private String signType;
		/** 签名数据 */
		private String sign;

		public String getCode() {
			return code;
		}

		public void setCode(String code) {
			this.code = code;
		}

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}

		public String getSubCode() {
			return subCode;
		}

		public void setSubCode(String subCode) {
			this.subCode = subCode;
		}

		public String getSubMessage() {
			return subMessage;
		}

		public void setSubMessage(String subMessage) {
			this.subMessage = subMessage;
		}

		public String getPartnerId() {
			return partnerId;
		}

		public void setPartnerId(String partnerId) {
			this.partnerId = partnerId;
		}

		public String getApiMethod() {
			return apiMethod;
		}

		public void setApiMethod(String apiMethod) {
			this.apiMethod = apiMethod;
		}

		public String getTimestamp() {
			return timestamp;
		}

		public void setTimestamp(String timestamp) {
			this.timestamp = timestamp;
		}

		public String getResult() {
			return result;
		}

		public void setResult(String result) {
			this.result = result;
		}

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
			return "RSAResponse [code=" + code + ", message=" + message + ", subCode=" + subCode + ", subMessage=" + subMessage + ", partnerId=" + partnerId + ", apiMethod=" + apiMethod
					+ ", timestamp=" + timestamp + ", result=" + result + ", signType=" + signType + ", sign=" + sign + "]";
		}

	}

	/**
	 * 实体类属性按照ASCII方式排序的签名原文
	 * @param object 实体类
	 * @param ignores 忽略的请求参数
	 * @return 按照ASCII方式拼接的签名源数据
	 */
	protected String getASCIISignOrigin(Object object, String... ignores) {
		Set<String> ignoreSets = new HashSet<String>();
		for (String ignore : ignores) {
			ignoreSets.add(ignore);
		}
		return this.getASCIISignOrigin(object, ignoreSets);
	}

	/**
	 * 实体类属性按照ASCII方式排序的签名原文
	 * @param object 实体类
	 * @param ignores 忽略的请求参数
	 * @return 按照ASCII方式拼接的签名源数据
	 */
	protected String getASCIISignOrigin(Object object, Set<String> ignores) {
		Class<?> clazz = object.getClass();
		List<Field> fields = Utils.getFields(clazz);

		List<ASCII> asciiList = new ArrayList<ASCII>();

		for (Field field : fields) {
			String name = field.getName();
			int modify = field.getModifiers();
			if (Modifier.isPublic(modify) && Modifier.isStatic(modify) && Modifier.isFinal(modify)) {
				// public static fianl
				continue;
			}
			if (ignores.contains(name)) {
				// 需要忽略的参数
				continue;
			}
			try {
				String methodName = "get" + name.substring(0, 1).toUpperCase() + name.substring(1);
				String value = (String) clazz.getMethod(methodName).invoke(object);

				// 为空的数据不参与签名 
				if (value == null || value.trim().length() == 0) {
					continue;
				}

				ASCII ascii = new ASCII();
				ascii.name = name;
				ascii.value = value;
				asciiList.add(ascii);
			} catch (Exception e) {
				logger.debug("获取值错误", e);
			}
		}

		// 没有参数
		if (asciiList.size() == 0) {
			return "";
		}

		// ASCII排序
		Collections.sort(asciiList, new Comparator<ASCII>() {

			@Override
			public int compare(ASCII o1, ASCII o2) {
				String name1 = o1.name;
				String name2 = o2.name;
				int len1 = name1.length();
				int len2 = name2.length();
				int len = len1 > len2 ? len2 : len1;
				for (int i = 0; i < len; i++) {
					int differ = name1.charAt(i) - name2.charAt(i);
					if (differ == 0) {
						continue;
					}
					return differ;
				}
				return len1 - len2;
			}
		});

		StringBuilder buffer = new StringBuilder();
		// 拼接参数
		for (int i = 0; i < asciiList.size(); i++) {
			ASCII ascii = asciiList.get(i);
			buffer.append("&").append(ascii.name).append("=").append(ascii.value);
		}

		String origin = buffer.toString().substring(1);

		return origin;

	}

	// ASCII
	private class ASCII {
		private String name;
		private String value;
	}

}
