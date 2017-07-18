package com.github.hualuomoli.framework.mvc.version.method;

import java.lang.reflect.Method;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.servlet.mvc.condition.RequestCondition;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import com.github.hualuomoli.framework.mvc.version.annotation.ApiVersion;
import com.github.hualuomoli.framework.mvc.version.condition.VersionRequestCondition;

public class RequestMappingVersionHandlerMapping extends RequestMappingHandlerMapping {

  @Override
  protected RequestCondition<?> getCustomTypeCondition(Class<?> handlerType) {
    ApiVersion apiVersion = AnnotationUtils.getAnnotation(handlerType, ApiVersion.class);
    return apiVersion == null ? null : new VersionRequestCondition(apiVersion.value());
  }

  @Override
  protected RequestCondition<?> getCustomMethodCondition(Method method) {
    ApiVersion apiVersion = AnnotationUtils.getAnnotation(method, ApiVersion.class);
    return apiVersion == null ? null : new VersionRequestCondition(apiVersion.value());
  }

}
