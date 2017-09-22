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
import com.github.hualuomoli.gateway.server.entity.Request;
import com.github.hualuomoli.gateway.server.entity.Response;
import com.github.hualuomoli.gateway.server.interceptor.Interceptor;
import com.github.hualuomoli.gateway.server.lang.NoPartnerException;
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

  @Autowired
  private BusinessHandler businessHandler;

  @Bean
  public GatewayServer initGateway() {

    GatewayServer server = new GatewayServer() {

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
    server.setInterceptors(this.interceptors());

    return server;
  }

  private List<Interceptor> interceptors() {
    List<Interceptor> interceptors = Lists.newArrayList();

    // 加密、解密
    interceptors.add(new Interceptor() {

      @Override
      public void preHandle(HttpServletRequest req, Request request) throws NoPartnerException, SecurityException {
        GatewayServerRequest gatewayServerRequest = (GatewayServerRequest) request;
        gatewayServerRequest.setBizContent(AES.decrypt(Key.SALT, gatewayServerRequest.getBizContent()));
      }

      @Override
      public void postHandle(HttpServletRequest req, HttpServletResponse res, Request request, Response response) {
        GatewayServerResponse gatewayServerResponse = (GatewayServerResponse) response;
        gatewayServerResponse.setResult(AES.encrypt(Key.SALT, gatewayServerResponse.getResult()));
      }

    });
    // 签名、验签
    interceptors.add(new Interceptor() {

      final Logger logger = LoggerFactory.getLogger(Interceptor.class);

      @Override
      public void preHandle(HttpServletRequest req, Request request) throws NoPartnerException, SecurityException {
        GatewayServerRequest gatewayServerRequest = (GatewayServerRequest) request;
        String origin = this.getSignOrigin(gatewayServerRequest, Sets.newHashSet("sign"));
        logger.debug("服务端端请求签名原文={}", origin);

        if (!RSA.verify(Key.CLIENT_PUBLIC_KEY, origin, gatewayServerRequest.getSign())) {
          throw new SecurityException("签名不合法");
        }
        // end
      }

      @Override
      public void postHandle(HttpServletRequest req, HttpServletResponse res, Request request, Response response) {
        GatewayServerResponse gatewayServerResponse = (GatewayServerResponse) response;
        String origin = this.getSignOrigin(gatewayServerResponse, Sets.newHashSet("sign"));
        String sign = RSA.sign(Key.SERVER_PRIVATE_KEY, origin);
        logger.debug("服务端端响应签名原文={}", origin);

        gatewayServerResponse.setSign(sign);
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
    interceptors.add(new Interceptor() {

      @Override
      public void preHandle(HttpServletRequest req, Request request) throws NoPartnerException, SecurityException {
      }

      @Override
      public void postHandle(HttpServletRequest req, HttpServletResponse res, Request request, Response response) {
        GatewayServerRequest gatewayServerRequest = (GatewayServerRequest) request;
        GatewayServerResponse gatewayServerResponse = (GatewayServerResponse) response;
        gatewayServerResponse.setCode("SUCCESS");
        gatewayServerResponse.setMessage("执行成功");
        gatewayServerResponse.setSubCode("SUCCESS");
        gatewayServerResponse.setSubMessage("业务处理成功");
        gatewayServerResponse.setNonceStr(gatewayServerRequest.getNonceStr());
      }
    });

    // 日志
    interceptors.add(new Interceptor() {

      final Logger logger = LoggerFactory.getLogger(Interceptor.class);

      @Override
      public void preHandle(HttpServletRequest req, Request request) throws NoPartnerException, com.github.hualuomoli.gateway.server.lang.SecurityException {
        logger.debug("请求业务内容={}", request.getBizContent());
      }

      @Override
      public void postHandle(HttpServletRequest req, HttpServletResponse res, Request request, Response response) {
        GatewayServerResponse gatewayServerResponse = (GatewayServerResponse) response;
        logger.debug("响应业务内容={}", gatewayServerResponse.getResult());
      }
    });

    return interceptors;
  }

}
