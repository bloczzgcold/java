package com.github.hualuomoli.gateway.server.dealer;

import com.github.hualuomoli.gateway.api.enums.SignatureEnum;
import com.github.hualuomoli.gateway.api.lang.InvalidDataException;
import com.github.hualuomoli.gateway.api.lang.NoPartnerException;

/**
 * 签名处理类
 */
public interface SignatureDealer {

  /**
   * 是否支持
   * @param signature 签名类型
   * @return 是否支持
   */
  boolean support(SignatureEnum signature);

  /**
   * 签名
   * @param origin 签名明文
   * @param partnerId 合作伙伴ID
   * @return 签名
   */
  String sign(String origin, String partnerId);

  /**
   * 验证签名
   * @param origin 签名明文
   * @param sign 签名
   * @param partnerId 合作伙伴ID
   * @return 签名是否合法
   * @throws NoPartnerException 合作伙伴未找到
   * @throws InvalidDataException 数据不合法
   */
  boolean verify(String origin, String sign, String partnerId) throws NoPartnerException, InvalidDataException;

}
