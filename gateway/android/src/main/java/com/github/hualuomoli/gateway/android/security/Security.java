package com.github.hualuomoli.gateway.android.security;

import com.github.hualuomoli.gateway.android.enums.ErrorTypeEnum;
import com.github.hualuomoli.gateway.android.lang.GatewayException;

/**
 * 安全
 */
public interface Security {

  /**
   * 获取签名类型
   * @return 签名类型
   */
  String getSignType();

  /**
   * 执行签名操作
   * @param origin 签名原文
   * @return 签名
   * @throws GatewayException 无法完成签名{@link ErrorTypeEnum#SECURITY_SIGN_ERROR}
   */
  byte[] sign(byte[] origin) throws GatewayException;

  /**
   * 执行验签操作
   * @param origin 签名原文
   * @param sign 签名
   * @return 签名是否合法
   * @throws GatewayException 无法完成签名验证{@link ErrorTypeEnum#SECURITY_VERIFY_ERROR}
   */
  boolean verify(byte[] origin, byte[] sign) throws GatewayException;

  /**
   * 获取加密类型,如果传输过程中不进行加密操作返回null
   * @return 加密类型
   */
  String getEncryptType();

  /**
   * 执行加密操作
   * @param text 明文
   * @return 密文
   * @throws GatewayException 无法完成加密操作{@link ErrorTypeEnum#SECURITY_ENCRYPT_ERROR}
   */
  byte[] encrypt(byte[] text) throws GatewayException;

  /**
   * 执行解密操作
   * @param chipherText 密文
   * @return 明文
   * @throws GatewayException 无法完成解密操作{@link ErrorTypeEnum#SECURITY_DECRYPT_ERROR}
   */
  byte[] decrypt(byte[] chipherText) throws GatewayException;

}
