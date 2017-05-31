package com.github.hualuomoli.gateway.client.java.http;

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

	private String[] urls;

	public HttpURLClient(String[] urls) {
		super();
		this.urls = urls;
	}

	public HttpURLClient(String[] urls, DateFormat dateFormat) {
		super(dateFormat);
		this.urls = urls;
	}

	@Override
	protected String execute(String content, Charset charset, Method method, List<Header> addRequestHeaders) throws IOException {
		Validate.notNull(charset, "charset is null.");
		Validate.notNull(method, "method is null.");

		logger.debug("发送数据={}", content);

		byte[] result = this.execute(content.getBytes(), method.name(), addRequestHeaders);

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
	private byte[] execute(byte[] content, String method, List<Header> addRequestHeaders) throws IOException {

		List<String> urls = this.getUrls();

		for (String url : urls) {
			HttpURLConnection conn = this.getConnection(url);

			// 无法获取连接
			if (conn == null) {
				this.urlReset(url);
				continue;
			}

			// 执行
			try {
				return this.execute(conn, content, method, addRequestHeaders);
			} catch (NotFoundException e) {
				this.urlReset(url);
			}
			// end
		}
		// 无法连接
		throw new IOException("404");
	}

	/**
	 * 获取URL集合
	 * @return URL集合
	 */
	private List<String> getUrls() {
		List<String> list = new ArrayList<String>();
		for (int i = 0, len = urls.length; i < len; i++) {
			list.add(urls[i]);
		}
		return list;
	}

	/**
	 * 重设URL.当前URL无法连接,排序到最后
	 * @param url 无法连接的URL
	 */
	private void urlReset(String url) {

		// copy
		String[] bakUrls = new String[urls.length];
		for (int i = 0, len = urls.length; i < len; i++) {
			bakUrls[i] = urls[i];
		}

		// 重新排序
		String[] array = new String[bakUrls.length];
		boolean found = false;
		for (int i = 0, len = bakUrls.length, j = 0; i < len; i++) {
			// 找到
			if (!found && url.equals(bakUrls[i])) {
				found = true;
				continue;
			}
			array[j] = bakUrls[i];
			j++;
		}
		array[array.length - 1] = url;

		// reset
		this.urls = array;
	}

	/**
	 * 获取HTTP连接
	 * @param urlStr URL地址
	 * @return HTTP连接
	 */
	private HttpURLConnection getConnection(String urlStr) {

		HttpURLConnection conn = null;

		try {

			this.initCertification(urlStr);

			URL url = new URL(urlStr);
			conn = (HttpURLConnection) url.openConnection();
			if (conn == null) {
				logger.debug("can not connect to {}", urlStr);
				return null;
			}

			return conn;
		} catch (Exception e) {
			logger.warn("can not connect to {}", urlStr, e);
		}
		return null;
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
	private byte[] execute(HttpURLConnection conn, byte[] content, String method, List<Header> addRequestHeaders) throws IOException {

		OutputStream os = null;
		InputStream is = null;

		try {

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
			HttpCurrent.setResHeaders(resHeaders); // add to current

			// get response
			if (conn.getResponseCode() == 200) {
				// 成功
				is = conn.getInputStream();
				return IOUtils.toByteArray(is);
			} else if (conn.getResponseCode() == 404) {
				// 未找到
				throw new NotFoundException();
			} else if (conn.getResponseCode() == 500) {
				// 失败
				is = conn.getErrorStream();
				throw new IOException(IOUtils.toString(is));
			} else {
				// 其他错误
				throw new IOException(conn.getResponseCode() + "");
			}
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
		}
	}

	// 404
	private class NotFoundException extends RuntimeException {

		private static final long serialVersionUID = 8351438051204079988L;

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
