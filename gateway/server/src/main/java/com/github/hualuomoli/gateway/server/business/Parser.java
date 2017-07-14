package com.github.hualuomoli.gateway.server.business;

import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.github.hualuomoli.gateway.api.lang.BusinessException;

/**
 * 解析器
 */
public interface Parser {

  /**
   * 获取处理类
   * @return 处理类的类型
   */
  List<Class<?>> dealers();

  /**
   * 实例化处理器
   * @param dealerClazz 处理器类型
   * @return 处理器
   */
  Object instance(Class<?> dealerClazz);

  /**
   * 获取网关方法
   * @param clazz 类
   * @param method 方法
   * @return 网关方法
   */
  String loadGatewayMethod(Class<?> clazz, Method method);

  /**
   * 加载方法版本号
   * @param clazz 类
   * @param method 方法
   * @return 方法版本号
   */
  String loadVersion(Class<?> clazz, Method method);

  /**
   * 获取请求的版本号
   * @param req HTTP请求
   * @return 请求的版本号
   */
  String requestVersion(HttpServletRequest req);

  /**
   * 当前功能版本是否支持请求的版本
   * @param functionVersion 功能版本
   * @param requestVersion 请求版本
   * @return 是否支持
   */
  boolean support(String functionVersion, String requestVersion);

  /**
   * 比较两个版本 {@linkplain Comparator#compare(Object, Object)}
   * @param v1 比较的第一个版本
   * @param v2 比较的第二个版本
   * @return {@linkplain Comparator#compare(Object, Object)}
   */
  int compare(String v1, String v2);

  /**
   * 解析业务执行过程中出现的错误
   * @param t 执行过程中的错误
   * @return #BusinessException
   */
  BusinessException parse(Throwable t);

}
