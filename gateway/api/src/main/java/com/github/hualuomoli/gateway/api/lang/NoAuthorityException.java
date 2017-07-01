package com.github.hualuomoli.gateway.api.lang;

/**
 * 没有访问权限
 */
public class NoAuthorityException extends RuntimeException {

  private static final long serialVersionUID = 5551557947850435422L;

  public NoAuthorityException() {
    super();
  }

  public NoAuthorityException(String message, Throwable cause) {
    super(message, cause);
  }

  public NoAuthorityException(String message) {
    super(message);
  }

  public NoAuthorityException(Throwable cause) {
    super(cause);
  }

}
