package com.github.hualuomoli.gateway.server.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.github.hualuomoli.gateway.api.entity.Request;
import com.github.hualuomoli.gateway.api.entity.Response;
import com.github.hualuomoli.gateway.api.lang.InvalidDataException;
import com.github.hualuomoli.gateway.api.lang.NoPartnerException;

/**
 * 拦截器
 */
public interface Interceptor {

    /**
     * 执行业务前置处理
     * @param req HTTP请求
     * @param res HTTP响应
     * @param request 网关请求
     * @param NoPartnerException 合作伙伴未注册
     * @throws InvalidDataException 不合法的数据
     */
    void preHandle(HttpServletRequest req, HttpServletResponse res, Request request) throws NoPartnerException, InvalidDataException;

    /**
     * 执行业务后置处理
     * @param req HTTP请求
     * @param res HTTP响应
     * @param request 网关请求
     * @param response 网关响应
     * @throws InvalidDataException 不合法的数据
     */
    void postHandle(HttpServletRequest req, HttpServletResponse res, Request request, Response response);

}
