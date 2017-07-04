package com.github.hualuomoli.sample.gateway.server.biz.gateway.service;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSON;
import com.github.hualuomoli.framework.mvc.annotation.ApiVersion;
import com.github.hualuomoli.gateway.api.lang.BusinessException;
import com.github.hualuomoli.gateway.api.parser.JSONParser;
import com.github.hualuomoli.gateway.server.business.AbstractBusinessHandler;
import com.github.hualuomoli.gateway.server.business.Parser;
import com.google.common.collect.Lists;

@Service(value = "com.github.hualuomoli.sample.gateway.server.biz.gateway.service.BusinessHandlerService")
public class BusinessHandlerService extends AbstractBusinessHandler implements ApplicationContextAware {

  private ApplicationContext context;

  private static final String VERSION_REGEX = "^\\d+(.\\d+)*$";

  @Override
  protected Parser parser() {
    final ApplicationContext applicationContext = context;

    return new Parser() {

      @Override
      public List<Class<?>> dealers() {
        List<Class<?>> clazzes = Lists.newArrayList();
        Map<String, Object> map = applicationContext.getBeansWithAnnotation(RequestMapping.class);
        Collection<Object> values = map.values();
        for (Object object : values) {
          clazzes.add(object.getClass());
        }
        return clazzes;
      }

      @Override
      public Object instance(Class<?> dealerClazz) {
        return applicationContext.getBean(dealerClazz);
      }

      @Override
      public String loadGatewayMethod(Class<?> clazz, Method method) {
        RequestMapping classRequestMapping = clazz.getAnnotation(RequestMapping.class);
        String[] classValues = classRequestMapping.value();
        if (classValues == null || classValues.length == 0) {
          return null;
        }

        RequestMapping methodRequestMapping = method.getAnnotation(RequestMapping.class);
        if (methodRequestMapping == null) {
          return null;
        }

        String[] methodValues = methodRequestMapping.value();
        if (methodValues == null || methodValues.length == 0) {
          return null;
        }

        String classValue = classValues[0];
        String methodValue = methodValues[0];

        if (classValue.startsWith("/")) {
          classValue = classValue.substring(1);
        }
        if (classValue.endsWith("/")) {
          classValue = classValue.substring(0, classValue.length() - 1);
        }
        if (!methodValue.startsWith("/")) {
          methodValue = "/" + methodValue;
        }
        if (methodValue.endsWith("/")) {
          methodValue = methodValue.substring(0, methodValue.length() - 1);
        }

        String url = classValue + methodValue;

        return StringUtils.replace(url, "/", ".");
      }

      @Override
      public String loadVersion(Class<?> clazz, Method method) {
        String classVersion = this.loadClassVersion(clazz);
        String methodVersion = this.loadMethodVersion(method);

        return StringUtils.isNotBlank(methodVersion) ? methodVersion : classVersion;
      }

      private String loadClassVersion(Class<?> clazz) {
        ApiVersion apiVersion = clazz.getAnnotation(ApiVersion.class);
        return apiVersion == null ? null : apiVersion.value();
      }

      private String loadMethodVersion(Method method) {
        ApiVersion apiVersion = method.getAnnotation(ApiVersion.class);
        return apiVersion == null ? null : apiVersion.value();
      }

      @Override
      public boolean support(String functionVersion, String requestVersion) {
        return this.compare(functionVersion, requestVersion) <= 0;
      }

      @Override
      public int compare(String v1, String v2) {
        if (v1 == null && v2 == null) {
          return 0;
        }
        if (v1 == null) {
          return -1;
        }
        if (v2 == null) {
          return 1;
        }
        Validate.matchesPattern(v1, VERSION_REGEX);
        Validate.matchesPattern(v2, VERSION_REGEX);

        if (StringUtils.equals(v1, v2)) {
          return 0;
        }

        String[] array1 = StringUtils.split(v1, ".");
        String[] array2 = StringUtils.split(v2, ".");

        int len1 = array1.length;
        int len2 = array2.length;
        int len = len1 > len2 ? len2 : len1;
        for (int i = 0; i < len; i++) {
          Integer i1 = Integer.parseInt(array1[i]);
          Integer i2 = Integer.parseInt(array2[i]);
          int c = i1 - i2;
          if (c == 0) {
            continue;
          }
          return c;
        }

        return len1 - len2;
      }

      @Override
      public BusinessException parse(Throwable t) {
        return new BusinessException("9999", t.getMessage());
      }

      @Override
      public String requestVersion(HttpServletRequest req) {
        return req.getHeader("api-version");
      }

    };
  }

  @Override
  protected JSONParser jsonParser() {
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
        return null;
      }
    };
  }

  @Override
  protected String[] packageNames() {
    return new String[] { "com.github.hualuomoli" };
  }

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    this.context = applicationContext;
  }

}
