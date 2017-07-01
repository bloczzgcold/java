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
   * @param req HTTP请求
   * @param res HTTP响应
   * @throws NoAuthorityException 没有访问权限
   */
  void handle(HttpServletRequest req, HttpServletResponse res) throws NoAuthorityException;

}
