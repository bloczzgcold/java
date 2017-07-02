package com.github.hualuomoli.tool.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.io.IOUtils;

/**
 * 默认客户端
 */
public class HttpDefault extends AbstractHttpCleint implements HttpClient {

  private static final ThreadLocal<List<Header>> LOCAL_HEADERS = new ThreadLocal<>();

  // 链接时长
  private Integer connectTimeout = 1000 * 2;
  // 从主机读取数据超时
  private Integer readTimeout = 1000 * 30;

  private String url;

  public HttpDefault(String url) {
    this.url = url;
  }

  public void setConnectTimeout(Integer connectTimeout) {
    this.connectTimeout = connectTimeout;
  }

  public void setReadTimeout(Integer readTimeout) {
    this.readTimeout = readTimeout;
  }

  @Override
  protected String get(List<Param> params, List<Header> headers) throws IOException {

    HttpURLConnection conn = null;

    try {
      // 获取URL地址
      String url = this.url;
      if (params != null && params.size() > 0) {
        if (url.lastIndexOf("?") > 0) {
          url = url + "&" + this.getEncoded(params);
        } else {
          url = url + "?" + this.getEncoded(params);
        }
      }

      conn = this.getConnection(url);
      conn.setConnectTimeout(connectTimeout); // 连接超时时间
      conn.setReadTimeout(readTimeout); // 从主机读取数据超时时间
      conn.setDoInput(true); // 设置是否读入
      conn.setDoOutput(true); // 设置是否输出
      conn.setUseCaches(false);// 不使用缓存

      // set request header
      this.writeHttpRequestHeader(conn, headers);

      // get response header
      this.readHttpResponseHeader(conn);

      // read data
      return this.readHttpResponseData(conn);
    } finally {
      this.close(conn);
    }
    // end
  }

  @Override
  protected String urlencoded(List<Param> params, List<Header> headers) throws IOException {

    HttpURLConnection conn = null;
    OutputStream os = null;

    try {
      conn = this.getConnection(url);
      conn.setConnectTimeout(connectTimeout); // 连接超时时间
      conn.setReadTimeout(readTimeout); // 从主机读取数据超时时间
      conn.setDoInput(true); // 设置是否读入
      conn.setDoOutput(true); // 设置是否输出
      conn.setUseCaches(false);// 不使用缓存

      // set request header
      this.writeHttpRequestHeader(conn, headers);

      // flush data
      if (params != null && params.size() > 0) {
        os = conn.getOutputStream();
        os.write(this.getEncoded(params).getBytes());
      }

      // get response header
      this.readHttpResponseHeader(conn);

      // read data
      return this.readHttpResponseData(conn);
    } finally {
      this.close(os);
      this.close(conn);
    }
    // end
  }

  @Override
  protected String json(String content, List<Header> headers) throws IOException {

    HttpURLConnection conn = null;
    OutputStream os = null;

    try {
      conn = this.getConnection(url);
      conn.setConnectTimeout(connectTimeout); // 连接超时时间
      conn.setReadTimeout(readTimeout); // 从主机读取数据超时时间
      conn.setDoInput(true); // 设置是否读入
      conn.setDoOutput(true); // 设置是否输出
      conn.setUseCaches(false);// 不使用缓存

      // set request header
      this.writeHttpRequestHeader(conn, headers);

      // flush data
      if (content != null && content.length() > 0) {
        os = conn.getOutputStream();
        os.write(content.getBytes());
      }

      // get response header
      this.readHttpResponseHeader(conn);

      // read data
      return this.readHttpResponseData(conn);
    } finally {
      this.close(os);
      this.close(conn);
    }
    // end
  }

  /**
   * 写入请求header
   * @param conn 连接
   * @param headers 请求header
   */
  private void writeHttpRequestHeader(HttpURLConnection conn, List<Header> headers) {
    if (headers == null || headers.size() == 0) {
      return;
    }
    for (Header header : headers) {
      String[] values = header.value;
      for (String value : values) {
        conn.addRequestProperty(header.name, value);
      }
    }
  }

  /**
   * 读取响应Header
   * @param conn 连接
   */
  private void readHttpResponseHeader(HttpURLConnection conn) {
    List<Header> headers = new ArrayList<Header>();
    Map<String, List<String>> resHeaderMap = conn.getHeaderFields();
    for (String name : resHeaderMap.keySet()) {
      headers.add(new Header(name, resHeaderMap.get(name).toArray(new String[] {})));
    }
    LOCAL_HEADERS.set(headers);
  }

  /**
   * 读取响应数据
   * @param conn 连接
   * @return 响应数据
   * @throws IOException 读取异常
   */
  private String readHttpResponseData(HttpURLConnection conn) throws IOException {
    InputStream is = null;
    try {
      // get response
      if (conn.getResponseCode() == 200) {
        // 成功
        is = conn.getInputStream();
        return IOUtils.toString(is, this.charset());
      } else if (conn.getResponseCode() == 404) {
        // 未找到
        throw new IOException("404");
      } else if (conn.getResponseCode() == 500) {
        // 失败
        is = conn.getErrorStream();
        throw new IOException(IOUtils.toString(is, this.charset()));
      } else {
        // 其他错误
        throw new IOException(conn.getResponseCode() + "");
      }
    } finally {
      this.close(is);
    }
  }

  @Override
  protected String upload(List<Param> params, FileParam[] fileParams, List<Header> headers) throws IOException {
    throw new IOException();
  }

  @Override
  protected String upload(List<Param> params, UploadParam[] uploadParams, List<Header> headers) throws IOException {
    throw new IOException();
  }

  @Override
  protected String charset() {
    return "UTF-8";
  }

  @Override
  public Set<String> getHeaderNames() {
    Set<String> names = new HashSet<String>();

    List<Header> headers = LOCAL_HEADERS.get();
    for (Header header : headers) {
      names.add(header.name);
    }
    return names;
  }

  @Override
  public Header getHeader(String name) {
    List<Header> headers = LOCAL_HEADERS.get();
    if (headers == null || headers.size() == 0) {
      return null;
    }

    for (Header header : headers) {
      if (header.name.equals(name)) {
        return header;
      }
    }

    return null;
  }

  /**
   * 获取HTTP链接,可考虑使用连接池
   * @param urlString 链接URL
   * @return HTTP链接
   * @throws IOException 链接异常
   */
  private HttpURLConnection getConnection(String urlString) throws IOException {
    // 信任所有的https
    this.initCertification(urlString);

    URL url = new URL(urlString);
    return (HttpURLConnection) url.openConnection();
  }

  /**
   * 关闭HTTP链接
   * @param conn HTTP链接
   */
  private void close(HttpURLConnection conn) {
    if (conn == null) {
      return;
    }
    conn.disconnect();
  }

  /**
  * 关闭输入流
  * @param is 输入流
  */
  private void close(InputStream is) {
    if (is == null) {
      return;
    }
    try {
      is.close();
    } catch (IOException e) {
    }
  }

  /**
  * 关闭输出流
  * @param os 输出流
  */
  private void close(OutputStream os) {
    if (os == null) {
      return;
    }
    try {
      os.close();
    } catch (IOException e) {
    }
  }

  /**
   * 初始化证书
   * @param url 请求的URL
   */
  private void initCertification(String url) {
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
