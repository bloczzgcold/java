package com.github.hualuomoli.gateway.server.lang;

/**
 * 业务异常
 */
public class BusinessException extends RuntimeException {

  private static final long serialVersionUID = -4331507472650794379L;

  /** 业务处理编码 */
  private String subCode;
  /** 业务处理信息 */
  private String subMessage;
  /** 业务处理错误编码 */
  private String subErrorCode;

  public BusinessException(String subCode, String subMessage, String subErrorCode) {
    super(subMessage);
    this.subCode = subCode;
    this.subMessage = subMessage;
    this.subErrorCode = subErrorCode;
  }

  public BusinessException(String subCode, String subMessage, String subErrorCode, Throwable t) {
    super(subMessage, t);
    this.subCode = subCode;
    this.subMessage = subMessage;
    this.subErrorCode = subErrorCode;
  }

  public BusinessException(Enum<?> subCode, String subMessage, String subErrorCode) {
    this(subCode.name(), subMessage, subErrorCode);
  }

  public BusinessException(Enum<?> subCode, String subMessage, String subErrorCode, Throwable t) {
    this(subCode.name(), subMessage, subErrorCode, t);
  }

  public String getSubCode() {
    return subCode;
  }

  public String getSubMessage() {
    return subMessage;
  }

  public String getSubErrorCode() {
    return subErrorCode;
  }

  @Override
  public String toString() {
    return "BusinessException [subCode=" + subCode + ", subMessage=" + subMessage + ", subErrorCode=" + subErrorCode + "]";
  }

}
