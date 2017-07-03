package com.github.hualuomoli.gateway.client.interceptor.encrypt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.hualuomoli.gateway.api.entity.Request;
import com.github.hualuomoli.gateway.api.entity.Response;
import com.github.hualuomoli.gateway.api.enums.EncryptionEnum;
import com.github.hualuomoli.gateway.api.lang.InvalidDataException;
import com.github.hualuomoli.gateway.client.DealerUtils;
import com.github.hualuomoli.gateway.client.dealer.EncryptionDealer;
import com.github.hualuomoli.gateway.client.interceptor.Interceptor;

/**
 * 加解密拦截器
 */
public class EncryptionInterceptor implements Interceptor {

  private static final Logger logger = LoggerFactory.getLogger(EncryptionInterceptor.class);

  @Override
  public void preHandle(Request request) {

    // 获取类型
    EncryptionEnum encryption = this.getType(request);
    if (encryption == null) {
      return;
    }

    // 获取处理类
    EncryptionDealer dealer = DealerUtils.getEncryptionDealer(encryption);
    if (dealer == null) {
      return;
    }

    // 请求业务内容
    String bizContent = request.getBizContent();
    // 合作伙伴ID
    String partnerId = request.getPartnerId();

    logger.debug("请求明文={}", bizContent);
    bizContent = dealer.encrypt(bizContent, partnerId);
    logger.debug("请求密文={}", bizContent);

    // 重新设置请求数据
    request.setBizContent(bizContent);
  }

  @Override
  public void postHandle(Request request, Response response) throws InvalidDataException {

    // 获取类型
    EncryptionEnum encryption = this.getType(request);
    if (encryption == null) {
      return;
    }
    // 获取处理类
    EncryptionDealer dealer = DealerUtils.getEncryptionDealer(encryption);
    if (dealer == null) {
      return;
    }

    // 响应数据
    String result = response.getResult();
    // 合作伙伴ID
    String partnerId = request.getPartnerId();

    logger.debug("响应密文={}", result);
    result = dealer.decrypt(result, partnerId);
    logger.debug("响应明文={}", result);

    // 重新设置响应数据
    response.setResult(result);
  }

  /**
   * 获取类型
   * @param request 网关请求
   * @return 类型
   */
  private EncryptionEnum getType(Request request) {
    String type = request.getEncryptType();
    if (type == null || type.trim().length() == 0) {
      return null;
    }
    return Enum.valueOf(EncryptionEnum.class, type);
  }

}
