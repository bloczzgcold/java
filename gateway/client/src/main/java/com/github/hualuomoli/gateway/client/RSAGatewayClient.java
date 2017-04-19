package com.github.hualuomoli.gateway.client;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.hualuomoli.gateway.client.enums.SignType;
import com.github.hualuomoli.gateway.client.http.HttpClient;
import com.github.hualuomoli.gateway.client.json.JSONParser;
import com.github.hualuomoli.gateway.client.lang.DealException;
import com.github.hualuomoli.gateway.client.lang.GatewayException;
import com.github.hualuomoli.gateway.client.security.RSA;
import com.github.hualuomoli.gateway.client.util.Validate;

/**
 * RSA网关客户端
 * @author lbq
 *
 */
public class RSAGatewayClient extends GatewayClientAdaptor {

	private static final Logger logger = LoggerFactory.getLogger(RSAGatewayClient.class);

	// header信息
	private Map<String, String> headers = new HashMap<String, String>();

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
			StringBuilder requestBuffer = new StringBuilder();
			requestBuffer.append("&apiMethod=").append(req.apiMethod);
			requestBuffer.append("&bizContent=").append(req.bizContent);
			requestBuffer.append("&gatewayVersion=").append(req.gatewayVersion);
			requestBuffer.append("&partnerId=").append(req.partnerId);
			requestBuffer.append("&signType=").append(req.signType);
			requestBuffer.append("&timestamp=").append(req.timestamp);

			String requestOrigin = requestBuffer.toString().substring(1);
			logger.debug("请求签名原文={}", requestOrigin);

			// 1.3、获取签名
			req.sign = rsa.sign(this.privateKey, requestOrigin);

			// 1.4、世纪向服务器发发送的参数
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("partnerId", req.partnerId);
			paramMap.put("apiMethod", req.apiMethod);
			paramMap.put("timestamp", req.timestamp);
			paramMap.put("bizContent", req.bizContent);
			paramMap.put("signType", req.signType);
			paramMap.put("sign", req.sign);

			// 2、执行盗用

			List<HttpClient.Header> requestHeaders = new ArrayList<HttpClient.Header>();
			List<HttpClient.Header> responseHeaders = new ArrayList<HttpClient.Header>();

			// 2.1、header信息
			for (String headerName : headers.keySet()) {
				String headerValue = headers.get(headerName);
				requestHeaders.add(new HttpClient.Header(headerName, headerValue));
			}

			// 2.2、执行http调用
			logger.debug("向服务器发送的数据request={}", req);
			String result = httpClient.urlencoded(this.serverURL, this.charset, paramMap, requestHeaders, responseHeaders);
			logger.debug("服务器返回的数据response={}", result);

			// 2.3、设置响应header
			for (HttpClient.Header header : responseHeaders) {
				headers.put(header.getName(), header.getValue());
			}

			// 3、响应

			// 3.1、获取响应内容
			RSAResponse res = jsonParser.parseObject(result, RSAResponse.class);

			// 3.2、验证网关
			if (!"0000".equals(res.code)) {
				throw new GatewayException(res.code, res.message);
			}

			// 3.3、获取响应参数签名原文
			StringBuilder responseBuffer = new StringBuilder();
			responseBuffer.append("&apiMethod=").append(res.apiMethod);
			responseBuffer.append("&code=").append(res.code);
			responseBuffer.append("&gatewayVersion=").append(res.gatewayVersion);
			responseBuffer.append("&message=").append(res.message);
			responseBuffer.append("&partnerId=").append(res.partnerId);
			responseBuffer.append("&result=").append(res.result);
			responseBuffer.append("&signType=").append(res.signType);
			responseBuffer.append("&subCode=").append(res.subCode);
			responseBuffer.append("&subMessage=").append(res.subMessage);
			responseBuffer.append("&timestamp=").append(res.timestamp);

			String responseOrigin = responseBuffer.toString().substring(1);
			logger.debug("响应签名原文={}", responseOrigin);

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
	 * 添加header
	 * @param name
	 * @param value
	 */
	@Override
	public void addHeader(String name, String value) {
		headers.put(name, value);
	}

	/**
	 * 获取header
	 * @return header
	 */
	@Override
	public Map<String, String> getHeaders() {

		Map<String, String> headerMap = new HashMap<String, String>();

		for (String headerName : headers.keySet()) {
			String headerValue = headers.get(headerName);

			if (headerName == null || headerValue == null) {
				continue;
			}

			headerMap.put(headerName, headerValue);
		}

		return headerMap;
	}

	/** RSA请求 */
	public static final class RSARequest {

		/** 网关版本号 */
		private final String gatewayVersion = GATEWAY_VERSION;
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

		@Override
		public String toString() {
			return "RSARequest [partnerId=" + partnerId + ", apiMethod=" + apiMethod + ", timestamp=" + timestamp + ", bizContent=" + bizContent + ", signType=" + signType + ", sign=" + sign + "]";
		}

	}

	/** RSA响应 */
	public static final class RSAResponse {

		/** 网关版本号 */
		private final String gatewayVersion = GATEWAY_VERSION;
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

		public void setPartnerId(String partnerId) {
			this.partnerId = partnerId;
		}

		public void setApiMethod(String apiMethod) {
			this.apiMethod = apiMethod;
		}

		public void setTimestamp(String timestamp) {
			this.timestamp = timestamp;
		}

		public void setCode(String code) {
			this.code = code;
		}

		public void setMessage(String message) {
			this.message = message;
		}

		public void setSubCode(String subCode) {
			this.subCode = subCode;
		}

		public void setSubMessage(String subMessage) {
			this.subMessage = subMessage;
		}

		public void setResult(String result) {
			this.result = result;
		}

		public void setSignType(String signType) {
			this.signType = signType;
		}

		public void setSign(String sign) {
			this.sign = sign;
		}

	}

}
