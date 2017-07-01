package com.github.hualuomoli.gateway.server.business;

import java.lang.reflect.Method;

/**
 * 功能
 */
final class Function {

  /** 请求的网关方法 */
  String gatewayMethod;
  /** 请求的版本号 */
  String version;
  /** 方法 */
  Method method;
  /** 处理 */
  Class<?> clazz;

  Function(String gatewayMethod, String version, Method method, Class<?> clazz) {
    super();
    this.gatewayMethod = gatewayMethod;
    this.version = version;
    this.method = method;
    this.clazz = clazz;
  }

  @Override
  public String toString() {
    return "Function [gatewayMethod=" + gatewayMethod + ", version=" + version + ", method=" + method + ", clazz=" + clazz + "]";
  }

}
