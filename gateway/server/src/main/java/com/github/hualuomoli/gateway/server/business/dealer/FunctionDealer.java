package com.github.hualuomoli.gateway.server.business.dealer;

import javax.servlet.http.HttpServletRequest;

import com.github.hualuomoli.gateway.api.lang.NoRouterException;
import com.github.hualuomoli.gateway.server.business.entity.Function;

public interface FunctionDealer {

  /**
   * 获取请求对应的Function
   * @param method 请求方法
   * @param request HTTP请求
   * @return Function
   * @throws NoRouterException 没有路由
   */
  <F extends Function> F getFunction(String method, HttpServletRequest request) throws NoRouterException;

  /**
   * 获取Function的处理类
   * @param function Function
   * @return 处理类
   */
  <F extends Function> Object getDealer(F function);

}
