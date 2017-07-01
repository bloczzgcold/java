package com.github.hualuomoli.gateway.server.interceptor.encrypt;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.github.hualuomoli.gateway.api.entity.Request;
import com.github.hualuomoli.gateway.api.entity.Response;
import com.github.hualuomoli.gateway.api.enums.EncryptionEnum;
import com.github.hualuomoli.gateway.api.lang.InvalidDataException;
import com.github.hualuomoli.gateway.api.lang.NoPartnerException;
import com.github.hualuomoli.gateway.server.DealerUtils;
import com.github.hualuomoli.gateway.server.dealer.EncryptionDealer;
import com.github.hualuomoli.gateway.server.interceptor.Interceptor;

/**
 * 加解密拦截器
 */
public class EncryptionInterceptor implements Interceptor {

  private static final Logger logger = Logger.getLogger(EncryptionInterceptor.class.getName());

  @Override
  public void preHandle(HttpServletRequest req, HttpServletResponse res, Request request) throws NoPartnerException, InvalidDataException {

    // 获取类型
    EncryptionEnum encryption = this.getType(request);
    if (encryption == null) {
      return;
    }

    // 获取处理类
    EncryptionDealer dealer = DealerUtils.getEncryptionDealer(encryption);
    if (dealer == null) {
      throw new InvalidDataException("there is no support encryption for " + encryption.name());
    }

    // 请求业务内容
    String bizContent = request.getBizContent();
    // 合作伙伴ID
    String partnerId = request.getPartnerId();

    if (logger.isLoggable(Level.INFO)) {
      logger.info("请求密文=" + bizContent);
    }
    bizContent = dealer.decrypt(bizContent, partnerId);
    if (logger.isLoggable(Level.INFO)) {
      logger.info("请求明文=" + bizContent);
    }

    // 重新设置请求数据
    request.setBizContent(bizContent);
  }

  @Override
  public void postHandle(HttpServletRequest req, HttpServletResponse res, Request request, Response response) {

    // 获取类型
    EncryptionEnum encryption = this.getType(request);
    if (encryption == null) {
      return;
    }

    // 获取处理类
    EncryptionDealer dealer = DealerUtils.getEncryptionDealer(encryption);

    // 响应数据
    String result = response.getResult();
    // 合作伙伴ID
    String partnerId = request.getPartnerId();

    if (logger.isLoggable(Level.INFO)) {
      logger.info("响应明文=" + result);
    }
    result = dealer.encrypt(result, partnerId);
    if (logger.isLoggable(Level.INFO)) {
      logger.info("响应密文=" + result);
    }

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
