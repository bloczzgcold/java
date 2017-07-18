package com.github.hualuomoli.gateway.api.lang;

/**
 * 请求版本号不支持
 */
public class RequestVersionNotSupportException extends RuntimeException {

  private static final long serialVersionUID = 3414288695150102402L;

  private String method;
  private String version;

  public RequestVersionNotSupportException(String method, String version) {
    super();
    this.method = method;
    this.version = version;
  }

  public RequestVersionNotSupportException(String method, String version, Throwable cause) {
    super(cause);
    this.method = method;
    this.version = version;
  }

  public String method() {
    return method;
  }

  public String version() {
    return version;
  }

  @Override
  public String getMessage() {
    return "method " + method + " for version " + version + " not support.";
  }

}
