package com.github.hualuomoli.gateway.client;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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
public class RSAGatewayClient extends AbstractGatewayClient {

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
	/** 签名 */
	private RSA rsa;

	public RSAGatewayClient(String serverURL, String partnerId, String publicKey, String privateKey, Charset charset //
	, JSONParser jsonParser, GatewayClient.ObejctParser obejctParser, HttpClient httpClient, RSA rsa) {
		super(jsonParser, obejctParser);
		this.serverURL = serverURL;
		this.partnerId = partnerId;
		this.publicKey = publicKey;
		this.privateKey = privateKey;
		this.charset = charset;
		this.jsonParser = jsonParser;
		this.httpClient = httpClient;
		this.rsa = rsa;
	}

	@Override
	public String call(String method, String bizContent) throws DealException, GatewayException {

		try {
			// 1、请求
			logger.info("请求业务内容={}", bizContent);

			// 1.1、请求数据
			RSARequest req = new RSARequest();
			req.partnerId = this.partnerId;
			req.apiMethod = method;
			req.timestamp = this.getTimeStamp();
			req.bizContent = bizContent;
			req.signType = SignType.RSA.name();

			// 1.2、获取请求参数签名原文
			String requestOrigin = this.getOrigin(req, "sign");
			logger.debug("请求签名原文={}", requestOrigin);

			// 1.3、获取签名
			req.sign = rsa.sign(this.privateKey, requestOrigin);

			// 1.4、实际向服务器发发送的参数
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("partnerId", req.partnerId);
			paramMap.put("apiMethod", req.apiMethod);
			paramMap.put("timestamp", req.timestamp);
			paramMap.put("bizContent", req.bizContent);
			paramMap.put("signType", req.signType);
			paramMap.put("sign", req.sign);

			// 2、执行调用

			// 2.2、执行http调用
			logger.debug("向服务器发送的数据request={}", req);
			String result = httpClient.urlencoded(this.serverURL, this.charset, paramMap);
			logger.debug("服务器返回的数据response={}", result);

			// 3、响应

			// 3.1、获取响应内容
			RSAResponse res = jsonParser.parseObject(result, RSAResponse.class);

			String responseOrigin = this.getOrigin(res, "sign");
			logger.debug("响应签名原文={}", responseOrigin);

			// 3.2、验证网关
			if (!"0000".equals(res.code)) {
				throw new GatewayException(res.code, res.message);
			}

			// 3.4、验证签名
			Validate.isTrue(rsa.verify(this.publicKey, responseOrigin, res.sign), "签名不合法");

			// 3.5、验证业务处理是否正常
			if (!"0000".equals(res.subCode)) {
				throw new DealException(res.subCode, res.subMessage);
			}

			logger.info("响应业务数据={}", res.result);

			return res.result;
		} catch (IOException e) {
			throw new GatewayException("404", "调用失败", e);
		}
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

	/** RSA请求 */
	public static final class RSARequest {

		/** 网关版本号 */
		private static final String gatewayVersion = GATEWAY_VERSION;

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

		public String getGatewayVersion() {
			return gatewayVersion;
		}

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

		/** 网关版本号 */
		private static final String gatewayVersion = GATEWAY_VERSION;

		/** 合作伙伴ID */
		private String partnerId;
		/** 请求的业务方法 */
		private String apiMethod;
		/** 时间戳 */
		private String timestamp;

		/** 调用结果编码 */
		private String code;
		/** 调用结果名称 */
		private String message;
		/** 业务处理编码  */
		private String subCode;
		/** 业务处理名称 */
		private String subMessage;

		/** 响应内容 */
		private String result;

		/** 签名类型 */
		private String signType;
		/** 签名数据 */
		private String sign;

		public String getGatewayVersion() {
			return gatewayVersion;
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

	}

}
