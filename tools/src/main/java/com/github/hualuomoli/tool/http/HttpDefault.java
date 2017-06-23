package com.github.hualuomoli.tool.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.io.IOUtils;

import com.github.hualuomoli.tool.http.entity.Header;

// default
public class HttpDefault extends AbstractHttpCleint implements HttpClient {

    private String url;

    private HttpDefault(String url) {
        this.url = url;
    }

    public static HttpDefault getInstance(String url) {
        return new HttpDefault(url);
    }

    @Override
    protected String getCharset() {
        return "UTF-8";
    }

    @Override
    protected Formater getFormater() {
        return new HttpClient.Formater() {

            final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            @Override
            public String format(Date date, Field field) {
                return sdf.format(date);
            }
        };
    }

    @Override
    protected String execute(String content, HttpMethod httpMethod, List<Header> addRequestHeaders) throws IOException {

        HttpURLConnection conn = this.getConnection(url);

        OutputStream os = null;
        InputStream is = null;

        try {

            // method
            switch (httpMethod) {
            case POST:
                conn.setRequestMethod("POST");
                break;
            case GET:
                conn.setRequestMethod("GET");
                break;
            }

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
            os.write(content.getBytes());

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
                return IOUtils.toString(is, this.getCharset());
            } else if (conn.getResponseCode() == 404) {
                // 未找到
                throw new IOException("404 not found.");
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

    /**
     * 获取HTTP连接
     * @param urlStr URL地址
     * @return HTTP连接
     * @throws IOException 连接失败
     */
    private HttpURLConnection getConnection(String urlStr) throws IOException {

        HttpURLConnection conn = null;

        try {

            this.initCertification(urlStr);

            URL url = new URL(urlStr);
            conn = (HttpURLConnection) url.openConnection();
            if (conn == null) {
                throw new IOException("can't connect to " + url);
            }

            return conn;
        } catch (Exception e) {
            throw new IOException(e);
        }
        // end
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
