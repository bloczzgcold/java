package com.github.hualuomoli.gateway.client.invoker;

import java.io.IOException;
import java.util.Set;

import com.github.hualuomoli.gateway.api.entity.Request;

/**
 * 客户端
 */
public interface ClientInvoker {

  /**
   * 调用
   * @param request 请求信息
   * @return invoker
   * @throws IOException 调用错误
   */
  ClientInvoker call(Request request) throws IOException;

  /**
   * 添加请求头
   * @param name 名称
   * @param value 值
   * @return invoker
   */
  ClientInvoker addRequestHeader(String name, String value);

  /**
   * 获取响应结果
   * @return 响应结果
   */
  String getResult();

  /**
   * 获取响应头名称
   * @return 响应头名称
   */
  Set<String> getReponseHeaders();

  /**
   * 获取响应头
   * @param name 头名称
   * @return 响应头
   */
  String[] getReponseHeader(String name);

}
