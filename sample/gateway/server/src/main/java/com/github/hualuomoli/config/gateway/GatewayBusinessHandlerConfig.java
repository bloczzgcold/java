package com.github.hualuomoli.config.gateway;

import java.lang.reflect.Method;
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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.hualuomoli.gateway.api.lang.BusinessException;
import com.github.hualuomoli.gateway.api.lang.NoAuthorityException;
import com.github.hualuomoli.gateway.api.parser.JSONParser;
import com.github.hualuomoli.gateway.server.business.AbstractBusinessHandler;
import com.github.hualuomoli.gateway.server.business.BusinessHandler;
import com.github.hualuomoli.gateway.server.business.dealer.FunctionDealer;
import com.github.hualuomoli.gateway.server.business.interceptor.AuthorityInterceptor;
import com.github.hualuomoli.gateway.server.business.interceptor.BusinessInterceptor;
import com.github.hualuomoli.gateway.server.business.local.Local;
import com.github.hualuomoli.gateway.server.business.parser.BusinessErrorParser;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

@Configuration(value = "com.github.hualuomoli.config.gateway.GatewayBusinessHandlerConfig")
public class GatewayBusinessHandlerConfig {

  private static final Logger logger = LoggerFactory.getLogger(GatewayBusinessHandlerConfig.class);

  @Autowired
  private FunctionDealer functionDealer;

  @Bean
  public BusinessHandler businessHandler() {
    AbstractBusinessHandler handler = new AbstractBusinessHandler();
    handler.setAuthorityInterceptor(this.authorityInterceptor());
    handler.setInterceptors(this.interceptors());
    handler.setFunctionDealer(functionDealer);
    handler.setBusinessErrorParser(this.businessErrorParser());
    handler.setJsonParser(this.jsonParser());
    handler.setPackageNames(new String[] { "com.github.hualuomoli" });

    return handler;
  }

  // 业务权限认证
  private AuthorityInterceptor authorityInterceptor() {
    return new AuthorityInterceptor() {

      @Override
      public void handle(String partnerId, String method, HttpServletRequest req, HttpServletResponse res) throws NoAuthorityException {
        logger.debug("业务权限认证{}是否有{}的访问权限", partnerId, method);
      }
    };
  }

  // 业务拦截器
  private List<BusinessInterceptor> interceptors() {
    List<BusinessInterceptor> interceptors = Lists.newArrayList();

    // 日志
    interceptors.add(new BusinessInterceptor() {

      @Override
      public void preHandle(HttpServletRequest req, HttpServletResponse res, Method method, Object handler, Object[] params) {
        logger.debug("业务处理前日志输出 partnerId={},method={},bizContent={}", Local.getPartnerId(), Local.getMethod(), Local.getBizContent());
      }

      @Override
      public void postHandle(HttpServletRequest req, HttpServletResponse res, Object result) {
        logger.debug("业务处理后日志输出 partnerId={},method={},result={}", Local.getPartnerId(), Local.getMethod(), result);
      }

      @Override
      public void afterCompletion(HttpServletRequest req, HttpServletResponse res, BusinessException be) {
        logger.debug("业务处理出现异常 partnerId={},method={},e={}", Local.getPartnerId(), Local.getMethod(), be.getMessage(), be);
      }
    });

    return interceptors;
  }

  // 业务处理错误转换器
  private BusinessErrorParser businessErrorParser() {
    return new BusinessErrorParser() {
      @Override
      public BusinessException parse(IllegalAccessException e) {
        return new BusinessException("0001", "没有访问权限");
      }

      @Override
      public BusinessException parse(IllegalArgumentException e) {
        return new BusinessException("0002", "请求参数不合法");
      }

      @Override
      public BusinessException parse(Throwable t) {
        return new BusinessException("9999", t.getMessage());
      }
    };
  }

  // JSON转换器
  private JSONParser jsonParser() {
    return new JSONParser() {

      @Override
      public String toJsonString(Object object) {
        if (object == null) {
          return null;
        }
        if (String.class.isAssignableFrom(object.getClass())) {
          return (String) object;
        }
        return JSON.toJSONString(object);
      }

      @Override
      public <T> T parseObject(String content, Class<T> clazz) {
        if (content == null) {
          return null;
        }
        return JSON.parseObject(content, clazz);
      }

      @Override
      public <T> List<T> parseArray(String content, Class<T> clazz) {
        if (content == null) {
          return null;
        }
        return JSON.parseArray(content, clazz);
      }

      @Override
      public Map<String, Object> parse(String content) {
        Map<String, Object> map = Maps.newHashMap();
        JSONObject obj = JSON.parseObject(content);
        Set<String> set = obj.keySet();
        for (String key : set) {
          map.put(key, obj.get(key));
        }
        return map;
      }
    };
  }
}
