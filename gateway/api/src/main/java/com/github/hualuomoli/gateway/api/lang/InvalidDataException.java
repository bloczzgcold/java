package com.github.hualuomoli.gateway.api.lang;

/**
 * 数据不合法
 */
public class InvalidDataException extends RuntimeException {

  private static final long serialVersionUID = 5347796573788452508L;

  public InvalidDataException() {
    super();
  }

  public InvalidDataException(String message, Throwable cause) {
    super(message, cause);
  }

  public InvalidDataException(String message) {
    super(message);
  }

  public InvalidDataException(Throwable cause) {
    super(cause);
  }

}
