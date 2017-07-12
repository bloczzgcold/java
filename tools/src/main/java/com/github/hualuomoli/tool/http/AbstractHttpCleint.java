package com.github.hualuomoli.tool.http;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 客户端
 */
public abstract class AbstractHttpCleint implements HttpClient {

  protected static final Logger logger = LoggerFactory.getLogger(AbstractHttpCleint.class);

  @Override
  public String get(List<Param> params) throws IOException {
    return this.get(params, null, null);
  }

  @Override
  public String urlencoded(List<Param> params) throws IOException {
    return this.urlencoded(params, null, null);
  }

  @Override
  public String json(String content) throws IOException {
    return this.json(content, null, null);
  }

  /**
   * 数据编码
   * @return 编码
   */
  protected abstract String charset();

  /**
   * 获取编码后的信息
   * @param object 数据
   * @return 编码后的数据
   */
  protected String getEncoded(List<Param> params) {
    if (params == null || params.size() == 0) {
      return null;
    }
    StringBuilder buffer = new StringBuilder();
    for (Param param : params) {
      buffer.append("&").append(param.name).append("=").append(this.encoded(param.value));
    }
    String data = buffer.toString().substring(1);

    logger.info("data={}", data);

    return data;
  }

  /**
   * 数据编码
   * @param value 数据
   * @return 编码后的数据
   */
  protected String encoded(String value) {
    try {
      return URLEncoder.encode(value, this.charset());
    } catch (UnsupportedEncodingException e) {
      throw new RuntimeException(e);
    }
  }

}
