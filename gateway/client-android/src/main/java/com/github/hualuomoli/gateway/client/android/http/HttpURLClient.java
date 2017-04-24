package com.github.hualuomoli.gateway.client.android.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.hualuomoli.gateway.client.http.AbstractHttpCleint;
import com.github.hualuomoli.gateway.client.http.HttpCurrent;
import com.github.hualuomoli.gateway.client.util.Utils.DateFormat;
import com.github.hualuomoli.gateway.client.util.Validate;

/**
 * java.net.HttpURLConnection
 * @author lbq
 *
 */
public class HttpURLClient extends AbstractHttpCleint {

	private static final Logger logger = LoggerFactory.getLogger(HttpURLClient.class);

	public HttpURLClient(DateFormat dateFormat) {
		super(dateFormat);
	}

	@Override
	protected String execute(String urlStr, String content, Charset charset, Method method, List<Header> addRequestHeaders) throws IOException {
		Validate.notBlank(urlStr, "urlStr is blank.");
		Validate.notNull(charset, "charset is null.");
		Validate.notNull(method, "method is null.");

		logger.debug("发送数据={}", content);

		byte[] result = this.execute(urlStr, content.getBytes(), method.name(), addRequestHeaders);

		String res = new String(result, charset);

		logger.debug("返回数据={}", res);

		return res;
	}

	/**
	 * 执行
	 * @param urlStr 请求URL
	 * @param content 请求内容
	 * @param method 请求方法
	 * @param addRequestHeaders 增加的请求header信息
	 * @return 执行结果
	 * @throws IOException 处理异常
	 */
	private byte[] execute(String urlStr, byte[] content, String method, List<Header> addRequestHeaders) throws IOException {

		logger.info("请求的URL={}", urlStr);

		HttpURLConnection conn = null;
		OutputStream os = null;
		InputStream is = null;

		try {

			this.initCertification(urlStr);

			URL url = new URL(urlStr);
			conn = (HttpURLConnection) url.openConnection();

			// method
			conn.setRequestMethod(method);

			conn.setDoInput(true); // input
			conn.setDoOutput(true); // output

			// header
			List<Header> reqHeaders = HttpCurrent.getReqHeaders();
			reqHeaders.addAll(addRequestHeaders);

			for (Header header : reqHeaders) {
				String headerName = header.getName();
				String[] headerValues = header.getValue();

				if (headerName == null || headerValues == null || headerValues.length == 0) {
					continue;
				}
				for (String headerValue : headerValues) {
					conn.addRequestProperty(headerName, headerValue);
				}
			}

			// output data
			os = conn.getOutputStream();
			os.write(content);

			// get response header
			Map<String, List<String>> map = conn.getHeaderFields();
			List<Header> resHeaders = new ArrayList<Header>();
			for (String headerName : map.keySet()) {
				List<String> values = map.get(headerName);
				if (values == null || values.size() == 0) {
					continue;
				}
				resHeaders.add(new Header(headerName, values.toArray(new String[] {})));
			}

			// get response
			if (conn.getResponseCode() == 200) {
				// 成功
				is = conn.getInputStream();
				return IOUtils.toByteArray(is);
			} else if (conn.getResponseCode() == 404) {
				// 未找到
				throw new IOException("404");
			} else if (conn.getResponseCode() == 500) {
				// 失败
				is = conn.getErrorStream();
				throw new IOException(IOUtils.toString(is));
			} else {
				// 其他错误
				throw new IOException(conn.getResponseCode() + "");
			}

		} catch (Exception e) {
			throw new IOException(e);
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (os != null) {
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (conn != null) {
				conn.disconnect();
			}
		}
	}

	/**
	 * 初始化证书
	 * @param url 请求的URL
	 */
	private void initCertification(String url) {
		if (url == null) {
			return;
		}
		if (!url.startsWith("https://")) {
			return;
		}
		try {
			HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
				@Override
				public boolean verify(String hostname, SSLSession session) {
					return true;
				}
			});
			SSLContext context = SSLContext.getInstance("TLS");
			context.init(null, new X509TrustManager[] { new X509TrustManager() {
				@Override
				public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
				}

				@Override
				public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
				}

				@Override
				public X509Certificate[] getAcceptedIssuers() {
					return new X509Certificate[0];
				}
			} }, new SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
