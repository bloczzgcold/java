package com.github.hualuomoli.tool.http;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.github.hualuomoli.tool.util.ClassUtils;

/**
 * 客户端
 */
public abstract class AbstractHttpCleint implements HttpClient {

  // 请求header
  private List<Header> headers = new ArrayList<Header>();

  private Parser parser = new DefaultParser();

  public void setParser(Parser parser) {
    this.parser = parser;
  }

  @Override
  public String get(Object object) throws IOException {
    return this.get(parser.parse(object), headers);
  }

  @Override
  public String urlencoded(Object object) throws IOException {
    return this.urlencoded(parser.parse(object), headers);
  }

  @Override
  public String json(String content) throws IOException {
    return this.json(content, headers);
  }

  @Override
  public String upload(Object object, FileParam[] fileParams) throws IOException {
    return this.upload(parser.parse(object), fileParams, headers);
  }

  @Override
  public String upload(Object object, UploadParam[] uploadParams) throws IOException {
    return this.upload(parser.parse(object), uploadParams, headers);
  }

  /**
  * GET请求
  * @param params 请求参数
  * @param headers 请求header
  * @return 执行结果
  * @throws IOException 执行错误
  */
  protected abstract String get(List<Param> params, List<Header> headers) throws IOException;

  /**
   * urlencoded请求
   * @param params 请求参数
   * @param headers 请求header
   * @return 执行结果
  * @throws IOException 执行错误
   */
  protected abstract String urlencoded(List<Param> params, List<Header> headers) throws IOException;

  /**
   * json请求
   * @param params 请求参数
   * @param headers 请求header
   * @return 执行结果
  * @throws IOException 执行错误
   */
  protected abstract String json(String content, List<Header> headers) throws IOException;

  /**
   * upload请求
   * @param params 普通参数
   * @param fileParams 文件参数
   * @param headers 请求header
   * @return 执行结果
  * @throws IOException 执行错误
   */
  protected abstract String upload(List<Param> params, FileParam[] fileParams, List<Header> headers) throws IOException;

  /**
   * upload请求
   * @param params 普通参数
   * @param uploadParams 文件参数
   * @param headers 请求header
   * @return 执行结果
  * @throws IOException 执行错误
   */
  protected abstract String upload(List<Param> params, UploadParam[] uploadParams, List<Header> headers) throws IOException;

  /**
   * 数据编码
   * @return 编码
   */
  protected abstract String charset();

  @Override
  public void addHeader(String name, String value) {
    for (Header header : headers) {
      if (header.name.equals(value)) {
        String[] values = header.value;
        int len = values.length;
        values = Arrays.copyOf(values, len + 1);
        values[len] = value;
        return;
      }
    }
    headers.add(new Header(name, value));
  }

  @Override
  public void setHeader(String name, String value) {
    for (Header header : headers) {
      if (header.name.equals(value)) {
        header.value = new String[] { value };
        return;
      }
    }
    headers.add(new Header(name, value));
  }

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
    return buffer.toString().substring(1);
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

  // 默认解析器
  private class DefaultParser implements Parser {

    @Override
    public List<Param> parse(Object object) {
      if (object == null) {
        return new ArrayList<Param>();
      }
      return this.parse(null, object);
    }

    @SuppressWarnings("unchecked")
    private List<Param> parse(String prefix, Object object) {
      List<Param> params = new ArrayList<Param>();

      Class<?> clazz = object.getClass();

      // String
      if (String.class.isAssignableFrom(clazz)) {
        params.add(new Param(prefix, (String) object));
        return params;
      }

      // Integer
      if (Integer.class.isAssignableFrom(clazz) || int.class.isAssignableFrom(clazz)) {
        params.add(new Param(prefix, String.valueOf(object)));
        return params;
      }

      // Long
      if (Long.class.isAssignableFrom(clazz) || long.class.isAssignableFrom(clazz)) {
        params.add(new Param(prefix, String.valueOf(object)));
        return params;
      }

      // Double
      if (Double.class.isAssignableFrom(clazz) || double.class.isAssignableFrom(clazz)) {
        params.add(new Param(prefix, String.valueOf(object)));
        return params;
      }

      // Boolean
      if (Boolean.class.isAssignableFrom(clazz) || boolean.class.isAssignableFrom(clazz)) {
        params.add(new Param(prefix, String.valueOf(object)));
        return params;
      }

      // Date 
      if (Date.class.isAssignableFrom(clazz)) {
        params.add(new Param(prefix, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format((Date) object)));
        return params;
      }

      // Map
      if (Map.class.isAssignableFrom(clazz)) {
        Map<String, Object> map = (Map<String, Object>) object;
        for (String key : map.keySet()) {
          params.addAll(this.parse(this.getPrefix(prefix, key), map.get(key)));
        }
        return params;
      }

      // object
      List<Field> fields = ClassUtils.getFields(object.getClass());
      for (Field field : fields) {
        Object value = ClassUtils.getFieldValue(field, object);
        if (value == null) {
          continue;
        }
        params.addAll(this.parse(this.getPrefix(prefix, field.getName()), value));
      }

      return params;

    }

    private String getPrefix(String prefix, String key) {
      if (prefix == null) {
        return key;
      }
      return prefix + "." + key;
    }

  }

}
