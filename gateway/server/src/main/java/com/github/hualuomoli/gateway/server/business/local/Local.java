package com.github.hualuomoli.gateway.server.business.local;

/**
 * 当前线程数据
 */
public class Local {

  private static final ThreadLocal<String> LOCAL_PARTNER_ID = new ThreadLocal<>(); // 合作伙伴ID
  private static final ThreadLocal<String> LOCAL_METHOD = new ThreadLocal<>(); // 请求方法
  private static final ThreadLocal<String> LOCAL_BIZ_CONTENT = new ThreadLocal<String>(); // 请求的业务内容

  public static String getPartnerId() {
    return LOCAL_PARTNER_ID.get();
  }

  public static void setPartnerId(String partnerId) {
    LOCAL_PARTNER_ID.set(partnerId);
  }

  public static String getMethod() {
    return LOCAL_METHOD.get();
  }

  public static void setMethod(String method) {
    LOCAL_METHOD.set(method);
  }

  public static String getBizContent() {
    return LOCAL_BIZ_CONTENT.get();
  }

  public static void setBizContent(String bizContent) {
    LOCAL_BIZ_CONTENT.set(bizContent);
  }

}
