package com.github.hualuomoli.gateway.client.interceptor;

import com.github.hualuomoli.gateway.api.entity.Request;
import com.github.hualuomoli.gateway.api.entity.Response;
import com.github.hualuomoli.gateway.api.lang.InvalidDataException;

/**
 * 拦截器
 */
public interface Interceptor {

  /**
   * 执行业务前置处理
   * @param request 网关请求
   * @throws InvalidDataException 不合法的数据
   */
  void preHandle(Request request);

  /**
   * 执行业务后置处理
   * @param request 网关请求
   * @param response 网关响应
   * @throws InvalidDataException 不合法的数据
   */
  void postHandle(Request request, Response response) throws InvalidDataException;

}
