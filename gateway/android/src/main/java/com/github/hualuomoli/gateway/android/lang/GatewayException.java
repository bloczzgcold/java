package com.github.hualuomoli.gateway.android.lang;

import com.github.hualuomoli.gateway.android.enums.ErrorTypeEnum;

/**
 * HTTP错误
 */
public class GatewayException extends RuntimeException {

  private static final long serialVersionUID = -3449990968812143357L;

  private ErrorTypeEnum errorType;

  public GatewayException(ErrorTypeEnum errorType, String message) {
    super(message);
    this.errorType = errorType;
  }

  public ErrorTypeEnum getErrorType() {
    return errorType;
  }

}
