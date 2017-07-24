package com.github.hualuomoli.gateway.server.business.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.github.hualuomoli.gateway.api.lang.NoAuthorityException;

/**
 * 权限拦截器
 */
public interface AuthorityInterceptor {

  /**
   * 验证权限
   * @param partnerId 合作伙伴ID
   * @param method 请求方法
   * @param req HTTP请求
   * @param res HTTP响应
   * @throws NoAuthorityException 没有访问权限
   */
  void handle(String partnerId, String method, HttpServletRequest req, HttpServletResponse res) throws NoAuthorityException;

}
