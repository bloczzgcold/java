package com.github.hualuomoli.gateway.server.error;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.github.hualuomoli.gateway.server.entity.Request;
import com.github.hualuomoli.gateway.server.entity.Response;
import com.github.hualuomoli.gateway.server.lang.BusinessException;
import com.github.hualuomoli.gateway.server.lang.NoPartnerException;
import com.github.hualuomoli.gateway.server.lang.NoRouterException;
import com.github.hualuomoli.gateway.server.lang.SecurityException;

/**
 * 错误处理器
 */
public interface ErrorDealer<Req extends Request, Res extends Response> {

  /**
   * 合作伙伴未找到
   * @param req HTTP请求
   * @param res HTTP响应
   * @param request 网关请求
   * @param response 网关响应
   * @param npe 合作伙伴未找到
   */
  void deal(HttpServletRequest req, HttpServletResponse res, Req request, Res response, NoPartnerException npe);

  /**
   * 认证错误
   * @param req HTTP请求
   * @param res HTTP响应
   * @param request 网关请求
   * @param response 网关响应
   * @param se 认证错误
   */
  void deal(HttpServletRequest req, HttpServletResponse res, Req request, Res response, SecurityException se);

  /**
   * 请求方法未找到
   * @param req HTTP请求
   * @param res HTTP响应
   * @param request 网关请求
   * @param response 网关响应
   * @param nre 请求方法未找到
   */
  void deal(HttpServletRequest req, HttpServletResponse res, Req request, Res response, NoRouterException nre);

  /**
   * 业务处理错误
   * @param req HTTP请求
   * @param res HTTP响应
   * @param request 网关请求
   * @param response 网关响应
   * @param be 业务处理错误
   */
  void deal(HttpServletRequest req, HttpServletResponse res, Req request, Res response, BusinessException be);

  /**
   * 错误
   * @param req HTTP请求
   * @param res HTTP响应
   * @param request 网关请求
   * @param response 网关响应
   * @param e 错误
   */
  void deal(HttpServletRequest req, HttpServletResponse res, Req request, Res response, Exception e);

}
