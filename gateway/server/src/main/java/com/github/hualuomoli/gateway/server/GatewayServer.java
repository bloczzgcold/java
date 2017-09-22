package com.github.hualuomoli.gateway.server;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.github.hualuomoli.gateway.server.business.BusinessHandler;
import com.github.hualuomoli.gateway.server.entity.Request;
import com.github.hualuomoli.gateway.server.entity.Response;
import com.github.hualuomoli.gateway.server.interceptor.Interceptor;
import com.github.hualuomoli.gateway.server.lang.BusinessException;
import com.github.hualuomoli.gateway.server.lang.NoPartnerException;
import com.github.hualuomoli.gateway.server.lang.NoRouterException;
import com.github.hualuomoli.gateway.server.lang.SecurityException;

/**
 * 网关服务器
 */
public abstract class GatewayServer {

  private BusinessHandler businessHandler;
  private List<Interceptor> interceptors = new ArrayList<Interceptor>();

  public void setBusinessHandler(BusinessHandler businessHandler) {
    this.businessHandler = businessHandler;
  }

  public void setInterceptors(List<Interceptor> interceptors) {
    this.interceptors = interceptors;
  }

  public <R extends Response> R execute(HttpServletRequest req, HttpServletResponse res, Class<R> clazz) throws NoPartnerException, SecurityException, NoRouterException, BusinessException {
    Request request = this.parseRequest(req);

    R response = this.newInstance(clazz);
    // 前置拦截
    for (int i = 0, size = interceptors.size(); i < size; i++) {
      interceptors.get(i).preHandle(req, request);
    }

    // 执行业务 
    String result = businessHandler.execute(req, res, request.getPartnerId(), request.getMethod(), request.getBizContent());
    response.setResult(result);

    // 后置拦截
    for (int size = interceptors.size(), i = size - 1; i >= 0; i--) {
      interceptors.get(i).postHandle(req, res, request, response);
    }

    return response;
  }

  /**
   * 实例化响应
   * @param clazz 类类型
   * @return 实例
   */
  private <R extends Response> R newInstance(Class<R> clazz) {
    try {
      return clazz.newInstance();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    // end
  }

  /**
   * 解析HTTP请求
   * @param req HTTP请求
   * @return 请求信息
   */
  protected abstract Request parseRequest(HttpServletRequest req);

}
