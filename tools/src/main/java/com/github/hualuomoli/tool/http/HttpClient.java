package com.github.hualuomoli.tool.http;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * HTTP client
 */
public interface HttpClient {

  /**
  * GET
  * @param object 请求信息
  * @return 响应结果
  * @throws IOException 调用过程中异常
  */
  String get(Object object) throws IOException;

  /**
   * application/x-www-form-urlencoded
   * @param object 请求信息
   * @return 响应结果
   * @throws IOException 调用过程中异常
   */
  String urlencoded(Object object) throws IOException;

  /**
   * application/json
   * @param object 请求信息
   * @return 响应结果
   * @throws IOException 调用过程中异常
   */
  String json(String content) throws IOException;

  /**
   * multipart/form-data
   * @param object 请求信息
   * @param fileParams 文件信息
   * @return 响应结果
   * @throws IOException 调用过程中异常
   */
  String upload(Object object, FileParam[] fileParams) throws IOException;

  /**
  * multipart/form-data
  * @param object 请求信息
  * @param uploadParams 上传资源信息
  * @return 响应结果
  * @throws IOException 调用过程中异常
  */
  String upload(Object object, UploadParam[] uploadParams) throws IOException;

  /**
   * 添加请求header
   * @param name 名称
   * @param value 值
   */
  void addHeader(String name, String value);

  /**
   * 设置请求header
   * @param name 名称
   * @param value 值
   */
  void setHeader(String name, String value);

  /**
   * 获取响应的header名
   * @return 响应header名
   */
  Set<String> getHeaderNames();

  /**
   * 获取响应header
   * @param name 名称
   * @return header
   */
  Header getHeader(String name);

  // 格式化
  interface Formater {

    /**
     * 格式化日期
     * @param date 日期
     * @param field 实体类属性
     * @param handler 实体类属性
     * @return 日期字符串
     */
    String format(Date date, Field field, Object handler);

  }

  interface Parser {

    /**
     * 解析参数为表单提交参数
     * @param object
     * @return 参数集合
     */
    List<Param> parse(Object object);

  }

  class Header {

    /** 名称 */
    String name;
    /** 值 */
    String[] value;

    public Header() {
    }

    public Header(String name, String value) {
      this.name = name;
      this.value = new String[] { value };
    }

    public Header(String name, String[] value) {
      this.name = name;
      this.value = value;
    }

    @Override
    public String toString() {
      return "Header [name=" + name + ", value=" + Arrays.toString(value) + "]";
    }

  }

  class Param {

    String name;
    String value;

    Param() {
    }

    Param(String name, String value) {
      this.name = name;
      this.value = value;
    }

    @Override
    public String toString() {
      return "Param [name=" + name + ", value=" + value + "]";
    }

  }

  class FileParam {
    String name;
    File file;
  }

  class UploadParam {
    String name;
    String filename;
    String contentType;
    InputStream is;
  }

}
