package com.github.hualuomoli.gateway.server.lang;

/**
 * 业务异常
 */
public class BusinessException extends RuntimeException {

  private static final long serialVersionUID = -6427061898550776746L;

  public BusinessException() {
    super();
  }

  public BusinessException(String message, Throwable cause) {
    super(message, cause);
  }

  public BusinessException(String message) {
    super(message);
  }

  public BusinessException(Throwable cause) {
    super(cause);
  }

}
