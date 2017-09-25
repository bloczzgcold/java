package com.github.hualuomoli.config.gateway;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.alibaba.fastjson.JSON;
import com.github.hualuomoli.config.gateway.entity.GatewayServerRequest;
import com.github.hualuomoli.config.gateway.entity.GatewayServerResponse;
import com.github.hualuomoli.gateway.server.GatewayServer;
import com.github.hualuomoli.gateway.server.business.BusinessHandler;
import com.github.hualuomoli.gateway.server.error.ErrorDealer;
import com.github.hualuomoli.gateway.server.interceptor.Interceptor;
import com.github.hualuomoli.gateway.server.lang.BusinessException;
import com.github.hualuomoli.gateway.server.lang.NoPartnerException;
import com.github.hualuomoli.gateway.server.lang.NoRouterException;
import com.github.hualuomoli.gateway.server.lang.SecurityException;
import com.github.hualuomoli.sample.gateway.server.key.Key;
import com.github.hualuomoli.tool.security.AES;
import com.github.hualuomoli.tool.security.RSA;
import com.github.hualuomoli.tool.util.ClassUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

@Configuration(value = "com.github.hualuomoli.config.gateway.GatewayServerConfig")
@Import(value = { GatewayBusinessHandlerConfig.class })
public class GatewayServerConfig {

  private static final Logger logger = LoggerFactory.getLogger(Interceptor.class);

  @Autowired
  private BusinessHandler businessHandler;

  @Bean
  public GatewayServer<GatewayServerRequest, GatewayServerResponse> initGateway() {

    GatewayServer<GatewayServerRequest, GatewayServerResponse> server = new GatewayServer<GatewayServerRequest, GatewayServerResponse>() {

      @Override
      protected GatewayServerRequest parseRequest(HttpServletRequest req) {
        Map<String, String> map = Maps.newHashMap();
        Enumeration<String> names = req.getParameterNames();
        while (names.hasMoreElements()) {
          String name = names.nextElement();
          String value = req.getParameter(name);
          map.put(name, value);
        }
        return JSON.parseObject(JSON.toJSONString(map), GatewayServerRequest.class);
      }

    };
    server.setBusinessHandler(businessHandler);
    server.setErrorDealer(this.errorDealer());
    server.setInterceptors(this.interceptors());

    return server;
  }

  // 错误处理器
  private ErrorDealer<GatewayServerRequest, GatewayServerResponse> errorDealer() {
    return new ErrorDealer<GatewayServerRequest, GatewayServerResponse>() {

      @Override
      public void deal(HttpServletRequest req, HttpServletResponse res, GatewayServerRequest request, GatewayServerResponse response, NoPartnerException npe) {
        response.setCode("NO_PARTNER");
        response.setMessage("合作伙伴未注册");
      }

      @Override
      public void deal(HttpServletRequest req, HttpServletResponse res, GatewayServerRequest request, GatewayServerResponse response, SecurityException se) {
        response.setCode("INVALID_SECURITY");
        response.setMessage("认证失败");
      }

      @Override
      public void deal(HttpServletRequest req, HttpServletResponse res, GatewayServerRequest request, GatewayServerResponse response, NoRouterException nre) {
        response.setCode("NO_ROUTER");
        response.setMessage("方法未注册");
      }

      @Override
      public void deal(HttpServletRequest req, HttpServletResponse res, GatewayServerRequest request, GatewayServerResponse response, BusinessException be) {
        response.setCode("SUCCESS");
        response.setMessage("执行成功");
        response.setSubCode("9999");
        response.setSubMessage(be.getMessage());
      }

      @Override
      public void deal(HttpServletRequest req, HttpServletResponse res, GatewayServerRequest request, GatewayServerResponse response, Exception e) {
        response.setCode("ERROR");
        response.setMessage("系统错误");
      }
    };
  }

  // 拦截器
  private List<Interceptor<GatewayServerRequest, GatewayServerResponse>> interceptors() {
    List<Interceptor<GatewayServerRequest, GatewayServerResponse>> interceptors = Lists.newArrayList();

    // 加密、解密
    interceptors.add(new Interceptor<GatewayServerRequest, GatewayServerResponse>() {

      @Override
      public void preHandle(HttpServletRequest req, HttpServletResponse res, GatewayServerRequest request, GatewayServerResponse response)
          throws NoPartnerException, SecurityException {
        request.setBizContent(AES.decrypt(Key.SALT, request.getBizContent()));
      }

      @Override
      public void postHandle(HttpServletRequest req, HttpServletResponse res, GatewayServerRequest request, GatewayServerResponse response) {
        response.setResult(AES.encrypt(Key.SALT, response.getResult()));
      }

    });
    // 签名、验签
    interceptors.add(new Interceptor<GatewayServerRequest, GatewayServerResponse>() {

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

    });

    // 其它
    interceptors.add(new Interceptor<GatewayServerRequest, GatewayServerResponse>() {

      @Override
      public void preHandle(HttpServletRequest req, HttpServletResponse res, GatewayServerRequest request, GatewayServerResponse response)
          throws NoPartnerException, SecurityException {
        logger.debug("请求业务内容={}", request.getBizContent());
      }

      @Override
      public void postHandle(HttpServletRequest req, HttpServletResponse res, GatewayServerRequest request, GatewayServerResponse response) {
        response.setCode("SUCCESS");
        response.setMessage("执行成功");
        response.setSubCode("SUCCESS");
        response.setSubMessage("业务处理成功");

        logger.debug("响应业务结果={}", response.getResult());
      }
    });

    // 日志
    interceptors.add(new Interceptor<GatewayServerRequest, GatewayServerResponse>() {

      @Override
      public void preHandle(HttpServletRequest req, HttpServletResponse res, GatewayServerRequest request, GatewayServerResponse response)
          throws NoPartnerException, SecurityException {
        logger.debug("请求业务内容={}", request.getBizContent());
      }

      @Override
      public void postHandle(HttpServletRequest req, HttpServletResponse res, GatewayServerRequest request, GatewayServerResponse response) {
        logger.debug("响应业务内容={}", response.getResult());
      }
    });

    return interceptors;
  }

}
