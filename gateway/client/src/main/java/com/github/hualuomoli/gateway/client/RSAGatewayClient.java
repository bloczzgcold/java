package com.github.hualuomoli.gateway.client;

import java.io.IOException;
import java.nio.charset.Charset;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import com.github.hualuomoli.gateway.client.enums.SignType;
import com.github.hualuomoli.gateway.client.json.JSON;
import com.github.hualuomoli.gateway.client.json.JSONAdaptor;
import com.github.hualuomoli.gateway.client.lang.DealException;
import com.github.hualuomoli.gateway.client.lang.GatewayException;
import com.github.hualuomoli.tool.http.HttpClient;
import com.github.hualuomoli.tool.http.HttpURLClient;
import com.github.hualuomoli.tool.security.RSA;

/**
 * RSA网关客户端
 * @author lbq
 *
 */
public class RSAGatewayClient extends GatewayClientAdaptor implements GatewayClient {

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
	private JSON json;
	/** HTTP发起URL请求 */
	private HttpClient httpClient;

	public RSAGatewayClient(String serverURL, String partnerId, String publicKey, String privateKey) {
		this(serverURL, partnerId, publicKey, privateKey, CHARSET, new JSONAdaptor(), new HttpURLClient());
	}

	public RSAGatewayClient(String serverURL, String partnerId, String publicKey, String privateKey, Charset charset, JSON json, HttpClient httpClient) {
		super(json);
		this.serverURL = serverURL;
		this.partnerId = partnerId;
		this.publicKey = publicKey;
		this.privateKey = privateKey;
		this.charset = charset;
		this.json = json;
		this.httpClient = httpClient;
	}

	@Override
	public String call(String method, String bizContent) throws DealException, GatewayException {
		try {
			String result = this.execute(method, bizContent);
			RSAResponse res = json.parseObject(result, RSAResponse.class);

			// 验证签名
			String origin = this.getASCIISignOrigin(res, "sign");
			Validate.isTrue(RSA.verify(this.publicKey, origin, res.sign));

			// 网关
			if (!StringUtils.equals(res.code, "0000")) {
				throw new GatewayException(res.code, res.message);
			}

			if (!StringUtils.equals(res.subCode, "0000")) {
				throw new DealException(res.subCode, res.subMessage);
			}

			return res.result;
		} catch (IOException e) {
			throw new GatewayException("404", "调用失败");
		}
	}

	/**
	 * 调用
	 * @param method 调用的方法
	 * @param bizContent 请求的业务内容
	 * @return 响应
	 * @throws IOException 错误 
	 */
	private String execute(String method, String bizContent) throws IOException {
		Validate.notBlank(method, "method is blank.");

		RSARequest req = new RSARequest();
		req.partnerId = this.partnerId;
		req.apiMethod = method;
		req.timestamp = this.getTimeStamp();
		req.bizContent = bizContent;
		req.signType = SignType.RSA.name();

		String origin = this.getASCIISignOrigin(req, "sign");
		req.sign = RSA.signBase64(this.privateKey, origin);

		// 执行盗用
		return httpClient.urlencoded(this.serverURL, this.charset, this.parseParam(req));
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

		public void setPartnerId(String partnerId) {
			this.partnerId = partnerId;
		}

		public void setApiMethod(String apiMethod) {
			this.apiMethod = apiMethod;
		}

		public void setTimestamp(String timestamp) {
			this.timestamp = timestamp;
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

		@Override
		public String toString() {
			return "RSAResponse [code=" + code + ", message=" + message + ", subCode=" + subCode + ", subMessage=" + subMessage + ", partnerId=" + partnerId + ", apiMethod=" + apiMethod
					+ ", timestamp=" + timestamp + ", result=" + result + ", signType=" + signType + ", sign=" + sign + "]";
		}

	}

}
