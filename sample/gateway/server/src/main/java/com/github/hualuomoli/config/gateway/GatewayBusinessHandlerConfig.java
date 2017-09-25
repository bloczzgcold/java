package com.github.hualuomoli.config.gateway;

import java.lang.reflect.Method;
import java.util.Collection;
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
import com.github.hualuomoli.enums.GatewaySubErrorEnum;
import com.github.hualuomoli.gateway.server.business.AbstractBusinessHandler;
import com.github.hualuomoli.gateway.server.business.BusinessHandler;
import com.github.hualuomoli.gateway.server.business.dealer.FunctionDealer;
import com.github.hualuomoli.gateway.server.business.interceptor.BusinessInterceptor;
import com.github.hualuomoli.gateway.server.business.parser.BusinessErrorParser;
import com.github.hualuomoli.gateway.server.business.parser.JSONParser;
import com.github.hualuomoli.gateway.server.lang.BusinessException;
import com.github.hualuomoli.validator.Validator;
import com.github.hualuomoli.validator.lang.InvalidParameterException;
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
    handler.setInterceptors(this.interceptors());
    handler.setFunctionDealer(functionDealer);
    handler.setBusinessErrorParser(this.businessErrorParser());
    handler.setJsonParser(this.jsonParser());
    handler.setPackageNames(new String[] { "com.github.hualuomoli" });

    return handler;
  }

  // 业务拦截器
  private List<BusinessInterceptor> interceptors() {
    List<BusinessInterceptor> interceptors = Lists.newArrayList();

    // 验证参数
    interceptors.add(new BusinessInterceptor() {

      private void valid(Object object) {
        try {
          Validator.valid(object);
        } catch (InvalidParameterException ipe) {
          GatewaySubErrorEnum e = GatewaySubErrorEnum.INVALID_PARAMETER;
          throw new BusinessException(e, ipe.getMessage(), e.getErrorCode());
        }
      }

      @Override
      public void preHandle(HttpServletRequest req, HttpServletResponse res, Method method, Object handler, Object[] params) {
        if (params == null || params.length == 0) {
          return;
        }
        for (Object param : params) {
          if (param == null) {
            continue;
          }
          if (HttpServletRequest.class.isAssignableFrom(param.getClass())) {
            continue;
          }
          if (HttpServletResponse.class.isAssignableFrom(param.getClass())) {
            continue;
          }
          // 集合
          if (Collection.class.isAssignableFrom(param.getClass())) {
            Collection<?> c = (Collection<?>) param;
            for (Object p : c) {
              this.valid(p);
            }
            continue;
          }
          // 基本参数
          this.valid(param);
        }
      }

      @Override
      public void postHandle(HttpServletRequest req, HttpServletResponse res, Object result) {
      }

      @Override
      public void afterCompletion(HttpServletRequest req, HttpServletResponse res, BusinessException be) {
      }
    });

    return interceptors;
  }

  // 业务处理错误转换器
  private BusinessErrorParser businessErrorParser() {
    return new BusinessErrorParser() {
      @Override
      public BusinessException parse(IllegalAccessException e) {
        return this.parse(GatewaySubErrorEnum.INVALID_AUTHORITY);
      }

      @Override
      public BusinessException parse(IllegalArgumentException e) {
        return this.parse(GatewaySubErrorEnum.INVALID_PARAMETER);
      }

      @Override
      public BusinessException parse(Throwable t) {
        logger.debug("转换错误信息,输出当前错误日志", t);
        return this.parse(GatewaySubErrorEnum.SYSTEM, t.getMessage());
      }

      private BusinessException parse(GatewaySubErrorEnum e) {
        return new BusinessException(e, e.getMessage(), e.getErrorCode());
      }

      private BusinessException parse(GatewaySubErrorEnum e, String message) {
        if (message == null || message.trim().length() == 0) {
          message = e.getMessage();
        }
        return new BusinessException(e, message, e.getErrorCode());
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
