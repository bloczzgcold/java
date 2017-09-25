package com.github.hualuomoli.gateway.server.business.interceptor;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.github.hualuomoli.gateway.server.lang.BusinessException;

/**
 * 业务拦截器
 */
public interface BusinessInterceptor {

  /**
   * 前置处理。如验证请求参数是否合法
   * @param req HTTP请求
   * @param method 处理方法
   * @param handler 处理类
   * @param params 业务参数
   */
  void preHandle(HttpServletRequest req, Method method, Object handler, Object[] params);

  /**
   * 后置处理
   * @param req HTTP请求
   * @param res HTTP响应
   * @param result 业务返回结果
   */
  void postHandle(HttpServletRequest req, HttpServletResponse res, Object result);

  /**
   * 错误处理
   * @param req HTTP请求
   * @param res HTTP响应
   * @param be 业务处理错误
   */
  void afterCompletion(HttpServletRequest req, HttpServletResponse res, BusinessException be);

}
