package com.github.hualuomoli.sample.gateway.server.biz.gateway.service;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.github.hualuomoli.gateway.server.GatewayServer;
import com.github.hualuomoli.gateway.server.error.ErrorDealer;
import com.github.hualuomoli.gateway.server.interceptor.Interceptor;
import com.github.hualuomoli.gateway.server.lang.BusinessException;
import com.github.hualuomoli.gateway.server.lang.NoPartnerException;
import com.github.hualuomoli.gateway.server.lang.NoRouterException;
import com.github.hualuomoli.gateway.server.lang.SecurityException;
import com.github.hualuomoli.sample.gateway.server.biz.gateway.entity.GatewayServerRequest;
import com.github.hualuomoli.sample.gateway.server.biz.gateway.entity.GatewayServerResponse;
import com.github.hualuomoli.sample.gateway.server.biz.gateway.enums.ResponseCodeEnum;
import com.github.hualuomoli.sample.gateway.server.key.Key;
import com.github.hualuomoli.tool.security.AES;
import com.github.hualuomoli.tool.security.RSA;
import com.github.hualuomoli.tool.util.ClassUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

@Service(value = "com.github.hualuomoli.sample.gateway.server.biz.gateway.service.GatewayService")
public class GatewayService extends GatewayServer<GatewayServerRequest, GatewayServerResponse> {

  private static final Logger logger = LoggerFactory.getLogger(GatewayService.class);

  @Autowired
  private GatewayBusinessHandler gatewayBusinessHandler;

  @PostConstruct
  public void init() {
    // 业务处理器
    this.setBusinessHandler(gatewayBusinessHandler);
    // 错误处理器
    this.setErrorDealer(this.errorDealer());
    // 拦截器
    List<Interceptor<GatewayServerRequest, GatewayServerResponse>> interceptors = Lists.newArrayList();
    interceptors.add(new EncryptInterceptor());
    interceptors.add(new SignInterceptor());
    interceptors.add(new ApplicationInterceptor());
    interceptors.add(new LogInterceptor());
    super.setInterceptors(interceptors);
  }

  @Override
  protected GatewayServerRequest parseRequest(HttpServletRequest req) {
    Enumeration<String> names = req.getParameterNames();
    Map<String, String> map = Maps.newHashMap();
    while (names.hasMoreElements()) {
      String name = names.nextElement();
      map.put(name, req.getParameter(name));
    }
    return JSON.parseObject(JSON.toJSONString(map), GatewayServerRequest.class);
  }

  // 错误处理器
  private ErrorDealer<GatewayServerRequest, GatewayServerResponse> errorDealer() {
    return new ErrorDealer<GatewayServerRequest, GatewayServerResponse>() {

      @Override
      public void deal(HttpServletRequest req, HttpServletResponse res, GatewayServerRequest request, GatewayServerResponse response, NoPartnerException npe) {
        this.config(ResponseCodeEnum.NO_PARTNER, "合作伙伴未注册", response);
      }

      @Override
      public void deal(HttpServletRequest req, HttpServletResponse res, GatewayServerRequest request, GatewayServerResponse response, SecurityException se) {
        String message = se.getMessage() == null ? "安全认证失败" : se.getMessage();
        this.config(ResponseCodeEnum.INVALID_SECURITY, message, response);
      }

      @Override
      public void deal(HttpServletRequest req, HttpServletResponse res, GatewayServerRequest request, GatewayServerResponse response, NoRouterException nre) {
        this.config(ResponseCodeEnum.NO_ROUTER, "请求方法未注册", response);
      }

      @Override
      public void deal(HttpServletRequest req, HttpServletResponse res, GatewayServerRequest request, GatewayServerResponse response, BusinessException be) {
        this.config(ResponseCodeEnum.BUSINESS, "业务处理失败", response);
        response.setSubCode("9999");
        response.setSubMessage(be.getMessage());
      }

      @Override
      public void deal(HttpServletRequest req, HttpServletResponse res, GatewayServerRequest request, GatewayServerResponse response, Exception e) {
        this.config(ResponseCodeEnum.SYSTEM, "系统错误", response);
        logger.debug("系统错误", e);
      }

      private void config(ResponseCodeEnum responseCode, String message, GatewayServerResponse response) {
        response.setCode(responseCode);
        response.setMessage(message);
      }

    };
  }

  // 加密、解密拦截器
  private class EncryptInterceptor implements Interceptor<GatewayServerRequest, GatewayServerResponse> {

    @Override
    public void preHandle(HttpServletRequest req, HttpServletResponse res, GatewayServerRequest request, GatewayServerResponse response)
        throws NoPartnerException, SecurityException {
      request.setBizContent(AES.decrypt(Key.SALT, request.getBizContent()));
    }

    @Override
    public void postHandle(HttpServletRequest req, HttpServletResponse res, GatewayServerRequest request, GatewayServerResponse response) {
      response.setResult(AES.encrypt(Key.SALT, response.getResult()));
    }
    // end
  }

  // 签名、验签拦截器
  private class SignInterceptor implements Interceptor<GatewayServerRequest, GatewayServerResponse> {

    @Override
    public void preHandle(HttpServletRequest req, HttpServletResponse res, GatewayServerRequest request, GatewayServerResponse response)
        throws NoPartnerException, SecurityException {
      String origin = this.getSignOrigin(request, Sets.newHashSet("sign"));
      logger.debug("服务端端请求签名原文={}", origin);

      if (!RSA.verify(Key.CLIENT_PUBLIC_KEY, origin, request.getSign())) {
        throw new SecurityException("签名不合法");
      }
      // end
    }

    @Override
    public void postHandle(HttpServletRequest req, HttpServletResponse res, GatewayServerRequest request, GatewayServerResponse response) {
      String origin = this.getSignOrigin(response, Sets.newHashSet("sign"));
      String sign = RSA.sign(Key.SERVER_PRIVATE_KEY, origin);
      logger.debug("服务端端响应签名原文={}", origin);

      response.setSign(sign);
    }

    private String getSignOrigin(Object obj, Set<String> ignores) {
      Class<? extends Object> clazz = obj.getClass();
      List<Field> fields = ClassUtils.getFields(clazz);
      Collections.sort(fields, new Comparator<Field>() {

        @Override
        public int compare(Field o1, Field o2) {
          return o1.getName().compareTo(o2.getName());
        }
      });

      StringBuilder buffer = new StringBuilder();
      for (Field field : fields) {
        if (ignores.contains(field.getName())) {
          continue;
        }
        Object value = ClassUtils.getFieldValue(field, obj);
        if (value == null) {
          continue;
        }
        buffer.append("&").append(field.getName()).append("=").append(value.toString());
      }

      return buffer.toString().substring(1);
    }
    // end
  }

  // 应用拦截器
  private class ApplicationInterceptor implements Interceptor<GatewayServerRequest, GatewayServerResponse> {

    @Override
    public void preHandle(HttpServletRequest req, HttpServletResponse res, GatewayServerRequest request, GatewayServerResponse response)
        throws NoPartnerException, SecurityException {
      logger.debug("请求业务内容={}", request.getBizContent());
    }

    @Override
    public void postHandle(HttpServletRequest req, HttpServletResponse res, GatewayServerRequest request, GatewayServerResponse response) {
      response.setCode(ResponseCodeEnum.SUCCESS);
      response.setMessage("执行成功");
      response.setSubCode(ResponseCodeEnum.SUCCESS.name());
      response.setSubMessage("业务处理成功");

      logger.debug("响应业务结果={}", response.getResult());
    }
    // end
  }

  // 日志拦截器
  private class LogInterceptor implements Interceptor<GatewayServerRequest, GatewayServerResponse> {

    @Override
    public void preHandle(HttpServletRequest req, HttpServletResponse res, GatewayServerRequest request, GatewayServerResponse response)
        throws NoPartnerException, SecurityException {
      if (!logger.isDebugEnabled()) {
        return;
      }
      logger.debug("请求业务内容={}", request.getBizContent());
    }

    @Override
    public void postHandle(HttpServletRequest req, HttpServletResponse res, GatewayServerRequest request, GatewayServerResponse response) {
      if (!logger.isDebugEnabled()) {
        return;
      }
      logger.debug("响应业务内容={}", response.getResult());
    }
    // end
  }

}
