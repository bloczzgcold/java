package com.github.hualuomoli.gateway.android.enums;

/**
 * 错误类型
 */
public enum ErrorTypeEnum {

  /** 404服务器未找到 */
  HTTP_SERVER_NOT_FOUND,
  /** 500服务器运行错误 */
  HTTP_SERVER_RUNTIME_ERROR,
  /** 其他错误 */
  HTTP_SERVER_OTHER_ERROR,

  /** 签名错误 */
  SECURITY_SIGN_ERROR,
  /** 验证签名错误 */
  SECURITY_VERIFY_ERROR,
  /** 加密错误 */
  SECURITY_ENCRYPT_ERROR,
  /** 解密错误 */
  SECURITY_DECRYPT_ERROR,

  /** 不合法的数据库JSON */
  INVALID_SERVER_JSON,

  /** 未知错误 */
  UNKNOWN;

}
