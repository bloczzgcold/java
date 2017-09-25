package com.github.hualuomoli.gateway.server.business;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.github.hualuomoli.gateway.server.business.dealer.FunctionDealer;
import com.github.hualuomoli.gateway.server.business.entity.Function;
import com.github.hualuomoli.gateway.server.business.interceptor.BusinessInterceptor;
import com.github.hualuomoli.gateway.server.business.parser.BusinessErrorParser;
import com.github.hualuomoli.gateway.server.business.parser.JSONParser;
import com.github.hualuomoli.gateway.server.lang.BusinessException;
import com.github.hualuomoli.gateway.server.lang.NoRouterException;

/**
 * 业务处理者
 */
public class AbstractBusinessHandler implements BusinessHandler {

  private static boolean success = false;
  private static final Object LOCK = new Object();

  // 业务拦截器
  private List<BusinessInterceptor> interceptors = new ArrayList<BusinessInterceptor>();
  // Function处理类
  private FunctionDealer functionDealer;
  // 业务错误解析器
  private BusinessErrorParser businessErrorParser;
  // JSON转换器
  private JSONParser jsonParser;
  // 实体类包路径
  private String[] packageNames;

  public void setInterceptors(List<BusinessInterceptor> interceptors) {
    this.interceptors = interceptors;
  }

  public void setFunctionDealer(FunctionDealer functionDealer) {
    this.functionDealer = functionDealer;
  }

  public void setBusinessErrorParser(BusinessErrorParser businessErrorParser) {
    this.businessErrorParser = businessErrorParser;
  }

  public void setJsonParser(JSONParser jsonParser) {
    this.jsonParser = jsonParser;
  }

  public void setPackageNames(String[] packageNames) {
    this.packageNames = packageNames;
  }

  // 验证并初始化
  private void checkAndInit() {
    if (success) {
      return;
    }

    synchronized (LOCK) {
      if (success) {
        return;
      }
      success = true;
      System.out.println("初始化网关functions");
      functionDealer.init();
    }

  }

  @Override
  public String execute(HttpServletRequest req, HttpServletResponse res, String partnerId, String method, String bizContent) throws NoRouterException, BusinessException {
    this.checkAndInit();

    Function function = functionDealer.getFunction(method, req);
    // 处理类
    Object handler = functionDealer.getDealer(function);

    List<Object> paramList = new ArrayList<Object>();
    Method m = function.getMethod();
    Class<?>[] parameterTypes = m.getParameterTypes();
    c: for (Class<?> parameterType : parameterTypes) {

      // HTTP request
      if (HttpServletRequest.class.isAssignableFrom(parameterType)) {
        paramList.add(req);
        continue;
      }

      // HTTP response
      if (HttpServletResponse.class.isAssignableFrom(parameterType)) {
        paramList.add(res);
        continue;
      }

      // List
      if (Collection.class.isAssignableFrom(parameterType)) {
        ParameterizedType genericParameterTypes = (ParameterizedType) m.getGenericParameterTypes()[0];
        Class<?> clazz = (Class<?>) genericParameterTypes.getActualTypeArguments()[0];
        paramList.add(jsonParser.parseArray(bizContent, clazz));
        continue;
      }

      // packageName
      String name = parameterType.getName();
      for (String packageName : packageNames) {
        if (name.startsWith(packageName)) {
          paramList.add(jsonParser.parseObject(bizContent, parameterType));
          continue c;
        }
      }

      // end
      throw new RuntimeException("there is not support type " + name);
    }

    // 请求参数
    Object[] params = paramList.toArray(new Object[] {});

    // 执行业务
    try {
      return this.deal(m, handler, params, req, res);
    } catch (BusinessException be) {
      throw be;
    } catch (Throwable t) {
      throw businessErrorParser.parse(t);
    }

  }

  /**
   * 业务处理
   * @param method 执行方法
   * @param handler 执行器
   * @param params 执行参数 
   * @param req HTTP请求
   * @param res HTTP响应
   * @return 业务处理结果
   */
  private String deal(Method method, Object handler, Object[] params, HttpServletRequest req, HttpServletResponse res) {

    // 前置拦截
    for (BusinessInterceptor interceptor : interceptors) {
      interceptor.preHandle(req, method, handler, params);
    }

    // 业务处理
    String result = null;
    BusinessException be = null;
    try {
      Object object = method.invoke(handler, params);
      if (object != null) {
        result = jsonParser.toJsonString(object);
      }
    } catch (IllegalAccessException e) {
      be = businessErrorParser.parse(e);
    } catch (IllegalArgumentException e) {
      be = businessErrorParser.parse(e);
    } catch (InvocationTargetException e) {
      be = businessErrorParser.parse(e.getTargetException());
    }

    if (be == null) {
      // 后置拦截
      for (BusinessInterceptor interceptor : interceptors) {
        interceptor.postHandle(req, res, result);
      }
    } else {
      // 错误拦截
      for (BusinessInterceptor interceptor : interceptors) {
        interceptor.afterCompletion(req, res, be);
      }
      throw be;
    }

    return result;
  }

}
